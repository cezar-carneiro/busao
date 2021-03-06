package com.busao.gyn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.busao.gyn.components.TabFragment;
import com.busao.gyn.data.BusaoDatabase;
import com.busao.gyn.data.IBusLineDataSource;
import com.busao.gyn.data.IBusStopDataSource;
import com.busao.gyn.data.line.BusLineDataSource;
import com.busao.gyn.data.stop.BusStopDataSource;
import com.busao.gyn.events.BusLineChanged;
import com.busao.gyn.events.BusStopChanged;
import com.busao.gyn.events.LineMapIconClickEvent;
import com.busao.gyn.events.StopMapIconClickEvent;
import com.busao.gyn.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private IBusStopDataSource mStopsDataSource;
    private IBusLineDataSource mLinesDataSource;
    private TextView mTotalStopsTextView;
    private TextView mTotalLinesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add_task);
//        fab_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//            }
//        });

        mTotalStopsTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.totalStopsTextView);
        mTotalLinesTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.totalLinesTextView);
        mStopsDataSource = new BusStopDataSource(BusaoDatabase.get(this).busStopDao());
        mLinesDataSource = new BusLineDataSource(BusaoDatabase.get(this).busLineDao());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            } else {
                ;//TODO: o q fazer?
            }
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putSerializable(TabFragment.TABS_PROVIDER_ARG, (Serializable) new BusaoTabPagerAdapter(fragmentManager));
        TabFragment tabFragment = (TabFragment) TabFragment.instantiate(this, TabFragment.class.getName(), bundle);
        tabFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.containerView, tabFragment).commit();

        int stopsCount = mStopsDataSource.countFavorites();
        mTotalStopsTextView.setText(stopsCount + " Pontos");
        int linesCount = mLinesDataSource.countFavorites();
        mTotalLinesTextView.setText(linesCount + " Linhas");

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onStopMapIconClick(StopMapIconClickEvent event) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TabFragment tabFragment = (TabFragment) fragmentManager.findFragmentById(R.id.containerView);
        tabFragment.switchToTab(2);
    }

    @Subscribe
    public void onLineMapIconClick(LineMapIconClickEvent event) {
        onStopMapIconClick(null);
    }

    @Subscribe
    public void onBusLineChanged(BusLineChanged event){
        int count = mLinesDataSource.countFavorites();
        mTotalLinesTextView.setText(count + " Linhas");
    }

    @Subscribe
    public void onBusStopChanged(BusStopChanged event){
        int count = mStopsDataSource.countFavorites();
        mTotalStopsTextView.setText(count + " Pontos");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO: implementar o tratamento do resultado das permissões
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);

            FragmentManager fragmentManager = getSupportFragmentManager();
            TabFragment tabFragment = (TabFragment) fragmentManager.findFragmentById(R.id.containerView);
            int currTab = tabFragment.getShowTabIndex();
            if(currTab == 0){
                intent.putExtra(SearchActivity.TYPE_KEY, SearchActivity.SearchType.STOP);
            } else {
                intent.putExtra(SearchActivity.TYPE_KEY, SearchActivity.SearchType.LINE);
            }

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_stops:

                Fragment tabFragment = fragmentManager.findFragmentById(R.id.containerView);
                if(tabFragment == null || !(tabFragment instanceof TabFragment)){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TabFragment.TABS_PROVIDER_ARG, (Serializable) new BusaoTabPagerAdapter(fragmentManager));
                    tabFragment = (TabFragment) TabFragment.instantiate(this, TabFragment.class.getName(), bundle);
                    tabFragment.setArguments(bundle);
                }

                fragmentTransaction.replace(R.id.containerView, tabFragment).commit();
                setTitle("Pontos");

                break;
            case R.id.nav_remove_ads:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_rate:
                break;
            case R.id.nav_developer:
                break;
            case R.id.nav_info:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Info");
                dialog.setMessage("Versão 1.0.0");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Ok", null);
                dialog.show();
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
