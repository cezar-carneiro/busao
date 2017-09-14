package com.busao.gyn.forecast;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.IForecastDataSource;
import com.busao.gyn.data.forecast.ForecastDataSource;
import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.events.ForecastFetchedEvent;
import com.busao.gyn.events.HttpRequestErrorEvent;
import com.busao.gyn.events.NetworkErrorEvent;
import com.busao.gyn.util.FormatsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.OkHttpClient;

public class ForecastActivity extends AppCompatActivity {

    private SwipeRefreshLayout mForecastSwipeRefreshLayout;

    private IForecastDataSource mForecastDataSource;
    private BusStop mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastActivity.this.finish();
            }
        });

        mForecastSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.forecastSwipeRefreshLayout);
        mForecastSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mForecastSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchForecast();
            }
        });

        mStop = (BusStop) getIntent().getSerializableExtra("stop");

        TextView stopNumber = (TextView) findViewById(R.id.stopNumber);
        TextView districtName = (TextView) findViewById(R.id.districtName);
        TextView stopDescription = (TextView) findViewById(R.id.stopDescription);

        stopNumber.setText(FormatsUtils.formatBusStop(mStop.getCode()));
        districtName.setText(mStop.getAddress());
        stopDescription.setText(mStop.getReference());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        mForecastDataSource = new ForecastDataSource(new OkHttpClient());
        setRefreshingStatus(true);
        fetchForecast();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void fetchForecast(){

        mForecastDataSource.fetchSchedule(mStop.getCode());
    }

    private void setRefreshingStatus(final boolean status){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mForecastSwipeRefreshLayout.setRefreshing(status);
            }
        });
    }

    @Subscribe
    public void onForecastFetched(ForecastFetchedEvent forecastFetchedEvent){
        setRefreshingStatus(false);
    }

    @Subscribe
    public void onNetworkError(NetworkErrorEvent networkErrorEvent){
        setRefreshingStatus(false);
    }

    @Subscribe
    public void onHttpRequestError(HttpRequestErrorEvent httpRequestErrorEvent){
        setRefreshingStatus(false);
    }

}
