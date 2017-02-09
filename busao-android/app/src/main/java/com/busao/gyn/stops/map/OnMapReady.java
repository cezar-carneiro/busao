package com.busao.gyn.stops.map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.DataBaseHelper;
import com.busao.gyn.data.BusStopDataSource;
import com.busao.gyn.data.DataSource;
import com.busao.gyn.stops.BusStop;
import com.busao.gyn.util.GeometryUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 03/01/17.
 */
public class OnMapReady implements OnMapReadyCallback {

    private GoogleMap map;
    private Boolean firstZoom = true;

    private Context context;

    DataBaseHelper helper;
    SQLiteDatabase database;
    BusStopDataSource dataSource;

    private List<Marker> markers = new ArrayList<Marker>();
    private Circle locationCircle;

    public OnMapReady(Context context) {
        this.context = context;
        this.helper = new DataBaseHelper(context);
        this.database = helper.getWritableDatabase();
        this.dataSource = new BusStopDataSource(database);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setCompassEnabled(true);
        this.map.setMyLocationEnabled(true);
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View v = inflater.inflate(R.layout.stop_list_item, null);

                DataBaseHelper mDbHelper = new DataBaseHelper(context);
                SQLiteDatabase mDatabase = mDbHelper.getWritableDatabase();
                BusStopDataSource mDataSource = new BusStopDataSource(mDatabase);
                BusStop stop = mDataSource.read(Integer.valueOf(marker.getTitle()));

                mDbHelper.close();
                mDatabase.close();

                TextView stopNumber = (TextView) v.findViewById(R.id.stop_number);
                stopNumber.setText(String.valueOf(stop.getCode()));
                TextView streetName = (TextView) v.findViewById(R.id.street_name);
                streetName.setText(stop.getAddress());
                TextView districtName = (TextView) v.findViewById(R.id.district_name);
                districtName.setText(stop.getNeighborhood());
                TextView stopDescription = (TextView) v.findViewById(R.id.stop_description);
                stopDescription.setText(stop.getReference());
                ImageView imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);

                if(stop.getFavorite() != null && stop.getFavorite() ){
                    imageFavorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                }

                return v;
            }

        });
        this.map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DataBaseHelper mDbHelper = new DataBaseHelper(context);
                SQLiteDatabase mDatabase = mDbHelper.getWritableDatabase();
                BusStopDataSource mDataSource = new BusStopDataSource(mDatabase);
                BusStop stop = mDataSource.read(Integer.valueOf(marker.getTitle()));
                stop.setFavorite(stop.getFavorite() == null ? true : !stop.getFavorite());
                mDataSource.update(stop);

                mDbHelper.close();
                mDatabase.close();

                marker.showInfoWindow();
            }
        });
        this.map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location loc) {
                LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());

                if(firstZoom) {
                    changeCamera(loc);
                    searchLocation(ll);
                    firstZoom = false;
                }
            }
        });

        this.map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                searchLocation(latLng);
            }
        });

    }

    private void searchLocation(LatLng ll){
        if(locationCircle == null){
            locationCircle = map.addCircle(new CircleOptions()
                    .center(ll)
                    .radius(500)
                    .strokeWidth(4)
                    .strokeColor(Color.argb(80, 255, 145, 0))
                    .fillColor(Color.argb(20, 255, 187, 0)));
        } else {
            locationCircle.setCenter(ll);
        }

        LatLng[] area = GeometryUtils.areaNearPosition(new LatLng(ll.latitude, ll.longitude), 500);
        List list = dataSource.search(area);
        changeStopsMarkers(map, list);
    }

    private void changeStopsMarkers(GoogleMap map, List<BusStop> stops){
        for(Marker m: markers){
            m.remove();
        }

        markers.clear();

        for(BusStop stop : stops){
            MarkerOptions mo = new MarkerOptions().position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                    .title(String.valueOf(stop.getCode()));
            Marker marker = map.addMarker(mo);
            markers.add(marker);
        }
    }

    private void changeCamera(Location loc){
        if(loc == null || map == null) {
            return;
        }
        LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 17), 2000, null);
    }
}
