package com.qiapps.qilibrary;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.qiapps.qiads.QIAppOpenAds;
import com.qiapps.qiads.QILoaderNativeAds;
import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QInterstitial;

public class CustomApplication extends Application {

    private QInterstitial qInterstitial;

    private boolean failedLoadContent = false;
    private QINativeAds qiNativeAds;
    private QIAppOpenAds qiAppOpenAds;
    //

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this);
        //qInterstitial = new QInterstitial(this, QInterstitial.TEST_AD_UNIT);
        //qInterstitial.build();

        qiAppOpenAds = new QIAppOpenAds(this,QIAppOpenAds.TEST_AD_UNIT,true);
    }

    public void showAppOpenAds(){
        qiAppOpenAds.show();
    }

    public void cleanAppOpenAds(){
        qiAppOpenAds.setAutoShow(false);
    }

    public void reactivateAppOpenAds(){
        qiAppOpenAds.setAutoShow(true);
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
