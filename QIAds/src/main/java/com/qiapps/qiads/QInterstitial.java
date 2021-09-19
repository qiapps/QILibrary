package com.qiapps.qiads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class QInterstitial {

    private String adUnit;
    private Activity context;
    private AdsUtils adsUtils;
    private InterstitialAd mInterstitialAd;
    private int countLoad = 0;
    private int max_count_load;
    private static final int MAX_COUNT_LOAD_DEFAULT = 3;
    public static final String TEST_AD_UNIT = "ca-app-pub-3940256099942544/1033173712";

    public QInterstitial(Activity context, String adUnit, AdsUtils adsUtils) {
        this.adUnit = adUnit;
        this.context = context;
        this.adsUtils = adsUtils;
        max_count_load = MAX_COUNT_LOAD_DEFAULT;
    }

    public QInterstitial(Activity context, String adUnit) {
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
        //interstitialAd = new InterstitialAd(context);
        InterstitialAd.load(context, adUnit, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                if(adsUtils != null){
                    adsUtils.onLoad();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if(countLoad < max_count_load){
                    build();
                }
            }
        });

        //interstitialAd.setAdUnitId(adUnit);
        //interstitialAd.loadAd(new AdRequest.Builder().build());
//        interstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                if(adsUtils != null){
//                    adsUtils.onLoad();
//                }
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                if(adsUtils != null){
//                    adsUtils.onClosed();
//                }
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                if(countLoad < max_count_load){
//                    build();
//                }
//            }
//        });
    }

    public boolean show(){
        if(mInterstitialAd!= null){
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if(adsUtils != null){
                        adsUtils.onClosed();
                    }
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if(adsUtils != null){
                        adsUtils.onClosed();
                    }
                }
            });
            mInterstitialAd.show(context);
        }
        return false;
    }

    public boolean isLoaded(){
        return mInterstitialAd != null;
    }

    public interface AdsUtils{
        void onLoad();
        void onClosed();
    }

}
