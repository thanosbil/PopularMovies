package com.example.thanos.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Thanos on 3/12/2018.
 */

public class NetworkUtilities {

    public enum Mode{ popular, top_rated }

    private static final String TMDB_API_URL = "http://api.themoviedb.org/3/movie";
    private static final String FOR_POPULAR = "popular";
    private static final String FOR_TOP_RATED = "top_rated";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "f1cef6cc743eeb511311fdb2985663f7";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";


    public static URL buildUrl(Mode mode){
        Uri buildUri = null;
        switch (mode) {
            case popular:
                buildUri = Uri.parse(TMDB_API_URL).buildUpon()
                        .appendPath(FOR_POPULAR)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                break;
            case top_rated:
                buildUri = Uri.parse(TMDB_API_URL).buildUpon()
                        .appendPath(FOR_TOP_RATED)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                break;
        }

        URL url = null;

        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildImageUrl(String imageRelativeUrl){
        Uri buildUri;

        buildUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(imageRelativeUrl)
                .build();

        URL url = null;

        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
