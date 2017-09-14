package com.busao.gyn.data.forecast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.busao.gyn.data.IForecastDataSource;
import com.busao.gyn.events.ForecastFetchedEvent;
import com.busao.gyn.events.HttpRequestErrorEvent;
import com.busao.gyn.events.NetworkErrorEvent;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public class ForecastDataSource implements IForecastDataSource {

    private static final String SCHEDULE_URL = "http://simapp.rmtcgoiania.com.br/pontoparada/previsaochegada";

    private OkHttpClient mHttpClient;
    private Gson mGson;

    public ForecastDataSource(OkHttpClient httpClient) {
        this.mHttpClient = httpClient;
        this.mGson = new Gson();
    }

    @Override
    public void fetchSchedule(@NonNull Integer stop) {
        fetchSchedule(stop, null);
    }

    @Override
    public void fetchSchedule(@NonNull Integer stop, @Nullable Integer line) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("qryIdPontoParada", String.valueOf(stop));
        if (line != null) {
            formBuilder.add("qryLinha", String.valueOf(line));
        }

        final Request request = new Request.Builder()
                .url(SCHEDULE_URL)
                .post(formBuilder.build())
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    EventBus.getDefault().post(new HttpRequestErrorEvent(response.code(), response.message()));
                    return;
                }
                JsonReader jsonReader = new JsonReader(new InputStreamReader(response.body().byteStream()));
                Forecast forecast = mGson.fromJson(jsonReader, Forecast.class);
                EventBus.getDefault().post(new ForecastFetchedEvent(forecast));
            }

            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post(new NetworkErrorEvent(e));
            }
        });

    }
}
