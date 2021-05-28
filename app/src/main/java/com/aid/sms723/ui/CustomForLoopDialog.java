package com.aid.sms723.ui;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aid.sms723.R;

public class CustomForLoopDialog extends AppCompatActivity {
    private AppCompatActivity activity;
    private AlertDialog alertDialog;

    public CustomForLoopDialog(AppCompatActivity myActivity){
        activity =myActivity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.custom_forloopdialog,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

    }
    public  void dismissDialog(){
        alertDialog.dismiss();
    }
}
