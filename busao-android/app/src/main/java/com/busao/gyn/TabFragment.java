package com.busao.gyn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busao.gyn.events.MapIconClickEvent;
import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.BusaoMapFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cezar on 13/10/16.
 */

public class TabFragment extends Fragment {

    private ViewPager mViewPager;

    public static final String ARG_POSITION = "POS";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x =  inflater.inflate(R.layout.tab_layout,null);
        final TabLayout tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        this.mViewPager = (ViewPager) x.findViewById(R.id.viewPager);

        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), new String[]{"Lista", "Mapa"}));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager doesn't work without the runnable.
         * Maybe a Support Library Bug.
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
                if(getArguments() != null){
                    mViewPager.setCurrentItem(getArguments().getInt(ARG_POSITION));
                }
            }
        });

        return x;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMapIconClick(MapIconClickEvent event) {
        swipeToMap();
    }

    private void swipeToMap(){
        mViewPager.setCurrentItem(1);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        private CharSequence mTitles[];

        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[]) {
            super(fm);
            this.mTitles = mTitles;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : return new StopListFragment();
                case 1 : return BusaoMapFragment.newInstance();
                default: return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

    }

}
