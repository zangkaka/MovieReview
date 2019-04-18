package com.giangdm.moviereview.fragments;


import android.os.Bundle;
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

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.adapters.MovieAdapter;
import com.giangdm.moviereview.models.Result;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        createData();
        mMovieAdapter = new MovieAdapter(list, getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMovieAdapter);
        return view;
    }

    private void createData() {
        for (int i = 0; i < 20; i++) {
            list.add(new Result("Shazam", "/xnopI5Xtky18MPhK40cZAGAOVeV.jpg", "2000"));
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
