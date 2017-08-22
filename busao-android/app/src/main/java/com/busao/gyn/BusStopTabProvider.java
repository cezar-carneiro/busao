package com.busao.gyn;

import android.support.v4.app.Fragment;

import com.busao.gyn.components.TabFragment;
import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.BusaoMapFragment;

import java.io.Serializable;

/**
 * Created by cezar.carneiro on 22/08/2017.
 */

public class BusStopTabProvider implements TabFragment.ITabFragmentProvider, Serializable {

    @Override
    public int count() {
        return 2;
    }

    @Override
    public String titleFor(int index) {
        switch (index) {
            case 0:
                return "Lista";
            case 1:
                return "Mapa";
        }
        return "";
    }

    @Override
    public Fragment fragmentFor(int index) {
        switch (index) {
            case 0:
                return new StopListFragment();
            case 1:
                return new BusaoMapFragment();
        }
        return null;
    }
}