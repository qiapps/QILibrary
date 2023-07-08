package com.qiapps.qilibrary;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.qiapps.qiads.QIAppOpenAds;
import com.qiapps.qiads.QIAppOpenSplash;
import com.qiapps.qiads.QILoaderNativeAds;
import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QInterstitial;

public class CustomApplication extends Application {

    private QInterstitial qInterstitial;

    private boolean failedLoadContent = false;
    public QINativeAds qiNativeAds;
    public QIAppOpenSplash qiAppOpenSplash;
    //

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this);
        //qInterstitial = new QInterstitial(this, QInterstitial.TEST_AD_UNIT);
        //qInterstitial.build();

        qiAppOpenSplash = new QIAppOpenSplash(this,QIAppOpenAds.TEST_AD_UNIT,2);



    }

    public void createNativeAds(Activity activity){
        qiNativeAds = new QINativeAds(activity,QINativeAds.TEST_AD_UNIT);
        qiNativeAds.build();
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

    public interface LoadListener{
        void onAdLoad();
        void onLoadFailed();
    }
}
