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

    public static final String URL_LOAD_IMAGE = "https://image.tmdb.org/t/p/w500";

    public static final String URL_LOAD_MOVIE_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=";
}
