package com.example.thanos.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.example.thanos.popularmovies.data.FavouriteMoviesContract;
import com.example.thanos.popularmovies.model.Movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DbUtilities {

    public static void insertFavouriteMovie(Movie movie, Context context, Drawable drawable){

        movie.setImage(saveToInternalStorage(drawable, movie.getMovieTitle(), context));

        ContentValues cv = new ContentValues();
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_TITLE, movie.getMovieTitle());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER, movie.getImage());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_DESCRIPTION, movie.getDescription());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_LANGUAGE, movie.getLanguage());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASED, movie.getReleaseDate());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_AVG, movie.getVoteAverage());
        cv.put(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_ADULT, movie.isAdult());

        Uri uri = context.getContentResolver().insert(FavouriteMoviesContract.FavouriteMovieEntry.CONTENT_URI, cv);

        if(uri != null){
            Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private static String saveToInternalStorage(Drawable drawable, String posterFileName, Context context){

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap posterBitmap = bitmapDrawable.getBitmap();

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("moviePosters", Context.MODE_PRIVATE);
        File path = new File(directory, posterFileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            posterBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public static Bitmap loadImageFromStorage(String path, String posterFileName)
    {
        Bitmap b = null;

        try {
            File f=new File(path, posterFileName);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;
    }
}
