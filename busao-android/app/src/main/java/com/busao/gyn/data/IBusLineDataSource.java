package com.busao.gyn.data;

import com.busao.gyn.data.line.BusLine;
import com.busao.gyn.data.line.BusLinesWithStops;

import java.util.List;

/**
 * Created by cezar.carneiro on 18/08/2017.
 */

public interface IBusLineDataSource {

    void update(BusLine busLine);

    List<BusLinesWithStops> readFavorites();//TODO: in the future we will paginate

    List<BusLinesWithStops> searchByText(String input);//TODO: in the future we will paginate
}
