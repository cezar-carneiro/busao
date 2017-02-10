package com.busao.gyn.stops.map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.BusStopDataSource;
import com.busao.gyn.data.DataBaseHelper;
import com.busao.gyn.data.DataSource;
import com.busao.gyn.stops.BusStop;
import com.busao.gyn.util.GeometryUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 09/02/17.
 */

public class BusaoMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Boolean firstZoom = true;

    private List<Marker> markers = new ArrayList<Marker>();
    private Circle locationCircle;

    private BusStopDataSource dataSource;

    public BusaoMapFragment(){
        getMapAsync(this);
    }

    public static SupportMapFragment newInstance() {
        return new BusaoMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataSource = new BusStopDataSource(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.dataSource.close();
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
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View v = inflater.inflate(R.layout.stop_list_item, null);

                BusStopDataSource mDataSource = new BusStopDataSource(getContext());
                BusStop stop = mDataSource.read(Integer.valueOf(marker.getTitle()));

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
                BusStopDataSource mDataSource = new BusStopDataSource(getContext());
                BusStop stop = mDataSource.read(Integer.valueOf(marker.getTitle()));
                stop.setFavorite(stop.getFavorite() == null ? true : !stop.getFavorite());
                mDataSource.update(stop);

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
