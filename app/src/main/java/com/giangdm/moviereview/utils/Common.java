package com.giangdm.moviereview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    private static final int THRESHOLD = 750;
    public static long timestamp = 0;

    public static boolean isOverThreashold(long now) {
        return now - timestamp > THRESHOLD;
    }

    /**
     * Check network
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();

        }

    }
}
