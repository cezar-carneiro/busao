package com.busao.gyn;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cezar.carneiro on 11/05/2017.
 */

public class BusaoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .throwSubscriberException(BuildConfig.DEBUG)
                .installDefaultEventBus();

        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
        }
    }
}
