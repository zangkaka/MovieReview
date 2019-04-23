package com.giangdm.moviereview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.activities.MainActivity;
import com.giangdm.moviereview.interfaces.ILoadMore;
import com.giangdm.moviereview.models.Result;
import com.giangdm.moviereview.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GiangDM on 18-04-19
 */
public class FavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    private View.OnClickListener mOnClickListener;


    public FavAdapter(List<Result> movieList, Context mContext, View.OnClickListener onClickListener) {
        this.movieList = movieList;
        this.mContext = mContext;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
        return new ListViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Result result = (Result) movieList.get(position);

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
        String id = null;
        ((ListViewHolder) holder).starImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_on));
        if (MainActivity.dbManager.getFavourite(String.valueOf(result.getId())) != null)
            id = MainActivity.dbManager.getFavourite(String.valueOf(result.getId())).getId().toString();
        ((ListViewHolder) holder).starImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieList.remove(position);
                MainActivity.dbManager.deleteFavourite(String.valueOf(result.getId()));
                notifyDataSetChanged();
            }
        });
        ((ListViewHolder) holder).itemLayout.setOnClickListener(mOnClickListener);
        ((ListViewHolder) holder).itemLayout.setTag(result);

    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
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
        public RelativeLayout itemLayout;

        public ListViewHolder(View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.item_movie_list_title_txt);
            thumbnailImg = itemView.findViewById(R.id.item_movie_list_thumbnail_img);
            releaseDataTxt = itemView.findViewById(R.id.item_movie_list_date_txt);
            ratingTxt = itemView.findViewById(R.id.item_movie_list_rating_txt);
            overViewTxt = itemView.findViewById(R.id.item_movie_list_overview_txt);
            starImg = itemView.findViewById(R.id.item_movie_list_star_img);
            sexImg = itemView.findViewById(R.id.item_movie_list_sex_img);
            itemLayout = itemView.findViewById(R.id.item_list_layout);
        }
    }


}
