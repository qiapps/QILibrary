package com.qiapps.qilibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.qiapps.qiads.QINativeAds;
import com.qiapps.qiads.QIUtils;

public class SecondActivity extends AppCompatActivity {

    private QINativeAds qiNativeAds;
    private ViewGroup vg_ads;
    private CustomApplication customApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        vg_ads = findViewById(R.id.vg_ads);
        customApplication = (CustomApplication)getApplication();

        qiNativeAds = new QINativeAds(this,vg_ads);
        qiNativeAds.setType(QIUtils.TYPE_SMALL2);
        qiNativeAds.buildAdView();
        customApplication.setContainerAd(qiNativeAds);
    }

    @Override
    protected void onDestroy() {
        customApplication.buildLoader();
        super.onDestroy();
    }
}