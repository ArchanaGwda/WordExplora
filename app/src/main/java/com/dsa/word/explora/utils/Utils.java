package com.dsa.word.explora.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    private static final String TAG = "Utils";


    /**
     * Checks for internet connection
     *
     * @param context Context of an application
     * @return true if internet is available else returns false
     */
    public static boolean isInternetAvailable(Context context) {
        try {
            if (isOnline(context)) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                final InetAddress address = InetAddress.getByName("www.google.com");
                return !address.equals("");
            }
        } catch (UnknownHostException e) {
            Log.e(TAG, "NETWORK ERROR: " + e);
        }
        return false;
    }

    /**
     * Checks for network connection
     *
     * @param context Context of an application
     * @return true if network connection available else return false
     */
    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
