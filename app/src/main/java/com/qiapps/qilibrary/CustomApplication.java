package com.qiapps.qilibrary;

import android.app.Application;
import android.util.Log;

import com.qiapps.qiads.QInterstitial;

public class CustomApplication extends Application {

    private QInterstitial qInterstitial;

    @Override
    public void onCreate() {
        super.onCreate();

        qInterstitial = new QInterstitial(this, QInterstitial.TEST_AD_UNIT);
        qInterstitial.build();
    }

    public void showInterstitial(final AppListener appListener){
        qInterstitial.setAdsUtils(new QInterstitial.AdsUtils() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                appListener.onAdCloded();
            }
        });
        qInterstitial.show();
    }

    public interface AppListener{
        void onAdCloded();
    }
}
