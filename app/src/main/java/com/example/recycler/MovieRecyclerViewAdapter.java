package com.example.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.tmdb.models.MovieGenreModel;
import com.example.tmdb.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface mRecyclerViewInterface;

    private Context mContext;
    private List<MovieModel> mMovieList;
    private List<MovieGenreModel> mMovieGenreList;

    public MovieRecyclerViewAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        mRecyclerViewInterface = recyclerViewInterface;
    }

    public void setMovieList(List<MovieModel> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    public void setMovieGenreList(List<MovieGenreModel> movieGenreList) {
        mMovieGenreList = movieGenreList;
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
        private TextView mMovieTitle, mReleaseYear, mMovieRating, mMovieGenres;
        private RatingBar mMovieRatingBar;

        public MyViewHolder(final View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.card_view_movie_poster);
            mMovieTitle = itemView.findViewById(R.id.card_view_movie_title);
            mReleaseYear = itemView.findViewById(R.id.card_view_movie_release_year);
            mMovieRating = itemView.findViewById(R.id.card_view_movie_rating);
            mMovieRatingBar = itemView.findViewById(R.id.card_view_movie_rating_bar);
            mMovieGenres = itemView.findViewById(R.id.card_view_movie_genre);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface == null)
                    return;

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                    recyclerViewInterface.onItemClick(itemView, position);
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
        String poster = mMovieList.get(position).getPoster_path();
        String title = mMovieList.get(position).getTitle();
        String release = mMovieList.get(position).getRelease_date();

        float fRating = mMovieList.get(position).getVote_average() / 2.0f;
        String sRating = new DecimalFormat("0.0").format(fRating);

        /* Assigns fragment component values. */
        Picasso.get()
                .load(poster)
                .error(R.drawable.ic_no_image)
                .into(holder.mMoviePoster);

        Map<Integer, String> genreMap = new HashMap<>();
        mMovieGenreList.stream()
                .forEach(genre -> genreMap.put(genre.getId(), genre.getName()));

        List<String> movieGenreIds= mMovieList.get(position).getGenre_ids();

        String movieGenreNames = movieGenreIds.stream()
                .map(movieId -> genreMap.get(Integer.valueOf(movieId)))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" ● "));

        holder.mMovieGenres.setText(movieGenreNames);

        holder.mMovieTitle.setText(title);
        holder.mReleaseYear.setText(release);
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
