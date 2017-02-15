package com.busao.gyn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.busao.gyn.stops.BusStop;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 23/01/17.
 */
public class BusStopDataSource extends AbstractDataSource<BusStop> {

    public static final String TABLE_NAME = "pontos";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "codigoPonto";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS = "endereco";
    public static final String COLUMN_NEIGHBORHOOD = "bairro";
    public static final String COLUMN_CITY = "cidade";
    public static final String COLUMN_REFERENCE = "pontoReferencia";
    public static final String COLUMN_FAVORITE = "favorito";
    public static final String COLUMN_BUSES = "linhas";

    private static BusStopDataSource INSTANCE = null;

    private BusStopDataSource(Context context) {
        super(context);
    }

    public static BusStopDataSource getInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = new BusStopDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        if(INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    @Override
    public boolean create(BusStop entity) {
        if (entity == null) {
            return false;
        }
        long result = mDatabase.insert(TABLE_NAME, null,
                generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(BusStop entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(BusStop entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(entity), COLUMN_ID + " = "
                        + entity.getId(), null);
        return result != 0;
    }

    @Override
    public List<BusStop> read() {
        Cursor cursor = mDatabase.rawQuery("SELECT pontos.id,\n" +
                "\tpontos.codigoPonto,\n" +
                "\tpontos.latitude,\n" +
                "\tpontos.longitude,\n" +
                "\tpontos.endereco,\n" +
                "\tpontos.bairro,\n" +
                "\tpontos.cidade,\n" +
                "\tpontos.pontoReferencia,\n" +
                "\tpontos.favorito,\n" +
                "\tgroup_concat(pontoslinhas.codigoLinha,', ') AS linhas \n" +
                "FROM pontos \n" +
                "INNER JOIN pontoslinhas ON pontos.codigoPonto = pontoslinhas.codigoPonto \n" +
                "WHERE pontos.favorito = 1\n" +
                "GROUP BY pontos.codigoPonto\n", new String[]{} );

        List tests = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                tests.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tests;
    }

    @Override
    public BusStop read(Integer id){
        Cursor cursor = mDatabase.rawQuery("SELECT pontos.id,\n" +
                "\tpontos.codigoPonto,\n" +
                "\tpontos.latitude,\n" +
                "\tpontos.longitude,\n" +
                "\tpontos.endereco,\n" +
                "\tpontos.bairro,\n" +
                "\tpontos.cidade,\n" +
                "\tpontos.pontoReferencia,\n" +
                "\tpontos.favorito,\n" +
                "\tgroup_concat(pontoslinhas.codigoLinha,', ') AS linhas \n" +
                "FROM pontos \n" +
                "INNER JOIN pontoslinhas ON pontos.codigoPonto = pontoslinhas.codigoPonto \n" +
                "WHERE pontos.id = ?", new String[]{ id.toString() });

        BusStop stop = null;
        if (cursor != null && cursor.moveToFirst()) {
            if (!cursor.isAfterLast()) {
                stop = generateObjectFromCursor(cursor);
            }
            cursor.close();
        }
        return stop;
    }

    public List<BusStop> search(LatLng[] area){
        Cursor cursor = mDatabase.rawQuery("SELECT pontos.id,\n" +
                "\tpontos.codigoPonto,\n" +
                "\tpontos.latitude,\n" +
                "\tpontos.longitude,\n" +
                "\tpontos.endereco,\n" +
                "\tpontos.bairro,\n" +
                "\tpontos.cidade,\n" +
                "\tpontos.pontoReferencia,\n" +
                "\tpontos.favorito,\n" +
                "\tgroup_concat(pontoslinhas.codigoLinha,', ') AS linhas \n" +
                "FROM pontos \n" +
                "INNER JOIN pontoslinhas ON pontos.codigoPonto = pontoslinhas.codigoPonto \n" +
                "WHERE latitude > ? AND "
                        + " latitude < ? AND "
                        + " longitude < ? AND "
                        + " longitude > ? \n"
                + " GROUP BY pontos.codigoPonto\n", new String[]{
                String.valueOf(area[2].latitude),
                String.valueOf(area[0].latitude),
                String.valueOf(area[1].longitude),
                String.valueOf(area[3].longitude)
        });

        Log.i("busao", "latitude > "+area[2].latitude+" AND "
               + " latitude < "+area[0].latitude+" AND "
                + " longitude < "+area[1].longitude+" AND "
                 + " longitude > "+area[3].longitude);

        List<BusStop> stops = new ArrayList<BusStop>();
        if (cursor != null && cursor.moveToFirst()) {
           do {
                stops.add(generateObjectFromCursor(cursor));
            } while(cursor.moveToNext());
            cursor.close();
        }
        return stops;
    }

    public List<BusStop> search(String input){
        Cursor cursor = mDatabase.rawQuery("SELECT pontos.id," +
                    "\tpontos.codigoPonto,\n" +
                    "\tpontos.latitude,\n" +
                    "\tpontos.longitude,\n" +
                    "\tpontos.endereco,\n" +
                    "\tpontos.bairro,\n" +
                    "\tpontos.cidade,\n" +
                    "\tpontos.pontoReferencia,\n" +
                    "\tpontos.favorito,\n" +
                    "\tgroup_concat(pontoslinhas.codigoLinha,', ') AS linhas \n" +
                "FROM pontos \n" +
                "INNER JOIN pontoslinhas ON pontos.codigoPonto = pontoslinhas.codigoPonto \n" +
                "WHERE pontos.codigoPonto LIKE '%?%' \n" +
                    "\tOR pontos.endereco LIKE '%?%'\n" +
                "GROUP BY pontos.codigoPonto", new String[]{input, input});
        List<BusStop> stops = new ArrayList<BusStop>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                stops.add(generateObjectFromCursor(cursor));
            } while(cursor.moveToNext());
            cursor.close();
        }
        return stops;
    }

    public String[] getAllColumns() {
        return new String[] { COLUMN_ID, COLUMN_CODE, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_ADDRESS,
                COLUMN_NEIGHBORHOOD, COLUMN_CITY, COLUMN_REFERENCE, COLUMN_FAVORITE, COLUMN_BUSES};
    }

    public BusStop generateObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        BusStop stop = new BusStop();
        stop.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        stop.setCode(cursor.getInt(cursor.getColumnIndex(COLUMN_CODE)));
        stop.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
        stop.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
        stop.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));
        stop.setNeighborhood(cursor.getString(cursor.getColumnIndex(COLUMN_NEIGHBORHOOD)));
        stop.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
        stop.setReference(cursor.getString(cursor.getColumnIndex(COLUMN_REFERENCE)));
        try{
            int favorite = cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE));
            if(favorite > 0) {
                stop.setFavorite(true);
            }
        }catch (Exception e){
            stop.setFavorite(false);
        }
        stop.setBuses(cursor.getString(cursor.getColumnIndex(COLUMN_BUSES)));

        return stop;
    }

    public ContentValues generateContentValuesFromObject(BusStop entity) {
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, entity.getCode());
        values.put(COLUMN_LATITUDE, entity.getLatitude());
        values.put(COLUMN_LONGITUDE, entity.getLongitude());
        values.put(COLUMN_ADDRESS, entity.getAddress());
        values.put(COLUMN_NEIGHBORHOOD, entity.getNeighborhood());
        values.put(COLUMN_REFERENCE, entity.getReference());
        values.put(COLUMN_FAVORITE, entity.getFavorite() ? 1 : 0);
        //values.put(COLUMN_BUSES, entity.getBuses());

        return values;
    }
}

