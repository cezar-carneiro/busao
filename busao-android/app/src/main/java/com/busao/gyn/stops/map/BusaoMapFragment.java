package com.busao.gyn.stops.map;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.BusStopDataSource;
import com.busao.gyn.stops.BusStop;
import com.busao.gyn.util.BusStopUtils;
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 09/02/17.
 */

public class BusaoMapFragment extends SupportMapFragment implements OnMapReadyCallback{

    private GoogleMap map;
    private Boolean firstZoom = true;

    private List<Marker> markers = new ArrayList<Marker>();
    private Circle locationCircle;

    private BusStopDataSource dataSource;

    public BusaoMapFragment(){
        getMapAsync(this);
    }

    public static BusaoMapFragment newInstance() {
        return new BusaoMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.dataSource = BusStopDataSource.getInstance(getActivity().getApplicationContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusStopDataSource.destroyInstance();
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
        this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.stop_details_map_dialog, null);

                BusStop stop = (BusStop) marker.getTag();

                TextView districtName = (TextView) v.findViewById(R.id.district_name);
                districtName.setText(stop.getNeighborhood());
                TextView cityName = (TextView) v.findViewById(R.id.city_name);
                cityName.setText(stop.getCity());
                TextView stopDescription = (TextView) v.findViewById(R.id.stop_description);
                if(StringUtils.isEmpty(stop.getReference())){
                    //stopDescription.setVisibility(View.GONE);
                    stopDescription.setText("(Sem descrição disponível)");
                    stopDescription.setTypeface(null, Typeface.ITALIC);
                }else{
                    stopDescription.setText(stop.getReference());
                    stopDescription.setTypeface(null, Typeface.NORMAL);
                }
                TextView linesAvailable = (TextView) v.findViewById(R.id.lines_available);
                linesAvailable.setText(stop.getLines());
                final ImageView imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);

                if(stop.getFavorite() != null && stop.getFavorite() ){
                    imageFavorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                }

                imageFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BusStop stop = (BusStop) marker.getTag();
                        stop.setFavorite(stop.getFavorite() == null ? true : !stop.getFavorite());
                        if(stop.getFavorite() != null && stop.getFavorite() ){
                            imageFavorite.setImageResource(R.drawable.ic_favorite);
                        }else{
                            imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                        }
                        dataSource.update(stop);
                        dataSource.refreshItems();
                        Snackbar.make(BusaoMapFragment.this.getView(), "Ponto " + stop.getCode() + " atualizado.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.StopDetailsMapDialogStyle);
                dialogBuilder.setTitle(BusStopUtils.formatBusStop(stop.getCode(), stop.getAddress()));
                dialogBuilder.setView(v);
                dialogBuilder.setNegativeButton("CLOSE", null);
                AlertDialog dialog = dialogBuilder.create();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                return false;
            }
        });
        this.map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location loc) {
                LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());

                if(firstZoom) {
                    changeCamera(ll);
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
        createCircle(ll);

        LatLng[] area = GeometryUtils.areaNearPosition(new LatLng(ll.latitude, ll.longitude), 500);
        List list = dataSource.search(area);
        refreshMarkers(list);
    }

    private void createCircle(LatLng ll) {
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
    }

    private void refreshMarkers(List<BusStop> stops){
        clearMarkers();

        for(BusStop stop : stops){
            createMarker(stop);
        }
    }

    private void clearMarkers() {
        for(Marker m: markers){
            m.remove();
        }

        markers.clear();
    }

    private void createMarker(BusStop stop) {
        MarkerOptions mo = new MarkerOptions().position(new LatLng(stop.getLatitude(), stop.getLongitude()))
                .title(String.valueOf(stop.getCode())).snippet(stop.getAddress());
        Marker marker = map.addMarker(mo);
        marker.setTag(stop);
        markers.add(marker);
    }

    private void changeCamera(LatLng loc){
        if(loc == null || map == null) {
            return;
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17), 2000, null);
    }

    public void showSingleStop(final BusStop stop) {
        if(map == null){
            return;
        }
        firstZoom = false;
        if(locationCircle != null){
            locationCircle.remove();
            locationCircle = null;
        }
        clearMarkers();
        createMarker(stop);
        changeCamera(new LatLng(stop.getLatitude(), stop.getLongitude()));
    }
}
