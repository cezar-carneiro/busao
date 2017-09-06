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
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.stop.BusStopDataSource;
import com.busao.gyn.data.stop.BusStopWithLines;
import com.busao.gyn.events.LineMapIconClickEvent;
import com.busao.gyn.events.StopMapIconClickEvent;
import com.busao.gyn.util.FormatsUtils;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cezar on 09/02/17.
 */

public class BusaoMapFragment extends SupportMapFragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private Boolean mFirstZoom = true;

    private List<Marker> mMarkers = new ArrayList<Marker>();
    private Circle mLocationCircle;

    private IBusStopDataSource mDataSource;

    public BusaoMapFragment(){
        getMapAsync(this);
    }

    public static BusaoMapFragment newInstance() {
        return new BusaoMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDataSource = new BusStopDataSource(BusaoDatabase.get(getContext()).busStopDao());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Manipulates the mGoogleMap once available.
     * This callback is triggered when the mGoogleMap is ready to be used.
     * This is where we can add mMarkers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        this.mGoogleMap = map;

        this.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        this.mGoogleMap.getUiSettings().setCompassEnabled(true);
        this.mGoogleMap.setMyLocationEnabled(true);
        this.mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.stop_details_map_dialog, null);

                BusStopWithLines stop = (BusStopWithLines) marker.getTag();

                TextView districtName = (TextView) v.findViewById(R.id.district_name);
                districtName.setText(stop.getStop().getNeighborhood());
                TextView cityName = (TextView) v.findViewById(R.id.city_name);
                cityName.setText(stop.getStop().getCity());
                TextView stopDescription = (TextView) v.findViewById(R.id.stop_description);
                if(StringUtils.isEmpty(stop.getStop().getReference())){
                    //stopDescription.setVisibility(View.GONE);
                    stopDescription.setText("(Sem descrição disponível)");
                    stopDescription.setTypeface(null, Typeface.ITALIC);
                }else{
                    stopDescription.setText(stop.getStop().getReference());
                    stopDescription.setTypeface(null, Typeface.NORMAL);
                }
                TextView linesAvailable = (TextView) v.findViewById(R.id.lines_available);
                linesAvailable.setText(stop.getFormatedLines());
                final ImageView imageFavorite = (ImageView) v.findViewById(R.id.imageFavorite);

                if(stop.getStop().isFavorite()){
                    imageFavorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                }

                imageFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BusStopWithLines stop = (BusStopWithLines) marker.getTag();
                        stop.getStop().setFavorite(!stop.getStop().isFavorite());
                        if(stop.getStop().isFavorite() ){
                            imageFavorite.setImageResource(R.drawable.ic_favorite);
                        }else{
                            imageFavorite.setImageResource(R.drawable.ic_favorite_border);
                        }
                        mDataSource.update(stop.getStop());
                        Snackbar.make(BusaoMapFragment.this.getView(), "Ponto " + stop.getStop().getCode() + " atualizado.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.StopDetailsMapDialogStyle);
                dialogBuilder.setTitle(FormatsUtils.formatBusStop(stop.getStop().getCode(), stop.getStop().getAddress()));
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
        this.mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location loc) {
                LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());

                if(mFirstZoom) {
                    changeCamera(ll, 17);
                    searchLocation(ll);
                    mFirstZoom = false;
                }
            }
        });

        this.mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                searchLocation(latLng);
            }
        });

    }

    private void searchLocation(LatLng ll){
        createCircle(ll);

        LatLng[] area = GeometryUtils.areaNearPosition(new LatLng(ll.latitude, ll.longitude), 500);
        List list = mDataSource.searchByLocation(area);
        refreshMarkers(list);
    }

    private void createCircle(LatLng ll) {
        if (mLocationCircle == null) {
            mLocationCircle = mGoogleMap.addCircle(new CircleOptions()
                    .center(ll)
                    .radius(500)
                    .strokeWidth(4)
                    .strokeColor(Color.argb(80, 255, 145, 0))
                    .fillColor(Color.argb(20, 255, 187, 0)));
        } else {
            mLocationCircle.setCenter(ll);
        }
    }

    private void refreshMarkers(List<BusStopWithLines> stops){
        clearMarkers();

        for(BusStopWithLines stop : stops){
            createMarker(String.valueOf(stop.getStop().getCode()),
                    stop.getStop().getAddress(),
                    stop,
                    new LatLng(stop.getStop().getLatitude(), stop.getStop().getLongitude()));
        }
    }

    private void clearMarkers() {
        for(Marker m: mMarkers){
            m.remove();
        }

        mMarkers.clear();
    }

    private void createMarker(String title, String snippet, Object tag, LatLng location) {
        MarkerOptions mo = new MarkerOptions().position(location)
                .title(title).snippet(snippet);
        Marker marker = mGoogleMap.addMarker(mo);
        marker.setTag(tag);
        mMarkers.add(marker);
    }

    private void changeCamera(LatLng loc, float zoom){
        if(loc == null || mGoogleMap == null) {
            return;
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom), 2000, null);
    }

    public void createMarkersForStops(BusStopWithLines... stops){
        if(mGoogleMap == null){
            return;
        }
        mFirstZoom = false;
        if(mLocationCircle != null){
            mLocationCircle.remove();
            mLocationCircle = null;
        }
        clearMarkers();
        if(stops != null && stops.length >= 1){
            changeCamera(new LatLng(stops[0].getStop().getLatitude(), stops[0].getStop().getLongitude()), 20);
            for(BusStopWithLines stop: stops){
                LatLng location = new LatLng(stop.getStop().getLatitude(), stop.getStop().getLongitude());
                createMarker(String.valueOf(stop.getStop().getCode()),
                        stop.getStop().getAddress(),
                        stop,
                        location);
            }
        }
    }

    @Subscribe
    public void onStopMapIconClick(StopMapIconClickEvent event) {
        createMarkersForStops(event.getStop());
    }

    @Subscribe
    public void onLineMapIconClick(LineMapIconClickEvent event) {
        List<BusStopWithLines> stops = mDataSource.listByLine(event.getLine().getLine().getCode());
        createMarkersForStops(stops.toArray(new BusStopWithLines[stops.size()]));
    }

}
