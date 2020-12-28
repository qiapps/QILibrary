package com.qiapps.qiads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class QILoaderNativeAds {

    private Context context;
    private String adUnit;
    private int countLoad = 0;
    private int max_count_load;
    private static final int MAX_COUNT_LOAD_DEFAULT = 3;
    private LoaderUtils loaderUtils;

    public QILoaderNativeAds(Context context, String adUnit,LoaderUtils loaderUtils) {
        this.context = context;
        this.adUnit = adUnit;
        this.max_count_load = MAX_COUNT_LOAD_DEFAULT;
        this.loaderUtils = loaderUtils;
    }

    public QILoaderNativeAds(Context context, String adUnit, int countLoad, LoaderUtils loaderUtils) {
        this.context = context;
        this.adUnit = adUnit;
        this.max_count_load = countLoad;
        this.loaderUtils = loaderUtils;
    }

    public void load(){
        countLoad++;
        AdLoader adLoader = new AdLoader.Builder(context, adUnit)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        if(unifiedNativeAd.getIcon() == null){
                            if(countLoad <= max_count_load){
                                load();
                            }else{
                                loaderUtils.onLoadFailed();
                            }
                        }else{
                            loaderUtils.onLoad(unifiedNativeAd);
                        }
                    }
                })
                .withAdListener(new AdListener() {

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        if(countLoad <= max_count_load){
                            load();
                        }else{
                            loaderUtils.onLoadFailed();
                        }
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public interface LoaderUtils{
        void onLoad(UnifiedNativeAd unifiedNativeAd);
        void onLoadFailed();
    }

}
