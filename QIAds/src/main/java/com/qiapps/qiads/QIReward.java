package com.qiapps.qiads;
import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class QIReward {

    private Activity context;
    private String adUnit;
    private RewardedAd mRewardedAd;
    private static final int MAX_COUNT = 3;
    private int countLoad = 0;
    private boolean inLoad = false;
    private RewardUtils rewardUtils;
    private boolean showWhenLoad = false;
    public static final String TEST_AD_UNIT = "ca-app-pub-3940256099942544/5224354917";

    public QIReward(Activity context, String adUnit, boolean preload,RewardUtils rewardUtils) {
        this.context = context;
        this.adUnit = adUnit;
        this.rewardUtils = rewardUtils;
        if(preload){
            build();
        }
    }

    private void reset(){
        mRewardedAd = null;
        inLoad = false;
        showWhenLoad = false;
        countLoad = 0;
    }

    public boolean isShowWhenLoad() {
        return showWhenLoad;
    }

    public void setShowWhenLoad(boolean showWhenLoad) {
        this.showWhenLoad = showWhenLoad;
    }

    public void build(){
        inLoad = true;
        countLoad++;
        RewardedAd.load(context, adUnit, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
                if(rewardUtils != null){
                    rewardUtils.onLoad();
                    inLoad = false;
                    if(showWhenLoad){
                        show();
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                if(countLoad < MAX_COUNT){
                    build();
                }else{
                    if(rewardUtils != null){
                        rewardUtils.onLoadFailed();
                        inLoad = false;
                        reset();
                    }
                }
            }
        });
    }

    public void show(){
        if(mRewardedAd != null){

            mRewardedAd.show(context, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    if(rewardUtils != null){
                        rewardUtils.onRewarded(rewardItem);
                    }
                }
            });
            if(rewardUtils != null){
                rewardUtils.onVideoOpen();
                reset();
            }
//            rewardedAd.show(context, new RewardedAdCallback() {
//                @Override
//                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                    //completou o vídeo
//                    if(rewardUtils != null){
//                        rewardUtils.onRewarded(rewardItem);
//                        reset();
//                    }
//                }
//
//                @Override
//                public void onRewardedAdOpened() {
//                    super.onRewardedAdOpened();
//                    //abriu o vídeo
//                    if(rewardUtils != null){
//                        rewardUtils.onVideoOpen();
//                    }
//                }
//
//                @Override
//                public void onRewardedAdClosed() {
//                    super.onRewardedAdClosed();
//                    //fechou o vídeo
//                    if(rewardUtils != null){
//                        rewardUtils.onVideoCloded();
//                        reset();
//                    }
//                }
//
//                @Override
//                public void onRewardedAdFailedToShow(AdError adError) {
//                    super.onRewardedAdFailedToShow(adError);
//                    //vídeo falhou ao mostrar
//                    if(rewardUtils != null){
//                        rewardUtils.onVideoFailedToShow();
//                        reset();
//                    }
//                }
//
//            });
        }else if(inLoad){
            showWhenLoad = true;
        }else{
            build();
            showWhenLoad = true;
        }
    }

    public interface RewardUtils{
        void onLoad();
        void onLoadFailed();
        void onRewarded(RewardItem rewardItem);
        void onVideoOpen();
        void onVideoFailedToShow();
    }

}
