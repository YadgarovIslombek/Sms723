package com.aid.sms723.ui.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.aid.sms723.R;

public class NetworkChangeReciever extends BroadcastReceiver {
    AlertDialog dialog;
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (CheckInnet.isConnectedInternet(context)) {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            View layoutView = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
//            alertDialog.setView(layoutView);
//            AppCompatButton appCompatButton = layoutView.findViewById(R.id.Retry);
//
//            AlertDialog dialog = alertDialog.create();
//            dialog.show();
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.getWindow().setGravity(Gravity.CENTER);
//            appCompatButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    onReceive(context, intent);
//                }
//            });
//        }
        try {
            if(!isOnline(context)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                View layoutView = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, null);
                alertDialog.setView(layoutView);
                AppCompatButton appCompatButton = layoutView.findViewById(R.id.Retry);
                dialog = alertDialog.create();
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setGravity(Gravity.CENTER);
                appCompatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        onReceive(context, intent);
                    }
                });
            }
            else
                {
//                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
        public boolean isOnline(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
        }
}
