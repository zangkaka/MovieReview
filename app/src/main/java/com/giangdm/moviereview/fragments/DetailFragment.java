package com.giangdm.moviereview.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.activities.MainActivity;
import com.giangdm.moviereview.adapters.CastCrewAdapter;
import com.giangdm.moviereview.models.Result;
import com.giangdm.moviereview.models.cast_crew.CastCrew;
import com.giangdm.moviereview.models.cast_crew.CastDto;
import com.giangdm.moviereview.models.detail.Detail;
import com.giangdm.moviereview.utils.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private ImageView favImg;
    private TextView releaseDataTxt;
    private TextView ratingTxt;
    private ImageView thumbnailImg;
    private TextView overviewTxt;
    private Button reminderBtn;
    private RecyclerView castRcl;
    private RelativeLayout maskView;
    private Detail mDetail = null;
    public static String title = null;
    private String id = null;
    private CastCrewAdapter mCastCrewAdapter;
    private RecyclerView mCastRclView;
    private List<CastDto> castDtoList;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment getNewInstance(String urlMovie) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url_movie", urlMovie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MoviesFragment.mMovieAdapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(view);

        String url = getArguments().getString("url_movie");

        if (url != null) {
            new LoadData().execute(url);
            mCastCrewAdapter = new CastCrewAdapter(getContext(), castDtoList);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            ((LinearLayoutManager) manager).setOrientation(LinearLayoutManager.HORIZONTAL);
            mCastRclView.setLayoutManager(manager);
            mCastRclView.setAdapter(mCastCrewAdapter);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_fav_img:
                if (TextUtils.equals(String.valueOf(mDetail.getId()), id)) {
                    favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_off));
                    MainActivity.dbManager.deleteFavourite(String.valueOf(mDetail.getId()));
                } else {
                    favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_on));
                    Result result1 = new Result(mDetail.getId(), mDetail.getTitle(), mDetail.getReleaseDate(), mDetail.getVoteAverage(), mDetail.getOverview(), mDetail.getPosterPath(), mDetail.getAdult());
                    MainActivity.dbManager.addFavourite(result1);
                }
                break;
            default:
                break;
        }
    }

    private class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            maskView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return Common.getDataFromInter(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            maskView.setVisibility(View.GONE);
            Gson gson = new Gson();
            mDetail = gson.fromJson(s, Detail.class);
            if (mDetail != null) {
                new LoadCastCrew().execute(String.format(Common.URL_LOAD_CAST_CREW, mDetail.getId().toString()));
                releaseDataTxt.setText(mDetail.getReleaseDate());
                ratingTxt.setText(mDetail.getVoteAverage().toString());
                Picasso.with(getContext()).load(Common.URL_LOAD_IMAGE + mDetail.getPosterPath()).into(thumbnailImg);
                overviewTxt.setText(mDetail.getOverview());
                title = mDetail.getTitle();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(mDetail.getTitle());
                if (MainActivity.dbManager.getFavourite(String.valueOf(mDetail.getId())) != null) {
                    id = MainActivity.dbManager.getFavourite(String.valueOf(mDetail.getId())).getId().toString();
                }
                if (TextUtils.equals(String.valueOf(mDetail.getId()), id)) {
                    favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_on));
                } else {
                    favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_off));
                }
            }

        }
    }

    private class LoadCastCrew extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Common.getDataFromInter(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            CastCrew castCrew = null;
            castCrew = gson.fromJson(s, CastCrew.class);
            if (castCrew != null) {
                for (int i = 0; i < castCrew.getCast().size(); i++) {
                    castDtoList.add(new CastDto(castCrew.getCast().get(i).getName(), castCrew.getCast().get(i).getProfilePath()));
                }
                for (int i = 0; i < castCrew.getCrew().size(); i++) {
                    castDtoList.add(new CastDto(castCrew.getCrew().get(i).getName(), castCrew.getCrew().get(i).getProfilePath()));
                }

                mCastCrewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initViews(View view) {
        favImg = view.findViewById(R.id.detail_fav_img);
        releaseDataTxt = view.findViewById(R.id.detail_date_txt);
        ratingTxt = view.findViewById(R.id.detail_rating_txt);
        thumbnailImg = view.findViewById(R.id.detail_thumbnail);
        overviewTxt = view.findViewById(R.id.detail_overview_txt);
        reminderBtn = view.findViewById(R.id.detail_reminder_btn);
        castRcl = view.findViewById(R.id.detail_rcl_view);
        maskView = view.findViewById(R.id.maskview_layout);
        mCastRclView = view.findViewById(R.id.detail_rcl_view);
        castDtoList = new ArrayList<>();

        favImg.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (MainActivity.dbManager.getFavourite(String.valueOf(mDetail.getId())) != null) {
                id = MainActivity.dbManager.getFavourite(String.valueOf(mDetail.getId())).getId().toString();
            }
            if (TextUtils.equals(String.valueOf(mDetail.getId()), id)) {
                favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_on));
            } else {
                favImg.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_star_off));
            }
        }
    }
}
