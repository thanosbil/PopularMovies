package com.example.thanos.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.thanos.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavouriteMovieEntry implements BaseColumns{
        // table name
        public static final String TABLE_FAVOURITE_MOVIES = "movie";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_RELEASED = "released";
        public static final String COLUMN_AVG = "average";
        public static final String COLUMN_ADULT = "adult";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVOURITE_MOVIES)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                "/" + TABLE_FAVOURITE_MOVIES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +
                "/" + TABLE_FAVOURITE_MOVIES;

        public static Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
