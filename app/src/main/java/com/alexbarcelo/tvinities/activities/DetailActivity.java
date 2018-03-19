package com.alexbarcelo.tvinities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alexbarcelo.tvinities.R;
import com.alexbarcelo.tvinities.adapters.TVShowPaginatedListAdapter;
import com.alexbarcelo.tvinities.api.TVinitiesAPI;
import com.alexbarcelo.tvinities.app.CustomApplication;
import com.alexbarcelo.tvinities.fragments.ErrorDialogFragment;
import com.alexbarcelo.tvinities.glide.GlideApp;
import com.alexbarcelo.tvinities.moviedb.model.Genre;
import com.alexbarcelo.tvinities.moviedb.model.PaginatedList;
import com.alexbarcelo.tvinities.moviedb.model.TVShowDetail;
import com.alexbarcelo.tvinities.moviedb.model.TVShowSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Android activity that displays detailed information of a TV show, along with a list of similar shows.
 *
 * @author Alex Barceló
 */

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TV_SHOW_ID = "com.alexbarcelo.tvinities.TV_SHOW_ID";

    @Inject
    TVinitiesAPI mTVinitiesAPI;
    private long mTVShowId;

    private TVShowPaginatedListAdapter mAdapter;

    private TextView mErrorMessage;
    private ViewGroup mErrorLayout;
    private TextView mNameView;
    private ImageView mBackdropView;
    private TextView mGenresView;
    private TextView mYearsAndSeasonsView;
    private TextView mRatingView;
    private TextView mOverviewView;
    private RecyclerView mSimilarShowsView;
    private ScrollView mScrollView;
    private ProgressBar mProgressBar;

    private boolean mIsLoadingMoreItems = false;
    private int mLastPageRetrieved = 0;
    private int mPageCount = Integer.MAX_VALUE;

    private DataState mDataState = DataState.LOADING_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        this.mTVShowId = intent.getLongExtra(EXTRA_TV_SHOW_ID, -1);

        ((CustomApplication) getApplication()).getAppComponent().inject(this);

        mErrorMessage = findViewById(R.id.error_view_message);
        mErrorLayout = findViewById(R.id.activity_detail_error_view);
        Button errorRetryButton = findViewById(R.id.error_view_retry_button);
        errorRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadShowDetail();
            }
        });

        mNameView = findViewById(R.id.activity_detail_name);
        mBackdropView = findViewById(R.id.activity_detail_backdrop_image);
        mGenresView = findViewById(R.id.activity_detail_genres);
        mYearsAndSeasonsView = findViewById(R.id.activity_detail_year_and_seasons);
        mRatingView = findViewById(R.id.activity_detail_rating);
        mOverviewView = findViewById(R.id.activity_detail_overview);
        mSimilarShowsView = findViewById(R.id.activity_detail_similar_shows_list);
        mProgressBar = findViewById(R.id.activity_detail_progress_bar);
        mScrollView = findViewById(R.id.activity_detail_scroll_view);

        mAdapter = new TVShowPaginatedListAdapter(DetailActivity.this);
        mAdapter.setOnItemClickListener(new TVShowPaginatedListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TV_SHOW_ID, id);
                startActivity(intent);
            }
        });
        mAdapter.setOnRetryListener(new TVShowPaginatedListAdapter.OnRetryListener() {
            @Override
            public void onRetry() {
                loadNextPage();
            }
        });

        mErrorLayout.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.VISIBLE);

        loadShowDetail();
    }

    private void updateViewProperties() {
        mProgressBar.setVisibility(mDataState == DataState.LOADING_DATA ? View.VISIBLE : View.INVISIBLE);
        mErrorLayout.setVisibility(mDataState == DataState.ERROR ? View.VISIBLE : View.INVISIBLE);
        mScrollView.setVisibility(mDataState == DataState.DATA_LOADED ? View.VISIBLE : View.INVISIBLE);
    }

    private void setDataState(DataState newState) {
        mDataState = newState;
        updateViewProperties();
    }

    private void loadShowDetail() {
        setDataState(DataState.LOADING_DATA);
        mTVinitiesAPI.getTVShowDetail(mTVShowId).enqueue(new Callback<TVShowDetail>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetail> call, @NonNull Response<TVShowDetail> response) {

                TVShowDetail tvShowDetail = response.body();

                if (response.isSuccessful() && tvShowDetail != null) {

                    setDataState(DataState.DATA_LOADED);

                    mNameView.setText(tvShowDetail.getName());

                    String posterFullPath = "https://image.tmdb.org/t/p/w500" + tvShowDetail.getBackdropPath();
                    GlideApp.with(DetailActivity.this)
                            .load(posterFullPath)
                            .placeholder(R.drawable.no_backdrop_found)
                            .into(mBackdropView);

                    List<String> genreNames = new ArrayList<>();
                    for (Genre g : tvShowDetail.getGenres()) {
                        genreNames.add(g.getName());
                    }
                    mGenresView.setText(TextUtils.join(", ", genreNames));

                    String startYear = tvShowDetail.getFirstAirDate().substring(0, 4);
                    String endYear = tvShowDetail.isInProduction() ?
                            getResources().getString(R.string.in_production) :
                            tvShowDetail.getLastAirDate().substring(0, 4);
                    int numberOfSeasons = tvShowDetail.getNumberOfSeasons();
                    String seasons = String.format(getResources().getQuantityString(R.plurals.numberOfSeasons, numberOfSeasons), numberOfSeasons);
                    mYearsAndSeasonsView.setText(String.format("%s - %s | %s", startYear, endYear, seasons));

                    Double voteRating = tvShowDetail.getVoteAverage();
                    mRatingView.setText(String.format(Locale.getDefault(), "%.1f", voteRating));
                    if (voteRating >= 7.) {
                        mRatingView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else if (voteRating >= 5.) {
                        mRatingView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                    } else {
                        mRatingView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }

                    mOverviewView.setText(tvShowDetail.getOverview());

                    mSimilarShowsView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    mSimilarShowsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            if (layoutManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1 &&
                                    mLastPageRetrieved < mPageCount && !mIsLoadingMoreItems &&
                                    mAdapter.getDataState() != TVShowPaginatedListAdapter.PaginationState.ERROR) {
                                loadNextPage();
                            }
                        }
                    });
                    mSimilarShowsView.setAdapter(mAdapter);
                    loadNextPage();
                } else {
                    setDataState(DataState.ERROR);
                    mErrorMessage.setText(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetail> call, @NonNull Throwable t) {
                setDataState(DataState.ERROR);
                mErrorMessage.setText(t.getMessage());
            }
        });
    }

    private void loadNextPage() {
        mIsLoadingMoreItems = true;
        Call<PaginatedList<TVShowSummary>> call = mTVinitiesAPI.getSimilarShows(mTVShowId, mLastPageRetrieved + 1);
        call.enqueue(new Callback<PaginatedList<TVShowSummary>>() {
            @Override
            public void onResponse(@NonNull Call<PaginatedList<TVShowSummary>> call, @NonNull Response<PaginatedList<TVShowSummary>> response) {
                if (response.isSuccessful()) {
                    PaginatedList<TVShowSummary> paginatedList = response.body();
                    if (paginatedList != null) {
                        mErrorLayout.setVisibility(View.INVISIBLE);
                        mPageCount = paginatedList.getTotalPages();
                        mLastPageRetrieved++;
                        mAdapter.addItems(paginatedList.getResults());
                        if (mLastPageRetrieved >= mPageCount) {
                            mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.DONE);
                        } else {
                            mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.PENDING);
                        }

                    }
                } else {
                    ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(response.message());
                    dialogFragment.show(getSupportFragmentManager(), null);
                    mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.ERROR);
                }
                mIsLoadingMoreItems = false;
            }

            @Override
            public void onFailure(@NonNull Call<PaginatedList<TVShowSummary>> call, @NonNull Throwable t) {
                ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(t.getMessage());
                dialogFragment.show(getSupportFragmentManager(), null);
                mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.ERROR);
                mIsLoadingMoreItems = false;
            }
        });
    }

    private enum DataState {
        ERROR, LOADING_DATA, DATA_LOADED
    }
}
