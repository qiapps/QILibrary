package com.qiapps.qilibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.qiapps.qiads.QIBottomDrawerAds;
import com.qiapps.qiads.QILoaderNativeAds;
import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QIReward;
import com.qiapps.qiads.QIUtils;
import com.qiapps.qiads.QInterstitial;
import com.qiapps.qirating.QIRating;

public class MainActivity extends AppCompatActivity {

    private QIReward qiReward;
    private ViewGroup vg_ads;
    private QINativeAds qiNativeAds;
    private QInterstitial qInterstitial;
    private QIBottomDrawerAds qiBottomDrawerAds;
    private ViewGroup drawerContainer;
    private Button button;
    private TextView txt;
    private CustomApplication customApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vg_ads = findViewById(R.id.container_ads);
        drawerContainer = findViewById(R.id.bottom_container);
        button = findViewById(R.id.button);
        txt = findViewById(R.id.txt);


        customApplication = (CustomApplication) getApplication();



        //exampleInterstitial();
        //exampleNativeDefault();
        //exampleBottomDrawer();
        //buildWithNativeAdLoader();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customApplication.showAppOpenAds();
            }
        });

    }

    private void exampleBottomDrawer(){
        qiBottomDrawerAds = new QIBottomDrawerAds(this, QINativeAds.TEST_AD_UNIT, drawerContainer, new QIBottomDrawerAds.BottomDrawerUtils() {
            @Override
            public void onShowDrawer() {

            }

            @Override
            public void onHideDrawer() {

            }

            @Override
            public void onClosePress() {
                qiBottomDrawerAds.hideDrawer();
            }

            @Override
            public void onFailedToShow() {
                qiBottomDrawerAds.hideDrawer();
            }
        });

        qiBottomDrawerAds.build();
        QINativeAds qin = qiBottomDrawerAds.getAds();
        qin.setAdAtribuitionBackgroundColor(Color.BLUE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qiBottomDrawerAds.show();
            }
        });
    }

    private void buildWithNativeAdLoader(){
        qiNativeAds = new QINativeAds(this,vg_ads);
        qiNativeAds.setType(QIUtils.TYPE_SMALL2);
        qiNativeAds.buildAdView();
        customApplication.setContainerAd(qiNativeAds);
        /*if(customApplication.failedLoadContent){
            qiNativeAds.showQIAppsAds();
        }else{
            if(customApplication.content != null){
                qiNativeAds.setUnifiedNativeAd(customApplication.content);
                qiNativeAds.show();
            }else{
                customApplication.contentListener = new CustomApplication.LoadListener() {
                    @Override
                    public void onAdLoad() {
                        qiNativeAds.setUnifiedNativeAd(customApplication.content);
                        qiNativeAds.show();
                    }

                    @Override
                    public void onLoadFailed() {
                        qiNativeAds.showQIAppsAds();
                    }
                };
            }
        }*/
    }

    private void exampleInterstitial(){
        qInterstitial = new QInterstitial(this, QInterstitial.TEST_AD_UNIT, new QInterstitial.AdsUtils() {
            @Override
            public void onLoad() {
                qInterstitial.show();
            }

            @Override
            public void onClosed() {

            }
        });
        qInterstitial.build();
    }

    private void exampleNativeDefault(){
        qiNativeAds = new QINativeAds(this,vg_ads,QINativeAds.TEST_AD_UNIT);
        //qiNativeAds.setShowWhenFinishLoad(false);//true default;
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //Drawable drawable = getDrawable(R.drawable.botao_gradiente2);
            //qiNativeAds.setButtonResource(drawable);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int color = getColor(R.color.colorAccent);
                qiNativeAds.setAdAtribuitionBackgroundColor(color);
            }

            qiNativeAds.setTitleColor(Color.WHITE);
            qiNativeAds.setDescriptionColor(Color.WHITE);
        }*/
        //disabled apps to show ads
        //qiNativeAds.addBlockedQIAppsAds(QIUtils.INSTADOWNLOADER);
        //qiNativeAds.addBlockedQIAppsAds(QIUtils.STATUS_SAVER);
        Typeface tf = Typeface.createFromAsset(getAssets(),"Montserrat_bold.ttf");
        qiNativeAds.setButtonTypeFace(tf);
        qiNativeAds.setTitleTypeFace(tf);
        qiNativeAds.setShowWhenFinishLoad(false);
        qiNativeAds.setType(QIUtils.TYPE_SMALL2);
        Drawable btn = ContextCompat.getDrawable(this,R.drawable.botao_slim);
        qiNativeAds.setButtonResource(btn);
        int color = ContextCompat.getColor(this,R.color.colorPrimary);
        qiNativeAds.setCallToActionColor(color);
        qiNativeAds.build();
        qiNativeAds.show();
    }

    private void exampleVideoReward(){
            qiReward = new QIReward(this, QIReward.TEST_AD_UNIT, true, new QIReward.RewardUtils() {
                @Override
                public void onLoad() {

                }

                @Override
                public void onLoadFailed() {

                }

                @Override
                public void onRewarded(RewardItem rewardItem) {

                }

                @Override
                public void onVideoOpen() {

                }

                @Override
                public void onVideoCloded() {

                }

                @Override
                public void onVideoFailedToShow() {

                }
        });

        qiReward.show();
    }

    @Override
    public void onBackPressed() {
        //qiBottomDrawerAds.show();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        Log.d("AppOpen","onStop");
        customApplication.cleanAppOpenAds();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customApplication.reactivateAppOpenAds();
    }
}