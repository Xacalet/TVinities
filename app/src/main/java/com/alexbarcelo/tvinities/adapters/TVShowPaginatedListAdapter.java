package com.alexbarcelo.tvinities.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexbarcelo.tvinities.R;
import com.alexbarcelo.tvinities.glide.GlideApp;
import com.alexbarcelo.tvinities.moviedb.model.TVShowSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.alexbarcelo.tvinities.api.TVinitiesAPI.BACKDROP_IMAGES_BASE_URL;

/**
 * Adapter class for visualizing TV show summary cards.
 *
 * @author Alex Barceló
 */

public class TVShowPaginatedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int LOADING_MORE_ITEMS_VIEW_TYPE = 1;

    private OnItemClickListener mOnItemClickListener;
    private OnRetryListener mOnRetryListener;

    private Context mContext;
    private List<TVShowSummary> mTVShows = new ArrayList<>();
    private PaginationState mDataState = PaginationState.DONE;

    public TVShowPaginatedListAdapter(Context context) {
        mContext = context;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOADING_MORE_ITEMS_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.show_list_loader_item, parent, false);
            return new LoadingMoreItemsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.show_list_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_VIEW_TYPE) {
            final TVShowSummary item = mTVShows.get(position);

            ItemViewHolder vh = (ItemViewHolder) holder;

            vh.mName.setText(item.getName());

            vh.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item.getId());
                    }
                }
            });

            Double voteRating = item.getVoteAverage();
            vh.mRating.setText(String.format(Locale.getDefault(), "%.1f", voteRating));
            Drawable drawable = DrawableCompat.wrap(vh.mRating.getBackground());
            if (voteRating >= 7.) {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(android.R.color.holo_green_dark));
            } else if (voteRating >= 5.) {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                DrawableCompat.setTint(drawable, mContext.getResources().getColor(android.R.color.holo_red_light));
            }

            GlideApp.with(mContext)
                    .load(BACKDROP_IMAGES_BASE_URL + item.getPosterPath())
                    .placeholder(R.drawable.no_poster_found)
                    .into(vh.mPoster);
        } else {
            LoadingMoreItemsViewHolder vh = (LoadingMoreItemsViewHolder) holder;

            vh.mRetryButton.setVisibility((mDataState == PaginationState.ERROR) ? View.VISIBLE : View.INVISIBLE);
            vh.mRetryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnRetryListener != null) {
                        mOnRetryListener.onRetry();
                    }
                }
            });

            vh.mProgressBar.setVisibility((mDataState == PaginationState.PENDING) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        // If we haven't reached the last page, this method will return the number of items retrieved
        // plus the special item indicating that more items are being loaded.
        return this.mTVShows.size() + ((mDataState != PaginationState.DONE) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        // The special item 'Loading more items' will be added at the end of the list.
        return (position == mTVShows.size()) ? LOADING_MORE_ITEMS_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    public void addItems(List<TVShowSummary> items) {
        this.mTVShows.addAll(items);
        notifyDataSetChanged();
    }

    public void setDataState(PaginationState newState) {
        this.mDataState = newState;
        notifyItemRangeChanged(mTVShows.size() - 1, 2);
    }

    public PaginationState getDataState() {
        return this.mDataState;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayout;
        private TextView mName;
        private TextView mRating;
        private ImageView mPoster;

        ItemViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.show_list_item_layout);
            mName = itemView.findViewById(R.id.activity_detail_name);
            mRating = itemView.findViewById(R.id.activity_detail_rating);
            mPoster = itemView.findViewById(R.id.show_list_item_poster);
        }
    }

    class LoadingMoreItemsViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;
        private Button mRetryButton;

        LoadingMoreItemsViewHolder(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.show_list_loader_item_progress_bar);
            mRetryButton = itemView.findViewById(R.id.show_list_loader_item_retry_button);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }

    public interface OnRetryListener {
        void onRetry();
    }

    public enum PaginationState {
        DONE, PENDING, ERROR
    }
}