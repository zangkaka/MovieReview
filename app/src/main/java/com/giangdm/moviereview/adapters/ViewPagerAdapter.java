package com.giangdm.moviereview.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdm.moviereview.R;
import com.giangdm.moviereview.fragments.AboutFragment;
import com.giangdm.moviereview.fragments.FavouriteFragment;
import com.giangdm.moviereview.fragments.MoviesFragment;
import com.giangdm.moviereview.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiangDM on 18-04-19
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
