package com.qiapps.qilibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.qiapps.qiads.QIAppOpenAds;
import com.qiapps.qiads.QILoaderNativeAds;
import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QInterstitial;

public class CustomApplication extends Application {

    private QInterstitial qInterstitial;
    //
    private UnifiedNativeAd content;
    private boolean failedLoadContent = false;
    private QINativeAds qiNativeAds;
    private QIAppOpenAds qiAppOpenAds;
    //

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this);
        qInterstitial = new QInterstitial(this, QInterstitial.TEST_AD_UNIT);
        qInterstitial.build();

        buildLoader();

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

    public void buildLoader(){
        new QILoaderNativeAds(this, QINativeAds.TEST_AD_UNIT, new QILoaderNativeAds.LoaderUtils() {
            @Override
            public void onLoad(UnifiedNativeAd unifiedNativeAd) {
                if(qiNativeAds != null){
                    qiNativeAds.setUnifiedNativeAd(unifiedNativeAd);
                    qiNativeAds.show();
                }else{
                    content = unifiedNativeAd;
                }
            }

            @Override
            public void onLoadFailed() {
                if(qiNativeAds != null){
                    qiNativeAds.showQIAppsAds();
                }else{
                    failedLoadContent = true;
                }

            }
        }).load();
    }

    public void setContainerAd(final QINativeAds qiNativeAds){
        if(content != null){
            qiNativeAds.setUnifiedNativeAd(content);
            qiNativeAds.show();
        }else if(failedLoadContent){
            qiNativeAds.showQIAppsAds();
        }else{
            this.qiNativeAds = qiNativeAds;
        }
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
