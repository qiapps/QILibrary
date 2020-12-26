package com.qiapps.qiads;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

class QIResourceAds{
    public String title;
    public String description;
    public String link;
    public int icon;
    public int banner;
    private ArrayList<String> blockedApps;
    private Context context;

    public QIResourceAds(Context context, ArrayList<String> blockedApps){
        this.context = context;
        this.blockedApps = blockedApps;
        generate();
    }

    private void generate(){
        Random r = new Random();
        int i = r.nextInt(100);
        int nrApps = 5;
        i = i%nrApps;
        buildResourceByInt(i);
    }

    public void buildResourceByInt(int i){
        switch (i){
            case 0:
                if(isBlocked(QIUtils.INSTADOWNLOADER)){
                    generate();
                }else{
                    this.title = context.getString(R.string.instadownloader_title);
                    this.description = context.getString(R.string.instadownloader_description);
                    this.icon = R.drawable.icon_instadownloader;
                    this.banner = R.drawable.banner_instadownloader;
                    this.link = "https://play.google.com/store/apps/details?id=" + "br.com.qiapps.baixarvideos";
                }
                break;
            case 1:
                if(isBlocked(QIUtils.STATUS_SAVER)){
                    generate();
                }else{
                    this.title = context.getString(R.string.statussaver_title);
                    this.description = context.getString(R.string.statussaver_description);
                    this.icon = R.drawable.icon_status_saver;
                    this.banner = R.drawable.banner_status_saver;
                    this.link = "https://play.google.com/store/apps/details?id=" + "br.com.qiapps.qistatussaver";
                }
                break;
            case 2:
                if(isBlocked(QIUtils.MINI_HABITOS)){
                    generate();
                }else{
                    this.title = context.getString(R.string.minihabitos_title);
                    this.description = context.getString(R.string.minihabitos_description);
                    this.icon = R.drawable.icon_mini_habitos;
                    this.banner = R.drawable.banner_mini_habitos;
                    this.link = "https://play.google.com/store/apps/details?id=" + "com.qiapps.minihabitos";
                }

                break;
            case 3:
                if(isBlocked(QIUtils.QINVEST)){
                    generate();
                }else{
                    this.title = context.getString(R.string.qinvest_title);
                    this.description = context.getString(R.string.qinvest_description);
                    this.icon = R.drawable.icon_qinvest;
                    this.banner = R.drawable.banner_qinvest;
                    this.link = "https://play.google.com/store/apps/details?id=" + "com.apps.murilex.calculadoradeinvestimentos";
                }

                break;
            case 4:
                if(isBlocked(QIUtils.QIMETAS)){
                    generate();
                }else{
                    this.title = context.getString(R.string.qimetas_title);
                    this.description = context.getString(R.string.qimetas_description);
                    this.icon = R.drawable.icon_qimetas;
                    this.banner = R.drawable.banner_qimetas;
                    this.link = "https://play.google.com/store/apps/details?id=" + "com.qiapps.qimetas";
                }

                break;
        }
    }

    private boolean isBlocked(String appName){
        for (String s:blockedApps) {
            if(s.equals(appName)){
                return true;
            }
        }

        return false;
    }
}
