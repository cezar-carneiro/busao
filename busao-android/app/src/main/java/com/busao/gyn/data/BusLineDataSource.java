package com.busao.gyn.data;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

public class BusLineDataSource extends AbstractDataSource<BusLine> {

    public static final String TABLE_NAME = "linhas";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CODE = "codigoPonto";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private BusLineDataSource(Context context) {
        super(context);
    }

    public static BusLineDataSource newInstance(Context context){
        return new BusLineDataSource(context);
    }

    @Override
    protected boolean doCreate(BusLine entity) {
        return false;
    }

    @Override
    protected BusLine doRead(Integer id) {
        return null;
    }

    @Override
    protected List<BusLine> doRead() {
        return null;
    }

    @Override
    protected boolean doUpdate(BusLine entity) {
        return false;
    }

    @Override
    protected boolean doDelete(BusLine entity) {
        return false;
    }
}
