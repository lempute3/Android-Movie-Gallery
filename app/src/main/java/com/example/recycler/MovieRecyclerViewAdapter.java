package com.example.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.tmdb.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface mRecyclerViewInterface;

    private Context mContext;
    private List<MovieModel> mMovieList;

    public MovieRecyclerViewAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        mRecyclerViewInterface = recyclerViewInterface;
    }

    public void setMovieList(List<MovieModel> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position) {
        if (mMovieList == null || mMovieList.size() <= 0)
            return null;

        return mMovieList.get(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    /* A holder class to hold recycler view item components. */
    {
        private ImageView mMoviePoster;
        private TextView mMovieTitle, mMovieRating, mMovieGenre;
        private RatingBar mMovieRatingBar;

        public MyViewHolder(final View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.card_view_movie_poster);
            mMovieTitle = itemView.findViewById(R.id.card_view_movie_title);
            mMovieRating = itemView.findViewById(R.id.card_view_movie_rating);
            mMovieRatingBar = itemView.findViewById(R.id.card_view_movie_rating_bar);
            mMovieGenre = itemView.findViewById(R.id.card_view_movie_genre);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface == null)
                    return;

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    recyclerViewInterface.onItemClick(position);
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    /* This method is used to inflate the view layout. */
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieRecyclerViewAdapter.MyViewHolder(view, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    /* This is the main method for assigning values to the recycler view layout components. */
    {
        /* Retrieves movie information by selected view position. */
        String poster = mMovieList.get(position).getPosterPath();
        String title = mMovieList.get(position).getTitle();

        float fRating = mMovieList.get(position).getVoteAverage() / 2.0f;
        String sRating = new DecimalFormat("0.0").format(fRating);


        /* Assigns fragment component values. */
        Picasso.get()
                .load(poster)
                .error(R.drawable.ic_no_image)
                .into(holder.mMoviePoster);

        holder.mMovieTitle.setText(title);
        holder.mMovieRating.setText(sRating);
        holder.mMovieRatingBar.setRating(fRating);
    }

    @Override
    public int getItemCount()
    /* Giving the recycler view number of how much items to display. */
    {
        if (mMovieList != null) return mMovieList.size();
        return 0;
    }

}
