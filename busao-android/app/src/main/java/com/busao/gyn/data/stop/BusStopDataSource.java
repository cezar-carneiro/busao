package com.busao.gyn.data.stop;

import android.support.annotation.NonNull;

import com.busao.gyn.data.IBusStopDataSource;
import com.google.android.gms.maps.model.LatLng;

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
        return mBusStopDao.searchByLocation(area[2].latitude, area[0].latitude, area[1].longitude, area[3].longitude);
    }

    @Override
    public List<BusStopWithLines> searchByText(String input) {
        return mBusStopDao.searchByText("%" + input + "%");
    }
}

