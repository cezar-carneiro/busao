package com.busao.gyn.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busao.gyn.R;

/**
 * Created by cezar on 13/10/16.
 */

public class TabFragment extends Fragment {

    private ViewPager mViewPager;

    public static final String TABS_PROVIDER_ARG = "TABS_PROVIDER";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x =  inflater.inflate(R.layout.tab_layout, null);
        final TabLayout tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        this.mViewPager = (ViewPager) x.findViewById(R.id.viewPager);

        Bundle args = getArguments();
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) args.get(TABS_PROVIDER_ARG);

        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        return x;
    }

    public void switchToTab(int index) {
        mViewPager.setCurrentItem(index);
    }

    public int getShowTabIndex(){
        return mViewPager.getCurrentItem();
    }

}
