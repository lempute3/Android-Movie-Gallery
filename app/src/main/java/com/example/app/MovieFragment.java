package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.app.firebase.FirebaseRepository;
import com.example.app.firebase.OnMovieCheckListener;
import com.example.app.firebase.OnTaskCompletionListener;
import com.example.tmdb.models.MovieModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class MovieFragment extends Fragment {

    private FirebaseRepository mFirebaseHelper = FirebaseRepository.getInstance();;

    private ImageView mMovieImagePoster, mMovieFavouriteBtn, mMovieWatchlistBtn;
    private TextView mMovieTitle, mMovieReleaseYear, mMovieRatingText, mMovieOverview;
    private RatingBar mMovieRatingBar;
    private FloatingActionButton mBackButton;

    private MovieModel movieModel, currentMovie;

    boolean isInWatchList = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        /*IMAGES*/
        mMovieImagePoster = view.findViewById(R.id.movie_poster);

        /*TEXT VIEWS*/
        mMovieTitle = view.findViewById(R.id.movie_title);
        mMovieReleaseYear = view.findViewById(R.id.movie_release_year);
        mMovieRatingText = view.findViewById(R.id.movie_rating);
        mMovieOverview = view.findViewById(R.id.movie_overview);

        /*RATING BAR*/
        mMovieRatingBar = view.findViewById(R.id.movie_rating_bar);

        /*BUTTONS*/
        mBackButton = view.findViewById(R.id.back_btn);
        mMovieFavouriteBtn = view.findViewById(R.id.movie_liked);
        mMovieWatchlistBtn = view.findViewById(R.id.movie_watchlist);


        /*EVENT LISTENERS*/
        mBackButton.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        mMovieFavouriteBtn.setOnClickListener(view12 -> {
            ;
        });

        getBundleFormArguments();
        checkMovieExist();
        configureWatchlistButton();
        configureFavouriteButton();

        return view;
    }

    private void getBundleFormArguments()
    /* This method is used for getting movie model bundle from fragment arguments. */
    {
        /* Checks if bundle exists. */
        Bundle movieBundle = getArguments();
        if (movieBundle == null) return;

        /* Assigns movie model information. */
        movieModel = movieBundle.getParcelable("movie");
        currentMovie = movieModel;
        String  poster   = movieModel.getPoster_path();
        String  title    = movieModel.getTitle();
        String  released = movieModel.getRelease_date();
        String  overview = movieModel.getOverview();

        float   fRating  = movieModel.getVote_average() / 2.0f;
        String  sRating  = new DecimalFormat("0.0").format(fRating);

        /* Assigns fragment component values. */
        Picasso.get()
                .load(poster)
                .error(R.drawable.ic_no_image)
                .into(mMovieImagePoster);

        mMovieTitle.setText(title);
        mMovieReleaseYear.setText(released);
        mMovieOverview.setText(overview);
        mMovieRatingText.setText(sRating);
        mMovieRatingBar.setRating(fRating);
    }

    void checkMovieExist()
    /* This method is used for checking if the current movie exist in user's watchlist. */
    {
        int movieId = currentMovie.getId();

        mFirebaseHelper.checkMovieInWatchlist(movieId, new OnMovieCheckListener() {
            @Override
            public void onMovieExist(boolean exist) {
                setButtonTint(mMovieWatchlistBtn, exist);
            }

            @Override
            public void onMovieError(String message) {
                Toast.makeText(getContext(), "Movie Error: " + message, Toast.LENGTH_LONG).show();
            }
        });

        mFirebaseHelper.checkMovieInFavourites(movieId, new OnMovieCheckListener() {
            @Override
            public void onMovieExist(boolean exist) {
                setButtonTint(mMovieFavouriteBtn, exist);
            }

            @Override
            public void onMovieError(String message) {
                Toast.makeText(getContext(), "Movie Error: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configureWatchlistButton()
    /* This method is used for adding and removing current movie from user's watchlist on button click. */
    {
        mMovieWatchlistBtn.setOnClickListener(view12 -> {
            mFirebaseHelper.addMovieToWatchlist(currentMovie, new OnTaskCompletionListener() {
                @Override
                public void onSuccess() {
                    setButtonTint(mMovieWatchlistBtn, true);
                    Toast.makeText(getContext(), "Added to Watchlist", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    mFirebaseHelper.removeMovieFromWatchlist(currentMovie.getId(), new OnTaskCompletionListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Removed from Watchlist", Toast.LENGTH_LONG).show();
                            setButtonTint(mMovieWatchlistBtn,false);
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getContext(), "Movie error: " + message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });
    }

    private void configureFavouriteButton()
    /* This method is used for adding and removing current movie from user's favourites on button click. */
    {
        mMovieFavouriteBtn.setOnClickListener(view12 -> {
            mFirebaseHelper.addMovieToFavourites(currentMovie, new OnTaskCompletionListener() {
                @Override
                public void onSuccess() {
                    setButtonTint(mMovieFavouriteBtn, true);
                    Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    mFirebaseHelper.removeMovieFromFavourites(currentMovie.getId(), new OnTaskCompletionListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Removed from Favourites", Toast.LENGTH_LONG).show();
                            setButtonTint(mMovieFavouriteBtn,false);
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getContext(), "Movie error: " + message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });
    }

    private void setButtonTint(ImageView view, boolean status)
    /* This method is used for setting view color tint*/
    {
        if (status) view.setColorFilter(ContextCompat.getColor(getContext(), R.color.app_primary_color));
        else view.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_orange));
    }

}