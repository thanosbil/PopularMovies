package com.example.thanos.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;

    public FavouritesDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+
                FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES + "(" + FavouriteMoviesContract.FavouriteMovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASED + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_AVG + " REAL NOT NULL, " +
                FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_ADULT + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES);
        onCreate(db);
    }
}
