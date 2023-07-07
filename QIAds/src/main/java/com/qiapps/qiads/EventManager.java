package com.qiapps.qiads;

import android.content.Context;
import android.content.SharedPreferences;

public final class EventManager {

    private static final String SHARED_PREFERENCES_NAME = "EventManager";
    private static final String COUNT = "ContadorEventos";

    //adicionar um evento positivo
    public static void addPositiveEvent(Context context) {
        int i = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(COUNT, 0);
        i++;
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(COUNT, i);
        editor.apply();
    }

    public static boolean isEventCountEnable(Context context, int count){
        int i = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(COUNT, 0);
        if (i == 0) return false;
        if(i>=count) {
            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
            editor.putInt(COUNT, 0);
            editor.apply();
            return true;
        }

        return false;
    }




}
