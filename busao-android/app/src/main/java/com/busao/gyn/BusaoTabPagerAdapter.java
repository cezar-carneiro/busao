package com.busao.gyn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.busao.gyn.lines.LinesFragment;
import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.BusaoMapFragment;

import java.io.Serializable;

/**
 * Created by cezar.carneiro on 22/08/2017.
 */

public class BusaoTabPagerAdapter extends FragmentPagerAdapter implements Serializable {

    public BusaoTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StopListFragment();
            case 1:
                return new LinesFragment();
            case 2:
                return new BusaoMapFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pontos";
            case 1:
                return "Linhas";
            case 2:
                return "Mapa";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
