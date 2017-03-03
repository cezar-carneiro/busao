package com.busao.gyn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.BusaoMapFragment;

/**
 * Created by cezar on 13/10/16.
 */

public class TabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x =  inflater.inflate(R.layout.tab_layout,null);
        final TabLayout tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), new String[]{"Lista", "Mapa"}));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager doesn't work without the runnable.
         * Maybe a Support Library Bug.
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

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
            }

            return null;
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
