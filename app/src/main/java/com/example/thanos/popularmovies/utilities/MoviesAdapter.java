package com.example.thanos.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thanos.popularmovies.R;

/**
 * Created by Thanos on 3/14/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private MoviesAdapterOnClickHandler clickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onClick(View v, int position);
    }

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler clickHandler){
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        view.setFocusable(true);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
