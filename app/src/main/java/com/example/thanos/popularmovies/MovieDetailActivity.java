package com.example.thanos.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView movieTitle;
    private TextView movieSynopsis;
    private TextView movieRating;
    private TextView movieIsAdult;
    private TextView movieReleaseDate;
    private TextView movieLanguage;
    private ImageView moviePoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieTitle = findViewById(R.id.tv_movie_title);
        movieSynopsis = findViewById(R.id.tv_synopsis);
        movieRating = findViewById(R.id.tv_vote_average);
        movieIsAdult = findViewById(R.id.tv_adult);
        movieReleaseDate = findViewById(R.id.tv_release_date);
        movieLanguage = findViewById(R.id.tv_language);
        moviePoster = findViewById(R.id.iv_movie_poster);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            Movie movie = (Movie) getIntent().getSerializableExtra("serializedData");

            if(movie != null){
                populateUI(movie);
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

        movieTitle.setText(movie.getMovieTitle());
        movieSynopsis.setText(movie.getDescription());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieIsAdult.setText(String.valueOf(movie.isAdult()));
        movieReleaseDate.setText(movie.getReleaseDate());
        movieLanguage.setText(movie.getLanguage());

       Picasso.with(this).load(String.valueOf(NetworkUtilities.buildImageUrl(movie.getImage(),
               NetworkUtilities.ImageMode.detail))).into(moviePoster);
    }
}
