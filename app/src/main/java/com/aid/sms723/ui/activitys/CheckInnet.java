package com.aid.sms723.ui.activitys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInnet {
    public static boolean isConnectedInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for (int i=0;i<info.length;i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED);
                    return true;
                }
            }
        }

        return false;
    }

}
