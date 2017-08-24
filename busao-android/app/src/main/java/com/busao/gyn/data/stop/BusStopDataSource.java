package com.busao.gyn.data.stop;

import android.support.annotation.NonNull;

import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.events.BusStopChanged;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by cezar on 23/01/17.
 */
public class BusStopDataSource implements IBusStopDataSource {

    private BusStopDao mBusStopDao;

    public BusStopDataSource(@NonNull BusStopDao busStopDao) {
        this.mBusStopDao = busStopDao;
    }

    @Override
    public void update(BusStop busStop) {
        mBusStopDao.update(busStop);
        EventBus.getDefault().post(new BusStopChanged(busStop));
    }

    @Override
    public List<BusStopWithLines> readFavorites() {
        return mBusStopDao.readFavorites();
    }

    @Override
    public BusStopWithLines readBusStop(Integer id) {
        return mBusStopDao.readBusStop(id);
    }

    @Override
    public List<BusStopWithLines> searchByLocation(LatLng[] area) {
        return mBusStopDao.searchByLocation(area[0].latitude, area[2].latitude, area[3].longitude, area[1].longitude);
    }

    @Override
    public List<BusStop> searchByText(String input) {
        return mBusStopDao.searchByText("%" + input + "%");
    }
}

