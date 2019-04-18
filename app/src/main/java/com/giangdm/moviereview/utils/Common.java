package com.giangdm.moviereview.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Common {
    private static final int THRESHOLD = 750;
    public static long timestamp = 0;

    public static boolean isOverThreashold(long now) {
        return now - timestamp > THRESHOLD;
    }

    /**
     * Check network
     *
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

    public static String getDataFromInter(String strURL) {
        String strResult = "";
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            url = new URL(strURL);
            //Ket noi den server dung HttpURLConnection
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setConnectTimeout(60000);
            //Doc du lieu ve
            InputStream in = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                strResult += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
