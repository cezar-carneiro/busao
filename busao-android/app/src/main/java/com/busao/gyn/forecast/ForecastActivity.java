package com.busao.gyn.forecast;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.busao.gyn.R;
import com.busao.gyn.data.IForecastDataSource;
import com.busao.gyn.data.forecast.Bus;
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

        TextView stopNumber = (TextView) findViewById(R.id.stopLabel);
        TextView stopDescription = (TextView) findViewById(R.id.stopDescription);

        stopNumber.setText(FormatsUtils.formatBusStop(mStop.getCode(), mStop.getAddress()));
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
    public void onForecastFetched(final ForecastFetchedEvent forecastFetchedEvent){
        setRefreshingStatus(false);

        final TableLayout tl = (TableLayout) findViewById(R.id.forecastTableLayout);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tl.removeAllViewsInLayout();

                TableRow th = (TableRow) View.inflate(ForecastActivity.this, R.layout.forecast_list_header, null);
                tl.addView(th);

                for(final Bus bus: forecastFetchedEvent.getForecast().getData()){
                    TableRow tr = (TableRow) View.inflate(ForecastActivity.this, R.layout.forecast_list_item, null);
                    TextView line = (TextView) tr.findViewById(R.id.forecastLineTextView);
                    TextView next = (TextView) tr.findViewById(R.id.forecastNextTextView);
                    TextView following = (TextView) tr.findViewById(R.id.forecastFollowingTextView);

                    line.setText(bus.getLine());
                    if(bus.getNext() != null && bus.getNext().getMinutes() != null){
                        next.setText(bus.getNext().getMinutes() + " min.");
                    }else{
                        next.setText("--");
                    }

                    if(bus.getFollowing() != null && bus.getFollowing().getMinutes() != null){
                        following.setText(bus.getFollowing().getMinutes() + " min.");
                    }else{
                        following.setText("--");
                    }

                    tl.addView(tr);
                }
            }
        });

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
