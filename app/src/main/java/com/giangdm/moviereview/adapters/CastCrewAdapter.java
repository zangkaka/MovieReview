package com.giangdm.moviereview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.models.cast_crew.CastDto;
import com.giangdm.moviereview.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by GiangDM on 23-04-19
 */
public class CastCrewAdapter extends RecyclerView.Adapter<CastCrewAdapter.CastViewHolder> {

    private Context context;
    private List<CastDto> castList;

    public CastCrewAdapter(Context context, List<CastDto> castList) {
        this.context = context;
        this.castList = castList;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast_crew, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        final CastDto cast = castList.get(position);
        Picasso.with(context).load(Common.URL_LOAD_IMAGE + cast.getPath()).error(R.drawable.no_profile).into(holder.thumbnailImg);
        holder.nameTxt.setText(cast.getName());
        holder.thumbnailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, cast.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (castList == null) {
            return 0;
        } else {
            return castList.size();
        }
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailImg;
        public TextView nameTxt;

        public CastViewHolder(View itemView) {
            super(itemView);
            thumbnailImg = itemView.findViewById(R.id.item_movie_grid_thumbnail);
            nameTxt = itemView.findViewById(R.id.item_movie_grid_title_txt);
        }
    }
}
