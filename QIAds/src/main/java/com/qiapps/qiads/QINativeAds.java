package com.qiapps.qiads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import java.util.ArrayList;

public class QINativeAds {

    private UnifiedNativeAd mUnifiedNativeAd;
    private UnifiedNativeAdView mAdView;
    private ViewGroup container_native_ads;
    private Activity context;
    private String adUnit;
    private boolean showWhenFinishLoad = true;
    private CustomNativeAdsUtils customNativeAdsUtils;
    public static final String TEST_AD_UNIT = "/6499/example/native";
    private int titleTextColor = 0;
    private int descriptionTextColor = 0;
    private Drawable buttonResource;
    private int adAtribuitionBackgroundColor = 0;
    private ArrayList<String> disabledQIApps = new ArrayList<>();
    private int typeAds = QIUtils.TYPE_DEFAULT;
    private Typeface buttonTypeFace;
    private Typeface titleTypeFace;
    private boolean inLoad = false;


    public QINativeAds(Activity context, ViewGroup container_native_ads, String adUnit) {
        this.container_native_ads = container_native_ads;
        this.context = context;
        this.adUnit = adUnit;
        initResources();
    }

    public QINativeAds(Activity context, ViewGroup container_native_ads, String adUnit,CustomNativeAdsUtils customNativeAdsUtils) {
        this.container_native_ads = container_native_ads;
        this.context = context;
        this.adUnit = adUnit;
        this.customNativeAdsUtils = customNativeAdsUtils;

        initResources();
    }

