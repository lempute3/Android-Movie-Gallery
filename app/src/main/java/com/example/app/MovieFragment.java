package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tmdb.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

public class MovieFragment extends Fragment {

    private ImageView mMovieImagePoster;
    private TextView mMovieTitle, mMovieGenre, mMovieRatingText, mMovieOverview;
    private RatingBar mMovieRatingBar;
    private Button mBackButton;

    private MovieModel movieModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        /*IMAGES*/
        mMovieImagePoster = view.findViewById(R.id.movie_poster);

        /*TEXT VIEWS*/
        mMovieTitle = view.findViewById(R.id.movie_title);
        mMovieGenre = view.findViewById(R.id.movie_genre);
        mMovieRatingText = view.findViewById(R.id.movie_rating);
        mMovieOverview = view.findViewById(R.id.movie_overview);

        /*RATING BAR*/
        mMovieRatingBar = view.findViewById(R.id.movie_rating_bar);

        /*BUTTONS*/
        mBackButton = view.findViewById(R.id.back_btn);

        /*EVENT LISTENERS*/
        mBackButton.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        getBundleFormArguments();

        return view;
    }

    private void getBundleFormArguments()
    /* This method is used for getting movie model bundle from fragment arguments. */
    {

        /* Checks if bundle exists. */
        Bundle movieBundle = getArguments();
        if (movieBundle == null) return;

        /* Gets movie model bundle. */
        movieModel = movieBundle.getParcelable("movie");
        String  poster   = movieModel.getPosterPath();
        String  title    = movieModel.getTitle();
        String  overview = movieModel.getMovieOverview();

        float   fRating  = movieModel.getVoteAverage() / 2.0f;
        String  sRating  = new DecimalFormat("0.0").format(fRating);

        /* Assigns fragment component values. */
        Picasso.get()
                .load(poster)
                .error(R.drawable.ic_no_image)
                .into(mMovieImagePoster);

        mMovieTitle.setText(title);
        mMovieOverview.setText(overview);
        mMovieRatingText.setText(sRating);
        mMovieRatingBar.setRating(fRating);
    }

}