package com.example.thanos.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanos.popularmovies.data.FavouriteMoviesContract;
import com.example.thanos.popularmovies.model.Movie;
import com.example.thanos.popularmovies.utilities.DbUtilities;
import com.example.thanos.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Thanos on 3/14/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyMovieViewHolder> {

    private ArrayList<Movie> data;
    private MoviesAdapterOnClickHandler mClickHandler;
    private Context context;
    private Cursor cursor;
    private  NetworkUtilities.Mode adapterMode;

    public MoviesAdapter(Context context, ArrayList<Movie> myData, MoviesAdapterOnClickHandler onClickHandler, NetworkUtilities.Mode mode){
        this.context = context;
        this.data = myData;
        this.mClickHandler = onClickHandler;
        this.adapterMode = mode;
    }

    public MoviesAdapter(Context context, Cursor cursor, MoviesAdapterOnClickHandler onClickHandler,NetworkUtilities.Mode mode){
        this.context = context;
        this.cursor = cursor;
        this.adapterMode = mode;
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
        String posterPath;
        switch (adapterMode){
            case popular:
            case top_rated:
                Movie movie = data.get(position);
                URL moviePosterUrl = NetworkUtilities.buildImageUrl(movie.getImage(), NetworkUtilities.ImageMode.rv_item);
                posterPath = moviePosterUrl.toString();
                Picasso.with(context).load(posterPath).into(holder.moviePoster);
                holder.movieTitle.setText(movie.getMovieTitle());
                break;
            case favourites:
           /*     if(!cursor.move(position)){
                    return;
                }
*/
                cursor.moveToPosition(position);
                String title = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_TITLE));
                posterPath = cursor.getString(cursor.getColumnIndex(FavouriteMoviesContract.FavouriteMovieEntry.COLUMN_POSTER));
                posterPath += "/" + title;
                holder.movieTitle.setText(title);
                Picasso.with(context).load(new File(posterPath)).into(holder.moviePoster);
                break;
        }

    }


    public Cursor swapCursor(Cursor newCursor) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == newCursor) {
            return null; // bc nothing has changed
        }
        Cursor temp = cursor;
        this.cursor = newCursor; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    @Override
    public int getItemCount() {
        if(adapterMode == NetworkUtilities.Mode.popular || adapterMode == NetworkUtilities.Mode.top_rated)
            return data.size();
        else
            return cursor.getCount();
    }

    public class MyMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MoviesAdapterOnClickHandler myClickHandler;
        private ImageView moviePoster;
        private TextView movieTitle;

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
