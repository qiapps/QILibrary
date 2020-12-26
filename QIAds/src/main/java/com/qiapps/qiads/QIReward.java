package com.qiapps.qiads;
import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class QIReward {

    private Activity context;
    private String adUnit;
    private RewardedAd rewardedAd;
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
        rewardedAd = new RewardedAd(context,adUnit);
        if(preload){
            build();
        }
    }

    private void reset(){
        rewardedAd = new RewardedAd(context,adUnit);
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
        rewardedAd.loadAd(new AdRequest.Builder().build(),new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                //load ad
                if(rewardUtils != null){
                    rewardUtils.onLoad();
                    inLoad = false;
                    if(showWhenLoad){
                        show();
                    }
                }
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
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
        if(rewardedAd.isLoaded()){
            rewardedAd.show(context, new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    //completou o vídeo
                    if(rewardUtils != null){
                        rewardUtils.onRewarded(rewardItem);
                        reset();
                    }
                }

                @Override
                public void onRewardedAdOpened() {
                    super.onRewardedAdOpened();
                    //abriu o vídeo
                    if(rewardUtils != null){
                        rewardUtils.onVideoOpen();
                    }
                }

                @Override
                public void onRewardedAdClosed() {
                    super.onRewardedAdClosed();
                    //fechou o vídeo
                    if(rewardUtils != null){
                        rewardUtils.onVideoCloded();
                        reset();
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    super.onRewardedAdFailedToShow(adError);
                    //vídeo falhou ao mostrar
                    if(rewardUtils != null){
                        rewardUtils.onVideoFailedToShow();
                        reset();
                    }
                }

            });
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
        void onVideoCloded();
        void onVideoFailedToShow();
    }

}
