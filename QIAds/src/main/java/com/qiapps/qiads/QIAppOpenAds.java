package com.qiapps.qiads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

public class QIAppOpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpen";
    private String AD_UNIT_ID;
    public static final String TEST_AD_UNIT = "ca-app-pub-3940256099942544/3419835294";
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private final Application myApplication;
    private static boolean isShowingAd = false;
    private long loadTime = 0;
    private boolean autoShow;
    private boolean autoReloadAds = true;
    private int countStartEvent = 0;
    private int MIN_COUNT_EVENT_START_FOR_SHOW = 4;

    /** Constructor */
    public QIAppOpenAds(Application myApplication,String adUnit,boolean autoShow) {
        this.myApplication = myApplication;
        this.AD_UNIT_ID = adUnit;
        this.autoShow = autoShow;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /** LifecycleObserver methods */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        countStartEvent++;
        fetchAd();
        if(autoShow && countStartEvent >= MIN_COUNT_EVENT_START_FOR_SHOW){
            show();
        }
    }

    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

//        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
//            @Override
//            public void onAppOpenAdLoaded(AppOpenAd ad) {
//                QIAppOpenAds.this.appOpenAd = ad;
//                QIAppOpenAds.this.loadTime = (new Date()).getTime();
//
//            }
//            @Override
//            public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
//                // Handle the error.
//            }
//        };
        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                QIAppOpenAds.this.appOpenAd = appOpenAd;
                QIAppOpenAds.this.loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }


    public void show(){
        if(isAdAvailable() && !isShowingAd){
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            QIAppOpenAds.this.appOpenAd = null;
                            isShowingAd = false;
                            countStartEvent = 0;
                            if(autoReloadAds){
                                fetchAd();
                            }else{
                                isShowingAd = true;
                            }

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);
        }

    }

    public void setMinStartCount(int count){
        MIN_COUNT_EVENT_START_FOR_SHOW = count;
    }

    public boolean isAutoReloadAds() {
        return autoReloadAds;
    }

    public void setAutoReloadAds(boolean autoReloadAds) {
        this.autoReloadAds = autoReloadAds;
    }

    public boolean isAutoShow() {
        return autoShow;
    }

    public void setAutoShow(boolean autoShow) {
        this.autoShow = autoShow;
    }

    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /** Utility method to check if ad was loaded more than n hours ago. */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /** Utility method that checks if ad exists and can be shown. */
    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }
}