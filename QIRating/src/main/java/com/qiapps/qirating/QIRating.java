package com.qiapps.qirating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;

public class QIRating {

    public static final int COLOR_DEFAULT = 0;

    public static boolean show(final Activity context, int colorTheme, final String packageName){

        if(RatingManager.showRating(context)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = context.getLayoutInflater();
            View cont = inflater.inflate(R.layout.qi_dialog_avaliar, null);
            dialog.setView(cont);

            LottieAnimationView stars = cont.findViewById(R.id.rating);
            stars.playAnimation();

            TextView btnAvaliar = cont.findViewById(R.id.btn_avaliar);
            TextView btnAgoraNao = cont.findViewById(R.id.btn_agoranao);
            TextView btnNunca = cont.findViewById(R.id.btn_nunca);
            LinearLayout title = cont.findViewById(R.id.title_container);

            if(colorTheme != 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnAvaliar.getBackground().setTint(colorTheme);
                    btnAgoraNao.setTextColor(colorTheme);
                    btnNunca.setTextColor(colorTheme);
                    title.getBackground().setTint(colorTheme);

                }
            }

            final AlertDialog myDialog = dialog.create();

            btnAvaliar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = "https://play.google.com/store/apps/details?id=" + packageName;
                    Uri uri = Uri.parse(link);
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setData(uri);
                    context.startActivity(it);
                    myDialog.cancel();
                    myDialog.dismiss();
                    RatingManager.finalizeRating(context);
                }
            });
            btnAgoraNao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.cancel();
                    myDialog.dismiss();
                    RatingManager.disablePermissionShow(context);
                }
            });
            btnNunca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.cancel();
                    myDialog.dismiss();
                    RatingManager.finalizeRating(context);
                }
            });

            myDialog.show();
        }else{
            return false;
        }
        return true;
    }

    public interface ModalActions{
        public abstract void onClickAvaliar();
    }

    public static void addPositiveEvent(Context context){
        RatingManager.addPositiveEvent(context);
    }

}
