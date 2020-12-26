package com.qiapps.qiads;

import android.content.Context;

class ResourceUtils {

    public static int getColor(Context context, int colorResource){
        int col = 0;
        col = context.getResources().getColor(colorResource);
        return col;
    }
}
