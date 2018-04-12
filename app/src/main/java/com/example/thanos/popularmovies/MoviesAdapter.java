package com.example.thanos.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Thanos on 3/14/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyMovieViewHolder> {

    private ArrayList<Movie> data;
    private MoviesAdapterOnClickHandler mClickHandler;
    private Context context;

    public MoviesAdapter(Context context, ArrayList<Movie> myData, MoviesAdapterOnClickHandler onClickHandler){
        this.context = context;
        this.data = myData;
        this.mClickHandler = onClickHandler;
    }




    public interface MoviesAdapterOnClickHandler{
         void onClick(View v, int position);
    }



    @Override
    public MoviesAdapter.MyMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new MyMovieViewHolder(itemView, mClickHandler);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MyMovieViewHolder holder, int position) {
        Movie movie = data.get(position);
        URL moviePosterUrl = NetworkUtilities.buildImageUrl(movie.getImage(), NetworkUtilities.ImageMode.rv_item);
        String posterPath = moviePosterUrl.toString();
        Picasso.with(context).load(posterPath).into(holder.moviePoster);
        holder.movieTitle.setText(movie.getMovieTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MoviesAdapterOnClickHandler myClickHandler;
        public ImageView moviePoster;
        public TextView movieTitle;

        public MyMovieViewHolder(View itemView, MoviesAdapterOnClickHandler clickHandler) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
            myClickHandler = clickHandler;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickHandler.onClick(v, getAdapterPosition());
        }
    }

}
