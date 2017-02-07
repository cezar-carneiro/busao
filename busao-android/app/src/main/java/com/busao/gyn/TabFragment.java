package com.busao.gyn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import com.busao.gyn.R;

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

        viewPager.setAdapter(new ViewPagerAdapter(getActivity(), getFragmentManager(), new String[]{"Lista", "Mapa"}));

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

}
