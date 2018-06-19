package com.example.thanos.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thanos.popularmovies.data.FavouriteMoviesContract;
import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.FavouritesAsyncTaskLoader;
import com.example.thanos.popularmovies.utilities.MyAsyncTaskLoader;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks{

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private MoviesAdapter myAdapter;
    private ArrayList<Movie> movieData;
    private TextView noConnectionTextView;
    private LoaderManager myLoaderManager;
    private NetworkUtilities.Mode mode;
    private ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = findViewById(R.id.rv_movies);
        noConnectionTextView = findViewById(R.id.tv_no_connection);
        loadingIndicator = findViewById(R.id.loading_indicator);
        movieData = new ArrayList<>();

        myLoaderManager = getLoaderManager();
        mode = NetworkUtilities.Mode.popular;

        if(NetworkUtilities.checkConnectivity(this)){
            myLoaderManager.initLoader(1, null, this);
            noConnectionTextView.setVisibility(View.INVISIBLE);
        }else {
            noConnectionTextView.setVisibility(View.VISIBLE);
            noConnectionTextView.setText(R.string.no_connection);
        }



    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("serializedData", movieData.get(position));
        startActivity(intent);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        loadingIndicator.setVisibility(View.VISIBLE);
        myRecyclerView.setVisibility(View.INVISIBLE);

        if(mode == NetworkUtilities.Mode.popular || mode == NetworkUtilities.Mode.top_rated){
            return new MyAsyncTaskLoader(this, mode);
        }else {
            return  new FavouritesAsyncTaskLoader(this);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        myRecyclerView.setVisibility(View.VISIBLE);
        movieData.clear();
        switch (mode){
            case popular:
            case top_rated:

                try{
                    String stringData = String.valueOf(data);
                    JSONObject jsonData = new JSONObject(stringData);
                    JSONArray jsonAllMovies = jsonData.getJSONArray("results");
                    for(int i=0; i<jsonAllMovies.length(); i++){
                        Movie movie = new Movie();
                        JSONObject jsonMovie = jsonAllMovies.getJSONObject(i);

                        movie.setMovieTitle(jsonMovie.getString("title"));
                        movie.setDescription(jsonMovie.getString("overview"));
                        movie.setImage(jsonMovie.getString("poster_path"));
                        movie.setAdult(jsonMovie.getBoolean("adult"));
                        movie.setLanguage(jsonMovie.getString("original_language"));
                        movie.setVoteAverage(jsonMovie.getDouble("vote_average"));
                        movie.setReleaseDate(jsonMovie.getString("release_date"));

                        movieData.add(movie);
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            case favourites:
                Cursor cursorData = (Cursor)data;
                fillMovieArrayList(cursorData);
                //myAdapter = new MoviesAdapter(this, cursorData, this, mode);
                //myAdapter.swapCursor(cursorData);
                break;

        }

        myAdapter = new MoviesAdapter(this, movieData, this, mode);
        myLayoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setHasFixedSize(true);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (NetworkUtilities.checkConnectivity(this)) {

            connectedActions();

            switch (item.getItemId()){
                case R.id.action_most_popular:
                    mode = NetworkUtilities.Mode.popular;
                    myLoaderManager.restartLoader(1, null, this);
                    return true;
                case R.id.action_top_rated:
                    mode = NetworkUtilities.Mode.top_rated;
                    myLoaderManager.restartLoader(1, null, this);
                    return true;
                case R.id.action_favourites:
                    mode = NetworkUtilities.Mode.favourites;
                    myLoaderManager.restartLoader(2, null, this);
                    return true;
            }
        }else
            noConnectionActions();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("enum_key", mode);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void connectedActions(){
        myRecyclerView.setVisibility(View.VISIBLE);
        noConnectionTextView.setVisibility(View.INVISIBLE);
    }

    private void noConnectionActions(){
        noConnectionTextView.setText(R.string.check_connection);
        noConnectionTextView.setVisibility(View.VISIBLE);
        myRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void fillMovieArrayList(Cursor c){
        Movie movie;
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            movie = new Movie();
            movie.setDescription(c.getString(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_DESCRIPTION)));
            movie.setMovieTitle(c.getString(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_TITLE)));
            movie.setImage(c.getString(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER)));
            movie.setReleaseDate(c.getString(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_RELEASED)));
            movie.setVoteAverage(c.getDouble(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_AVG)));
            movie.setLanguage(c.getString(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_LANGUAGE)));
            int adult = c.getInt(c.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_ADULT));

            if (adult == 0)
                movie.setAdult(false);
            else
                movie.setAdult(true);
            movieData.add(movie);
        }
    }
}
