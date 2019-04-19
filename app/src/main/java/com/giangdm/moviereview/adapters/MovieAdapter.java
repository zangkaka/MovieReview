package com.giangdm.moviereview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.activities.MainActivity;
import com.giangdm.moviereview.interfaces.ILoadMore;
import com.giangdm.moviereview.models.Result;
import com.giangdm.moviereview.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by GiangDM on 18-04-19
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> movieList;
    private Context mContext;
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;
    private RecyclerView mRecyclerView;

    private ILoadMore loadMore;
    private boolean isLoading = false;
    private int visibleThreshold = 5;
    private int lastVisibleItem = 0;
    private int totalItemCount = 0;
    private int VIEW_TYPE_LOADING = 2;


    public MovieAdapter(List<Result> movieList, Context mContext, RecyclerView recyclerView) {
        this.movieList = movieList;
        this.mContext = mContext;
        this.mRecyclerView = recyclerView;

        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = manager.getItemCount();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
            return new ListViewHolder(itemView);
        } else if (viewType == GRID_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
            return new GridViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Result result = (Result) movieList.get(position);
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).titleTxt.setText(result.getTitle());
            ((ListViewHolder) holder).releaseDataTxt.setText(result.getReleaseDate());
            ((ListViewHolder) holder).ratingTxt.setText(result.getVoteAverage() + "");
            ((ListViewHolder) holder).overViewTxt.setText(result.getOverview());
            Picasso.with(mContext).load(Common.URL_LOAD_IMAGE + result.getPosterPath()).into(((ListViewHolder) holder).thumbnailImg);
            if (result.getAdult()) {
                ((ListViewHolder) holder).sexImg.setVisibility(View.VISIBLE);
            } else {
                ((ListViewHolder) holder).sexImg.setVisibility(View.GONE);
            }
            final String id = MainActivity.dbManager.getFavourite(String.valueOf(result.getId()));
            Log.d(TAG, "onBindViewHolder: "+ String.valueOf(result.getId()));
            if (TextUtils.equals(String.valueOf(result.getId()), id)) {
                ((ListViewHolder) holder).starImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_on));
            } else {
                ((ListViewHolder) holder).starImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_off));
            }

            ((ListViewHolder) holder).starImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.equals(String.valueOf(result.getId()), id)) {
                        ((ListViewHolder) holder).starImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_off));
                        MainActivity.dbManager.deleteFavourite(String.valueOf(result.getId()));
                        notifyDataSetChanged();
                    } else {
                        ((ListViewHolder) holder).starImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_on));
                        MainActivity.dbManager.addFavourite(String.valueOf(result.getId()));
                        notifyDataSetChanged();
                    }
                }
            });
        } else if (holder instanceof GridViewHolder) {
            ((GridViewHolder) holder).titleTxt.setText(result.getTitle());
            Picasso.with(mContext).load(Common.URL_LOAD_IMAGE + result.getPosterPath()).into(((GridViewHolder) holder).thumbnailImg);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (movieList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            if (isSwitchView) {
                return LIST_ITEM;
            } else {
                return GRID_ITEM;
            }
        }
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public boolean toogleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTxt;
        public ImageView thumbnailImg;
        public TextView releaseDataTxt;
        public TextView ratingTxt;
        public TextView overViewTxt;
        public ImageView starImg;
        public ImageView sexImg;

        public ListViewHolder(View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.item_movie_list_title_txt);
            thumbnailImg = itemView.findViewById(R.id.item_movie_list_thumbnail_img);
            releaseDataTxt = itemView.findViewById(R.id.item_movie_list_date_txt);
            ratingTxt = itemView.findViewById(R.id.item_movie_list_rating_txt);
            overViewTxt = itemView.findViewById(R.id.item_movie_list_overview_txt);
            starImg = itemView.findViewById(R.id.item_movie_list_star_img);
            sexImg = itemView.findViewById(R.id.item_movie_list_sex_img);
        }
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailImg;
        public TextView titleTxt;

        public GridViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_movie_grid_thumbnail);
            titleTxt = itemView.findViewById(R.id.item_movie_grid_title_txt);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
