package com.busao.gyn;

import android.support.v4.app.Fragment;

import com.busao.gyn.components.TabFragment;
import com.busao.gyn.lines.LinesFragment;
import com.busao.gyn.stops.list.StopListFragment;
import com.busao.gyn.stops.map.BusaoMapFragment;

import java.io.Serializable;

/**
 * Created by cezar.carneiro on 22/08/2017.
 */

public class BusStopTabProvider implements TabFragment.ITabFragmentProvider, Serializable {

    @Override
    public int count() {
        return 3;
    }

    @Override
    public String titleFor(int index) {
        switch (index) {
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
    public Fragment fragmentFor(int index) {
        switch (index) {
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
}