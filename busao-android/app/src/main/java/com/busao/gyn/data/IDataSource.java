package com.busao.gyn.data;

import java.util.List;

/**
 * Created by cezar.carneiro on 24/08/2017.
 */

public interface IDataSource<T> {

    void update(T item);

    List<T> searchByText(String input);//TODO: in the future we will paginate
}
