package com.busao.gyn;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.OnMapReady;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by cezar on 13/10/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private CharSequence Titles[];
    private int NumbOfTabs;
    private SupportMapFragment supportMapFragment;
    private StopListFragment socialFragment;
    private OnMapReady onMapReady;

    public ViewPagerAdapter(Context context, FragmentManager fm, CharSequence mTitles[]) {
        super(fm);
        this.context = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mTitles.length;
        this.onMapReady = new OnMapReady(context);

        this.supportMapFragment =  SupportMapFragment.newInstance();
        this.supportMapFragment.getMapAsync(onMapReady);

        this.socialFragment = new StopListFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 : return (Fragment) socialFragment;
            case 1 : return supportMapFragment;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
