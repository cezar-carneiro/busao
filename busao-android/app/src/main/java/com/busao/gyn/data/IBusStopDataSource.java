package com.busao.gyn.data;

import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.data.stop.BusStopWithLines;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by cezar.carneiro on 18/08/2017.
 */

public interface IBusStopDataSource extends IDataSource<BusStop> {

    List<BusStopWithLines> readFavorites();

    BusStopWithLines readBusStop(Integer id);

    List<BusStopWithLines> searchByLocation(LatLng[] area);//TODO: in the future we will paginate

}
