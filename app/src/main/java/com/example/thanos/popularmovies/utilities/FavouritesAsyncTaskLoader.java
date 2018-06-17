package com.example.thanos.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.example.thanos.popularmovies.data.FavouriteMoviesContract;

public class FavouritesAsyncTaskLoader extends AsyncTaskLoader<Cursor> {

    private Cursor taskData = null;

    public FavouritesAsyncTaskLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(taskData != null){
            deliverResult(taskData);
        }else {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(FavouriteMoviesContract.FavouriteMovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASED);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void deliverResult(Cursor data){
        super.deliverResult(data);
        taskData = data;
    }
}
