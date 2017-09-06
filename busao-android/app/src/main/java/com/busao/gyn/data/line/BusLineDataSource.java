package com.busao.gyn.data.line;

import com.busao.gyn.data.IBusLineDataSource;

import java.util.List;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

public class BusLineDataSource implements IBusLineDataSource {

    private BusLineDao mBusLineDao;

    public BusLineDataSource(BusLineDao mBusLineDao) {
        this.mBusLineDao = mBusLineDao;
    }

    @Override
    public void update(BusLine busLine) {
        mBusLineDao.update(busLine);
    }

    @Override
    public int countFavorites() {
        return mBusLineDao.countFavorites();
    }

    @Override
    public List<BusLinesWithStops> readFavorites() {
        return mBusLineDao.readFavorites();
    }

    public List<BusLine> searchByText(String input) {
        return mBusLineDao.searchByText("%" + input + "%");
    }
}
