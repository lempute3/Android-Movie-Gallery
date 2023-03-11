package com.example.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmdb.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MovieModel> mMovieList;

    public MovieRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setMovieList(List<MovieModel> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder
    /* A holder class to hold recycler view item components. */
    {
        private ImageView mMoviePoster;
        private TextView mMovieTitle, mMovieRating, mMovieGenre;
        private RatingBar mMovieRatingBar;

        public MyViewHolder(final View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.card_view_movie_poster);
            mMovieTitle = itemView.findViewById(R.id.card_view_movie_title);
            mMovieRating = itemView.findViewById(R.id.card_view_movie_rating);
            mMovieRatingBar = itemView.findViewById(R.id.card_view_movie_rating_bar);
            mMovieGenre = itemView.findViewById(R.id.card_view_movie_genre);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    /* This method is used to inflate the view layout. */
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    /* This is the main method for assigning values to the recycler view layout components. */
    {
        String poster = mMovieList.get(position).getPosterPath();
        String title = mMovieList.get(position).getTitle();
        float rating = mMovieList.get(position).getVoteAverage();

        Picasso.get().load(poster).into(holder.mMoviePoster);
        holder.mMovieTitle.setText(title);
        holder.mMovieRating.setText(Float.toString(rating));
        holder.mMovieRatingBar.setRating(rating);
    }

    @Override
    public int getItemCount()
    /* Giving the recycler view number of how much items to display. */
    {
        if (mMovieList != null) return mMovieList.size();
        return 0;
    }

}
