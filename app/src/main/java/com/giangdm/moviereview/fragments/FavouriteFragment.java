package com.giangdm.moviereview.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.activities.MainActivity;
import com.giangdm.moviereview.adapters.FavAdapter;
import com.giangdm.moviereview.models.Movie;
import com.giangdm.moviereview.models.Result;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mFavRclView;
    private FavAdapter mAdapter;
    private List<Result> list;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment favouriteFragment = new FavouriteFragment();
        return favouriteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mFavRclView = view.findViewById(R.id.fav_rcl_view);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mFavRclView.setLayoutManager(manager);
        list = MainActivity.dbManager.getAllFav();
        mAdapter = new FavAdapter(list, getContext(), this);
        mFavRclView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            list = MainActivity.dbManager.getAllFav();
            mAdapter = new FavAdapter(list, getContext(), this);
            mFavRclView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_change_view);
        menuItem.setVisible(false);
    }
}
