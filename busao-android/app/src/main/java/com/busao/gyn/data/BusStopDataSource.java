package com.busao.gyn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.busao.gyn.events.BusStopChanged;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

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
    public static final String COLUMN_LINES = "linhas";

    private BusStopDataSource(Context context) {
        super(context);
    }

    public static BusStopDataSource newInstance(Context context){
        return new BusStopDataSource(context);
    }

    public void destroyInstance(){
        super.close();
    }

    @Override
    protected boolean doCreate(BusStop entity) {
        if (entity == null) {
            return false;
        }
        long result = mDatabase.insert(TABLE_NAME, null,
                generateContentValuesFromObject(entity));
        return result != -1;
    }

    @Override
    protected boolean doDelete(BusStop entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    protected boolean doUpdate(BusStop entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.update(TABLE_NAME,
                generateContentValuesFromObject(entity), COLUMN_ID + " = "
                        + entity.getId(), null);

        if(result != 0){
            EventBus.getDefault().post(new BusStopChanged(entity));
            return true;
        }

        return false;
    }

    @Override
    protected List<BusStop> doRead() {
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
    protected BusStop doRead(Integer id){
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
                "WHERE pontos.codigoPonto LIKE ? \n" +
                    "\tOR pontos.endereco LIKE ? \n" +
                    "\tOR pontos.pontoReferencia LIKE ? \n" +
                "GROUP BY pontos.codigoPonto\n", new String[]{"%"+input+"%", "%"+input+"%", "%"+input+"%"});
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
                COLUMN_NEIGHBORHOOD, COLUMN_CITY, COLUMN_REFERENCE, COLUMN_FAVORITE, COLUMN_LINES};
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
                stop.setFavorite(1);
            }
        }catch (Exception e){
            stop.setFavorite(0);
        }
        stop.setLines(cursor.getString(cursor.getColumnIndex(COLUMN_LINES)));

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
        values.put(COLUMN_FAVORITE, entity.getFavorite());

        return values;
    }
}

