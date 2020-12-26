package com.qiapps.qiads;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class QIBottomDrawerAds {

    private View view;
    private ViewGroup vg_drawer;//container contendo o anuncio e o botao de sair
    private ViewGroup vg_drawer_container;//container contendo o bloco com o anuncio, botao de sair e o fundo preto opaco.
    private ViewGroup container_ads;//container que contem o anúncio
    private ViewGroup container;//container onde o anuncio vai ser adicionado no app.
    private QINativeAds qiNativeAds;
    private TextView sair;
    private Activity context;
    private String adUnit;
    private boolean isShowDrawer = false;
    private float dp;
    private BottomDrawerUtils customBottomDrawerUtils;

    public QIBottomDrawerAds(Activity context, String adUnit,ViewGroup container, BottomDrawerUtils customBottomDrawerUtils) {
        this.context = context;
        this.adUnit = adUnit;
        this.container = container;
        this.customBottomDrawerUtils = customBottomDrawerUtils;
    }

    public void build(){
        view = context.getLayoutInflater()
                .inflate(R.layout.qi_bottom_drawer, null);

        vg_drawer = view.findViewById(R.id.vg_drawer);
        vg_drawer_container = view.findViewById(R.id.vg_container_drawer);
        container_ads = view.findViewById(R.id.container_ads_drawer);
        sair = view.findViewById(R.id.txt_sair_drawer);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customBottomDrawerUtils.onClosePress();
            }
        });
        vg_drawer_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDrawer();
            }
        });

        qiNativeAds = new QINativeAds(context,container_ads,adUnit);
        qiNativeAds.setShowWhenFinishLoad(false);
        qiNativeAds.build();

        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dp = metrics.density;

        vg_drawer_container.setVisibility(View.GONE);
        container.addView(view);

    }

    public void hideDrawer(){
        customBottomDrawerUtils.onHideDrawer();
        isShowDrawer = false;
        ObjectAnimator animation = ObjectAnimator.ofFloat(vg_drawer, "translationY", dp*400f);
        animation.setDuration(200);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                vg_drawer_container.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation.start();
    }

    public void show(){
            if (!qiNativeAds.show() || isShowDrawer) {
                customBottomDrawerUtils.onFailedToShow();
            } else {
                customBottomDrawerUtils.onShowDrawer();
                isShowDrawer = true;
                view.setVisibility(View.VISIBLE);
                vg_drawer.setTranslationY(dp * 400f);
                ObjectAnimator animation = ObjectAnimator.ofFloat(vg_drawer, "translationY", 0f);
                animation.setDuration(200);
                animation.start();
            }
    }

    public QINativeAds getAds(){
        return qiNativeAds;
    }

    public void destroy(){
        if(qiNativeAds != null) {
            qiNativeAds.destroy();
        }
    }

    public interface BottomDrawerUtils{
        void onShowDrawer();
        void onHideDrawer();
        void onClosePress();
        void onFailedToShow();
    }


}
