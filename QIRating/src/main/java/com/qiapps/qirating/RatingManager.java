package com.qiapps.qirating;

import android.content.Context;
import android.content.SharedPreferences;

class RatingManager {


    public static final int OK = 1;
    public static final int NUNCA = 2;
    private static final String SHARED_PREFERENCES_NAME = "RatingManagerQILIB";
    private static final String COUNT = "RatingManagerCount";
    private static final String PERMISSION = "RatingManagerPermission";
    private static final String STATUS = "RatingManagerStatus";
    private static final String COUNT_EVENTS = "RatingManagerCountEvents";
    private static final int COUNT_EVENTS_ENABLED = 3;

    /** Passos para utilizar a classe RatingManager
     *
     *
     * 1 - Utilize o método addPositiveEvent para adicionar a contagem de eventos considerados relevantes para o app.
     *
     * 2 - Chame a função showRating, se retornar true pode mostrar a avaliação, caso contrario nao mostra
     *
     * 3 - Se o usuário clicar em Avaliar ou em Nunca, chame a função finalizeRating
     *
     * 4 - Se o usuário clicar em agora não, chame a função disablePermissionShow para reiniciar o processo.
     * */

    public static void addPositiveEvent(Context context){

        if(!isFinalizeRating(context)) {
            int i = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(COUNT, 0);
            int count = getCountEvents(context);
            if (i == count-1) {
                //terceiro evento positivo -> permitir mostrar
                enablePermissionShow(context);
            } else {
                i++;
                SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                editor.putInt(COUNT, i);
                editor.apply();
            }
        }
    }

    public static void enablePermissionShow(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PERMISSION,true);
        editor.apply();
    }

    public static void disablePermissionShow(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PERMISSION,false);
        editor.putInt(COUNT,0);//reinicia a contagem
        editor.apply();

        //adiciona 2 a contagem necessária para mostrar avaliação denovo.
        int count = getCountEvents(context) +2;
        setCountEvents(context,count);
    }

    public static void finalizeRating(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(STATUS,NUNCA);
        editor.apply();
    }

    public static boolean isFinalizeRating(Context context){//verifica se o processo de avaliação foi encerrado pelo usuário
        int i = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE).getInt(STATUS,OK);
        return i != OK;
    }

    public static boolean showRating(Context context){
        if(!isFinalizeRating(context) && context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE).getBoolean(PERMISSION,false)){
            return true;
        }else{
            return false;
        }
    }

    public static void setCountEvents(Context context,int count){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(COUNT_EVENTS,count);
        editor.apply();
    }

    public static int getCountEvents(Context context){
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE).getInt(COUNT_EVENTS,COUNT_EVENTS_ENABLED);
    }

}
