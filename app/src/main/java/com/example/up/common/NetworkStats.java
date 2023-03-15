package com.example.up.common;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class NetworkStats {
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
