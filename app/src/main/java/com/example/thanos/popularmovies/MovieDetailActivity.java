package com.example.thanos.popularmovies;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.DbUtilities;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView movieTitle;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieIsAdult;
    private TextView movieReleaseDate;
    private TextView movieLanguage;
    private ImageView moviePoster;
    private Button addToFavourites;
    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = new Movie();

        movieSynopsis = findViewById(R.id.tv_synopsis);
        movieRating = findViewById(R.id.tv_vote_average);
        movieIsAdult = findViewById(R.id.tv_adult);
        movieReleaseDate = findViewById(R.id.tv_release_date);
        movieLanguage = findViewById(R.id.tv_language);
        moviePoster = findViewById(R.id.iv_movie_poster);
        addToFavourites = findViewById(R.id.btn_add_to_favourites);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            movie = (Movie) getIntent().getSerializableExtra("serializedData");

            if(movie != null){
                populateUI(movie);
                addToFavourites.setEnabled(true);
            }else{
                closeOnError();
            }

        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Movie data not available", Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie){

        setTitle(movie.getMovieTitle());

        movieSynopsis.setText(movie.getDescription());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieIsAdult.setText(String.valueOf(movie.isAdult()));
        movieReleaseDate.setText(movie.getReleaseDate());
        movieLanguage.setText(movie.getLanguage());

       Picasso.with(this).load(String.valueOf(NetworkUtilities.buildImageUrl(movie.getImage(),
               NetworkUtilities.ImageMode.detail))).into(moviePoster);
    }

    public void onAddToFavouritesClick(View v){
        DbUtilities.insertFavouriteMovie(movie, this, moviePoster.getDrawable());
    }





}
