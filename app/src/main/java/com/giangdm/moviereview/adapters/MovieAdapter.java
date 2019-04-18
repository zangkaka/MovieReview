package com.giangdm.moviereview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.models.Result;
import com.giangdm.moviereview.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GiangDM on 18-04-19
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Result> movieList;
    private Context mContext;
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    public MovieAdapter(List<Result> movieList, Context mContext) {
        this.movieList = movieList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
            return new ListViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, parent, false);
            return new GridViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result result = (Result) movieList.get(position);
        if (holder instanceof ListViewHolder) {
            ((ListViewHolder) holder).titleTxt.setText(result.getTitle());
            ((ListViewHolder) holder).releaseDataTxt.setText(result.getReleaseDate());
            ((ListViewHolder) holder).ratingTxt.setText(result.getVoteAverage() + "");
            ((ListViewHolder) holder).overViewTxt.setText(result.getOverview());
            Picasso.with(mContext).load(Common.URL_LOAD_IMAGE + result.getPosterPath()).into(((ListViewHolder) holder).thumbnailImg);
        } else if (holder instanceof GridViewHolder) {
            ((GridViewHolder) holder).titleTxt.setText(result.getTitle());
            Picasso.with(mContext).load(Common.URL_LOAD_IMAGE + result.getPosterPath()).into(((GridViewHolder) holder).thumbnailImg);
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
        if (isSwitchView) {
            return LIST_ITEM;
        } else {
            return GRID_ITEM;
        }
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
}