    private void initResources(){
        this.titleTextColor = ResourceUtils.getColor(context,R.color.primaryText);
        this.descriptionTextColor = ResourceUtils.getColor(context,R.color.secondaryText);
        this.adAtribuitionBackgroundColor = ResourceUtils.getColor(context,R.color.yellow);

        Drawable d = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            d = ContextCompat.getDrawable(context,R.drawable.botao_gradiente);
        }
        this.buttonResource = d;
    }

    public void setShowWhenFinishLoad(boolean showWhenFinishLoad) {
        this.showWhenFinishLoad = showWhenFinishLoad;
    }

    private void buildAdView(){
        UnifiedNativeAdView adView;
        if(typeAds == QIUtils.TYPE_SMALL){
            adView = (UnifiedNativeAdView) context.getLayoutInflater()
                    .inflate(R.layout.qi_native_ads_small, null);
        }else if(typeAds == QIUtils.TYPE_SMALL2){
            adView = (UnifiedNativeAdView) context.getLayoutInflater()
                    .inflate(R.layout.qi_native_ads_small2, null);
        }else{
            adView = (UnifiedNativeAdView) context.getLayoutInflater()
                    .inflate(R.layout.qi_native_ads_default, null);
        }

        this.mAdView = adView;
    }

    public void build() {
        buildAdView();
        inLoad = true;
        QILoaderNativeAds qiLoaderNativeAds = new QILoaderNativeAds(context, adUnit, new QILoaderNativeAds.LoaderUtils() {
            @Override
            public void onLoad(UnifiedNativeAd unifiedNativeAd) {
                inLoad = false;
                mUnifiedNativeAd = unifiedNativeAd;
                if(showWhenFinishLoad){
                    show();
                }
            }

            @Override
            public void onLoadFailed() {
                inLoad = false;
                if(showWhenFinishLoad){
                    showQIAppsAds();
                }
            }
        });

        qiLoaderNativeAds.load();
    }

    public void setButtonResource(Drawable drawable){
        this.buttonResource = drawable;
    }

    public void setAdAtribuitionBackgroundColor(int color){
        this.adAtribuitionBackgroundColor = color;
    }

    public void setTitleColor(int color){
        this.titleTextColor = color;
    }

    public void setDescriptionColor(int color){
        this.descriptionTextColor = color;
    }

    public void addBlockedQIAppsAds(String blockedAppAds){
        disabledQIApps.add(blockedAppAds);
    }

    public void setSmallType(){
        typeAds = QIUtils.TYPE_SMALL;
    }

    public void setType(int type){
        typeAds = type;
    }

    public void setButtonTypeFace(Typeface buttonTypeFace) {
        this.buttonTypeFace = buttonTypeFace;
    }

    public void setTitleTypeFace(Typeface titleTypeFace) {
        this.titleTypeFace = titleTypeFace;
    }

    public boolean show(){
        if(inLoad){
            showWhenFinishLoad = true;
            return false;
        }else{
            if(mAdView != null && mUnifiedNativeAd != null) {
                try {
                    String headLine = mUnifiedNativeAd.getHeadline();
                    String sub = mUnifiedNativeAd.getBody();
                    String callToAction = mUnifiedNativeAd.getCallToAction();

                    if(typeAds == QIUtils.TYPE_DEFAULT){
                        MediaView mdv = mAdView.findViewById(R.id.mediaview);
                        mAdView.setMediaView(mdv);
                    }

                    ImageView img = mAdView.findViewById(R.id.img_ads);
                    img.setImageDrawable(mUnifiedNativeAd.getIcon().getDrawable());
                    mAdView.setIconView(img);

                    TextView title = mAdView.findViewById(R.id.title_ads);
                    title.setText(headLine);
                    title.setTextColor(titleTextColor);
                    mAdView.setHeadlineView(title);

                    TextView subtitle = mAdView.findViewById(R.id.subtitle_ads);
                    subtitle.setText(sub);
                    subtitle.setTextColor(descriptionTextColor);
                    mAdView.setBodyView(subtitle);

                    Button btn = mAdView.findViewById(R.id.btn_ads);
                    btn.setText(callToAction);
                    if(buttonResource != null){
                        btn.setBackground(buttonResource);
                    }
                    if(buttonTypeFace != null){
                        btn.setTypeface(buttonTypeFace);
                    }
                    if(titleTypeFace != null){
                        title.setTypeface(titleTypeFace);
                    }
                    mAdView.setCallToActionView(btn);
                    //ad atribuition
                    TextView adAtr = mAdView.findViewById(R.id.ad_atribuition);
                    adAtr.setBackgroundColor(adAtribuitionBackgroundColor);

                    mAdView.setNativeAd(mUnifiedNativeAd);

                    container_native_ads.removeAllViews();
                    container_native_ads.addView(mAdView);
                    container_native_ads.setVisibility(View.VISIBLE);
                    if(customNativeAdsUtils != null){
                        customNativeAdsUtils.onLoad();
                    }
                    return true;
                }catch (Exception e){
                    showQIAppsAds();
                    return true;
                }
            }else{
                showQIAppsAds();
                return true;
            }
        }

    }

    private void showQIAppsAds(){

        QIResourceAds resourceAds = new QIResourceAds(context,disabledQIApps);
        int icon = resourceAds.icon;
        int banner = resourceAds.banner;
        String tt = resourceAds.title;
        String description = resourceAds.description;
        final String link = resourceAds.link;

        View view;
        if(typeAds == QIUtils.TYPE_SMALL){
            view = context.getLayoutInflater().inflate(R.layout.qiapps_native_ads_small,null);
        }else if(typeAds == QIUtils.TYPE_SMALL2){
            view = context.getLayoutInflater().inflate(R.layout.qiapps_native_ads_small2,null);
        }else{
            view = context.getLayoutInflater().inflate(R.layout.qiapps_native_ads_default,null);
        }

        ImageView img = view.findViewById(R.id.img_ads);
        TextView title = view.findViewById(R.id.title_ads);
        TextView subtitle = view.findViewById(R.id.subtitle_ads);
        Button btn = view.findViewById(R.id.btn_ads);
        TextView adAtr = view.findViewById(R.id.ad_atribuition);
        if(typeAds == QIUtils.TYPE_DEFAULT){
            ImageView mdv = view.findViewById(R.id.imageView);
            mdv.setImageResource(banner);
        }

        img.setImageResource(icon);
        title.setText(tt);
        subtitle.setText(description);
        btn.setText(R.string.download);

        //config resources
        title.setTextColor(titleTextColor);
        subtitle.setTextColor(descriptionTextColor);
        adAtr.setBackgroundColor(adAtribuitionBackgroundColor);
        if(buttonResource != null){
            btn.setBackground(buttonResource);
        }
        if(buttonTypeFace != null){
            btn.setTypeface(buttonTypeFace);
        }
        if(titleTypeFace != null){
            title.setTypeface(titleTypeFace);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setData(uri);
                context.startActivity(it);
            }
        });

        container_native_ads.removeAllViews();
        container_native_ads.addView(view);
        container_native_ads.setVisibility(View.VISIBLE);
        if(customNativeAdsUtils != null){
            customNativeAdsUtils.onLoad();
        }
    }

    public void destroy(){
        if(mAdView != null){
            mAdView.destroy();
        }
    }



    public interface CustomNativeAdsUtils{
        abstract void onLoad();
    }

}
