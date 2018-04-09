package com.example.thanos.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.ProgressBar;

import java.net.URL;

public class MyAsyncTaskLoader extends AsyncTaskLoader<String> {

    NetworkUtilities.Mode mode;
    String moviesData;

    public MyAsyncTaskLoader(Context context, NetworkUtilities.Mode mode, String moviesData) {
        super(context);
        this.mode = mode;
        this.moviesData = moviesData;
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
            String jsonTMDBresponse = NetworkUtilities.getResponseFromHttpUrl(moviesRequest);
            return jsonTMDBresponse;
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
