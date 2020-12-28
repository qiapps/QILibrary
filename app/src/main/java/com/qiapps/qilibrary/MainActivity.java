package com.qiapps.qilibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.qiapps.qiads.QIBottomDrawerAds;
import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QIReward;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vg_ads = findViewById(R.id.container_ads);
        drawerContainer = findViewById(R.id.bottom_container);
        button = findViewById(R.id.button);
        txt = findViewById(R.id.txt);

        MobileAds.initialize(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = getResources().getColor(R.color.colorAccent);
                boolean show = QIRating.show(MainActivity.this,QIRating.COLOR_DEFAULT,"com.qiapps.minihabitos");
                if(!show){
                    QIRating.addPositiveEvent(MainActivity.this);
                }
            }
        });

        //exampleInterstitial();
        exampleNativeDefault();
        exampleBottomDrawer();
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
        qiNativeAds.setSmallType();
        qiNativeAds.build();
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
        qiBottomDrawerAds.show();
    }
}