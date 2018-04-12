package com.example.thanos.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.MyAsyncTaskLoader;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String>{

    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter myAdapter;
    private ArrayList<Movie> movieData;
    private TextView noConnectionTextView;
    private LoaderManager myLoaderManager;
    private NetworkUtilities.Mode mode = NetworkUtilities.Mode.popular;
    private ProgressBar loadingIndicator;
    private String responseData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = findViewById(R.id.rv_movies);
        noConnectionTextView = findViewById(R.id.tv_no_connection);
        loadingIndicator = findViewById(R.id.loading_indicator);





        myLoaderManager = getLoaderManager();

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
    public Loader<String> onCreateLoader(int id, Bundle args) {
        loadingIndicator.setVisibility(View.VISIBLE);
        myRecyclerView.setVisibility(View.INVISIBLE);
        return new MyAsyncTaskLoader(this, mode, responseData);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        myRecyclerView.setVisibility(View.VISIBLE);
        movieData = new ArrayList<>();
        try{
            JSONObject jsonData = new JSONObject(data);
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

        myLayoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setHasFixedSize(true);
        myAdapter = new MoviesAdapter(this, movieData, this);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

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
            }
        }else
            noConnectionActions();


        return super.onOptionsItemSelected(item);
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
}
