package com.qiapps.qiads;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class QInterstitial {

    private String adUnit;
    private Context context;
    private AdsUtils adsUtils;
    private InterstitialAd interstitialAd;
    private int countLoad = 0;
    private int max_count_load;
    private static final int MAX_COUNT_LOAD_DEFAULT = 3;
    public static final String TEST_AD_UNIT = "ca-app-pub-3940256099942544/1033173712";

    public QInterstitial(Context context, String adUnit, AdsUtils adsUtils) {
        this.adUnit = adUnit;
        this.context = context;
        this.adsUtils = adsUtils;
        max_count_load = MAX_COUNT_LOAD_DEFAULT;
    }

    public QInterstitial(Context context, String adUnit) {
        this.adUnit = adUnit;
        this.context = context;
        max_count_load = MAX_COUNT_LOAD_DEFAULT;
    }

    public void setMaxCountLoad(int i){
        max_count_load = i;
    }

    public void setAdsUtils(AdsUtils adsUtils) {
        this.adsUtils = adsUtils;
    }

    public void build(){
        countLoad ++;
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(adUnit);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(adsUtils != null){
                    adsUtils.onLoad();
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(adsUtils != null){
                    adsUtils.onClosed();
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if(countLoad < max_count_load){
                    build();
                }
            }
        });
    }

    public boolean show(){
        if(interstitialAd!= null){
            if(interstitialAd.isLoaded()){
                interstitialAd.show();
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public boolean isLoaded(){
        return interstitialAd.isLoaded();
    }

    public interface AdsUtils{
        void onLoad();
        void onClosed();
    }

}
