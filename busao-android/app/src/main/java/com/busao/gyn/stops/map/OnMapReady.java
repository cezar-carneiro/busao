package com.busao.gyn.stops.map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;

import com.busao.gyn.R;
import com.busao.gyn.data.DataBaseHelper;
import com.busao.gyn.data.BusStopDataSource;
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
 * Created by Ratan on 7/29/2015.
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
                View v = LayoutInflater.from(context).inflate(R.layout.stop_list_item, null, false);
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

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

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
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
