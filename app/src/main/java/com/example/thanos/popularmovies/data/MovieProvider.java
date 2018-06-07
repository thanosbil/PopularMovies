package com.example.thanos.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    FavouritesDBHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 200;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES, MOVIE);
        matcher.addURI(authority, FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new FavouritesDBHelper(context);

        return true;
    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor returnCursor;

        switch (sUriMatcher.match(uri)){

            case MOVIE:
                returnCursor = dbHelper.getReadableDatabase().query(FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_WITH_ID:
                returnCursor = dbHelper.getReadableDatabase().query(FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES,
                        projection,
                        FavouriteMoviesContract.FavouriteMovieEntry._ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String retType;

        switch (sUriMatcher.match(uri)){
            case MOVIE:
                retType = FavouriteMoviesContract.FavouriteMovieEntry.CONTENT_DIR_TYPE;
                break;

            case MOVIE_WITH_ID:
                retType = FavouriteMoviesContract.FavouriteMovieEntry.CONTENT_ITEM_TYPE;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return retType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MOVIE:
                long _id = db.insert(FavouriteMoviesContract.FavouriteMovieEntry.TABLE_FAVOURITE_MOVIES, null, values);
                if(_id > 0){
                    returnUri  = FavouriteMoviesContract.FavouriteMovieEntry.buildFavouritesUri(_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
