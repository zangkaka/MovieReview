package com.giangdm.moviereview.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.adapters.MovieAdapter;
import com.giangdm.moviereview.interfaces.ILoadMore;
import com.giangdm.moviereview.models.Movie;
import com.giangdm.moviereview.models.Result;
import com.giangdm.moviereview.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        MoviesFragment moviesFragment = new MoviesFragment();
        return moviesFragment;
    }

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private MovieAdapter mMovieAdapter;
    private List<Result> list;
    private boolean mIsChangeView = false;
    private int mPage = 1;
    private List<Result> totalList;
    private RelativeLayout maskViewLayout;
    private Movie mMovie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        maskViewLayout = view.findViewById(R.id.maskview_layout);
        refreshLayout = view.findViewById(R.id.swipe_container);
        list = new ArrayList<>();
        totalList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMovieAdapter = new MovieAdapter(list, getContext(), mRecyclerView);
        mRecyclerView.setAdapter(mMovieAdapter);
        if (Common.isNetworkConnected(getContext())) {
            new LoadData().execute(Common.URL_LOAD_MOVIE_POPULAR + String.valueOf(mPage));
            // handler load more
            loadMoreList();
        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
        }


        return view;
    }

    private void loadMoreList() {

        mMovieAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (mPage <= mMovie.getTotalPages()) {
                    mPage++;
                    maskViewLayout.setVisibility(View.VISIBLE);
//                    list.add(null);
//                    mMovieAdapter.notifyItemInserted(list.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            list.remove(list.size() - 1);
//                            mMovieAdapter.notifyItemRemoved(list.size());
                            new LoadData().execute(Common.URL_LOAD_MOVIE_POPULAR + String.valueOf(mPage));
                            mMovieAdapter.setLoaded();
                            maskViewLayout.setVisibility(View.GONE);
                        }
                    }, 3000);
                } else {
                    Toast.makeText(getContext(), "Load data complete", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        if (Common.isNetworkConnected(getContext())) {
            list.clear();
            new LoadData().execute(Common.URL_LOAD_MOVIE_POPULAR + "1");
        } else {
            refreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "No network", Toast.LENGTH_SHORT).show();
        }

    }

    class LoadData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(getContext());
//            dialog.setMessage("Loading...");
//            dialog.setCancelable(false);
//            dialog.show();
            maskViewLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return Common.getDataFromInter(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshLayout.setRefreshing(false);
            maskViewLayout.setVisibility(View.GONE);
            Gson gson = new Gson();
            if (s != null) {
                mMovie = gson.fromJson(s, Movie.class);
                if (mMovie != null) {
                    list.addAll(mMovie.getResults());
                    mMovieAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_view:
                getActivity().supportInvalidateOptionsMenu();
                boolean isSwitched = mMovieAdapter.toogleItemViewType();
                mRecyclerView.setLayoutManager(isSwitched ? new LinearLayoutManager(getContext()) : new GridLayoutManager(getContext(), 2));
                mMovieAdapter.notifyDataSetChanged();
                mIsChangeView = !mIsChangeView;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_change_view);
//        menuItem.setIcon(mIsChangeView ? R.drawable.ic_view_list : R.drawable.ic_view_grid);
        menuItem.setVisible(false);
    }
}
