package com.qiapps.qiads;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.Timer;
import java.util.TimerTask;

public class QIntersticialReward {

    private Activity activity;
    private RewardedInterstitialAd rewardedInterstitialAd;

    private Utils utils;

    private boolean isLoad = false;

    public static final String AD_UNIT = "ca-app-pub-3940256099942544/5354046379";

    private String adUnit;

    public QIntersticialReward(Activity activity, String adUnit) {
        this.activity = activity;
        this.adUnit = adUnit;
    }

    public QIntersticialReward(Activity activity, String adUnit, Utils utils) {
        this.activity = activity;
        this.adUnit = adUnit;
        this.utils = utils;
    }


    public void build(){
        RewardedInterstitialAd.load(activity,adUnit,
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        //Log.d(TAG, "Ad was loaded.");
                        rewardedInterstitialAd = ad;
                        isLoad = true;
                        if(utils != null){
                            utils.onAdLoad();
                        }
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        //Log.d(TAG, loadAdError.toString());
                        rewardedInterstitialAd = null;
                    }
                });
    }

    public void show(){
        if(rewardedInterstitialAd!=null){
            rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    //Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    //Log.d(TAG, "Ad dismissed fullscreen content.");
                    rewardedInterstitialAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    //Log.e(TAG, "Ad failed to show fullscreen content.");
                    rewardedInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    //Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    //Log.d(TAG, "Ad showed fullscreen content.");
                }
            });


            rewardedInterstitialAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    if(utils != null){
                        utils.onUserEarnedReward(rewardItem);
                    }
                }
            });
        }
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public boolean isLoad(){
        return this.isLoad;
    }

    private int count = 5;

    public void showDialogReward(Activity context, String reward, int colorTheme, Drawable img_reward){
        count = 5;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View cont = inflater.inflate(R.layout.qi_dialog_reward_ads, null);
        dialog.setView(cont);



        final AlertDialog myDialog = dialog.create();

        TextView rewardTxt = cont.findViewById(R.id.reward_txt);
        rewardTxt.setText(reward);

        LinearLayout title = cont.findViewById(R.id.header);
        if(colorTheme != 0){
            title.getBackground().setTint(colorTheme);
        }

        if(img_reward !=null){
            ImageView img = cont.findViewById(R.id.img_reward);
            img.setImageDrawable(img_reward);
        }


        final TextView txtTime = cont.findViewById(R.id.txt_time);
        String s = "Anúncio em " + count;
        txtTime.setText(s);
        final Handler h = new Handler();

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count--;
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if(count > 0){
                            String s = "Anúncio em " + count;
                            txtTime.setText(s);
                        }else{
                            //esgotou o tempo
                            show();
                            timer.cancel();
                            myDialog.cancel();
                            myDialog.dismiss();
                        }

                    }
                });
            }
        },1000,1000)
        ;




        TextView button = cont.findViewById(R.id.btn_nao_quero);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                myDialog.cancel();
                myDialog.dismiss();

            }
        });
        myDialog.show();


    }

    public interface Utils{
        abstract void onUserEarnedReward(RewardItem rewardItem);
        abstract void onAdLoad();
    }

}




