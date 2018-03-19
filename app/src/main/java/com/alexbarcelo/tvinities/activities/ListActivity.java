package com.alexbarcelo.tvinities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alexbarcelo.tvinities.R;
import com.alexbarcelo.tvinities.adapters.TVShowPaginatedListAdapter;
import com.alexbarcelo.tvinities.api.TVinitiesAPI;
import com.alexbarcelo.tvinities.app.CustomApplication;
import com.alexbarcelo.tvinities.fragments.ErrorDialogFragment;
import com.alexbarcelo.tvinities.moviedb.model.PaginatedList;
import com.alexbarcelo.tvinities.moviedb.model.TVShowSummary;
import com.sqisland.android.recyclerview.AutofitRecyclerView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Android activity that displays a grid with popular TV shows.
 *
 * @author Alex Barceló
 */

public class ListActivity extends AppCompatActivity {

    @Inject
    TVinitiesAPI mTVinitiesAPI;

    private TVShowPaginatedListAdapter mAdapter;

    private TextView mErrorMessage;
    private ViewGroup mErrorLayout;

    private boolean mIsLoadingMoreItems = false;
    private int mLastPageRetrieved = 0;
    private int mPageCount = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ((CustomApplication) getApplication()).getAppComponent().inject(this);

        mAdapter = new TVShowPaginatedListAdapter(this);
        mAdapter.setOnItemClickListener(new TVShowPaginatedListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
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

        mErrorMessage = findViewById(R.id.error_view_message);
        mErrorLayout = findViewById(R.id.activity_detail_error_view);
        Button errorRetryButton = findViewById(R.id.error_view_retry_button);
        errorRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextPage();
            }
        });

        AutofitRecyclerView tvShowListView = findViewById(R.id.activity_detail_similar_shows_list);
        tvShowListView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1 &&
                        mLastPageRetrieved < mPageCount && !mIsLoadingMoreItems &&
                        mAdapter.getDataState() != TVShowPaginatedListAdapter.PaginationState.ERROR) {
                    loadNextPage();
                }
            }
        });
        tvShowListView.setAdapter(mAdapter);
        loadNextPage();
    }

    private void loadNextPage() {
        mIsLoadingMoreItems = true;
        Call<PaginatedList<TVShowSummary>> call = mTVinitiesAPI.getPopularTVShows(mLastPageRetrieved + 1);
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
                    if (mLastPageRetrieved == 0) {
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mErrorMessage.setText(response.message());
                    } else {
                        ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(response.message());
                        dialogFragment.show(getSupportFragmentManager(), null);
                        mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.ERROR);
                    }
                }
                mIsLoadingMoreItems = false;
            }

            @Override
            public void onFailure(@NonNull Call<PaginatedList<TVShowSummary>> call, @NonNull Throwable t) {
                if (mLastPageRetrieved == 0) {
                    mErrorLayout.setVisibility(View.VISIBLE);
                    mErrorMessage.setText(t.getMessage());
                } else {
                    ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(t.getMessage());
                    dialogFragment.show(getSupportFragmentManager(), null);
                    mAdapter.setDataState(TVShowPaginatedListAdapter.PaginationState.ERROR);
                }
                mIsLoadingMoreItems = false;
            }
        });
    }
}