package com.giangdm.moviereview.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class MoviesFragment extends Fragment {


    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        MoviesFragment moviesFragment = new MoviesFragment();
        return moviesFragment;
    }

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private List<Result> list;
    private boolean mIsChangeView = false;
    private int mPage = 1;
    private List<Result> totalList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        totalList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMovieAdapter = new MovieAdapter(totalList, getContext(), mRecyclerView);
        if (Common.isNetworkConnected(getContext())) {
            new LoadData().execute(Common.URL_LOAD_MOVIE_POPULAR + String.valueOf(mPage));

        } else {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
        }


        return view;
    }

    private void loadMoreList() {
        mMovieAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                mPage++;

                totalList.add(null);
                mMovieAdapter.notifyItemInserted(totalList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        totalList.remove(totalList.size() - 1);
                        mMovieAdapter.notifyItemRemoved(totalList.size());
                        new LoadData().execute(Common.URL_LOAD_MOVIE_POPULAR + String.valueOf(mPage));
                        mMovieAdapter.setLoaded();
                    }
                }, 2000);

            }
        });
    }

    class LoadData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return Common.getDataFromInter(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Gson gson = new Gson();
            Movie movie = null;
            if (s != null) {
                movie = gson.fromJson(s, Movie.class);
                if (movie != null) {
                    list = movie.getResults();
                    totalList.addAll(list);
                    mMovieAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mMovieAdapter);
                    // handler load more
                    loadMoreList();
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
        menuItem.setIcon(mIsChangeView ? R.drawable.ic_view_list : R.drawable.ic_view_grid);
    }
}
