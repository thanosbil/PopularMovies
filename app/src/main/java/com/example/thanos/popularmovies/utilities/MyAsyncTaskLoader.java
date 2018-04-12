package com.example.thanos.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;

public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {

    private NetworkUtilities.Mode mode;
    private String moviesData;

    public MyAsyncTaskLoader(Context context, NetworkUtilities.Mode mode) {
        super(context);
        this.mode = mode;
    }


    @Override
    public void onStartLoading(){

        if(moviesData != null){
            deliverResult(moviesData);
        }else{
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {

        URL moviesRequest = NetworkUtilities.buildUrl(mode);
        try{
            return NetworkUtilities.getResponseFromHttpUrl(moviesRequest);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public  void deliverResult(String data){
        super.deliverResult(data);
        moviesData = data;
    }

}
