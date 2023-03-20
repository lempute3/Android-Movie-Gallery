package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.tmdb.request.TMDBRetrofitHelper;
import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.app.viewmodels.MovieListViewModel;
import com.example.recycler.CategoryRecyclerViewAdapter;
import com.example.recycler.MovieRecyclerViewAdapter;
import com.example.recycler.RecyclerViewInterface;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private MovieListViewModel mMovieListViewModel;

    private RecyclerView mCategoryRecyclerView, mMovieRecyclerView;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;
    private CategoryRecyclerViewAdapter mCategoryRecyclerViewAdapter;

    private FragmentManager mFragmentManager;
    private FrameLayout mFragmentLayout;
    private UIFragmentSwitcher mMovieFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*VIEW-MODELS*/
        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        /*FRAGMENTS*/
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentLayout = view.findViewById(R.id.h_frame_layout);
        mMovieFragment = new UIFragmentSwitcher(mFragmentLayout.getId(), mFragmentManager, new MovieFragment());

        /*RECYCLER VIEWS*/
        mCategoryRecyclerView = view.findViewById(R.id.h_category_recycler_view);
        mMovieRecyclerView = view.findViewById(R.id.h_movie_recycler_view);


        ConfigureCategoryRecycleView();
        ConfigureMovieRecycleView();
        ObserveAnyChange();

        return view;
    }

    private void ConfigureCategoryRecycleView()
    /*Initializes category recycler view layout. */
    {
        mCategoryRecyclerView.setHasFixedSize(true);
        mCategoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(getContext(), this);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mCategoryRecyclerView.setAdapter(mCategoryRecyclerViewAdapter);
        mMovieListViewModel.searchMovieGenresApi();
    }

    private void ConfigureMovieRecycleView()
    /* Initializes movie recycler view layout. */
    {
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext(), this);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMovieRecyclerView.setAdapter(mMovieRecyclerViewAdapter);

        // RecyclerView Pagination.
        mMovieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollHorizontally(1))
                    mMovieListViewModel.searchNextPage();
            }
        });
    }

    private void ObserveAnyChange()
    /* This method is used for observing live-data change in View-Model. */
    {
        mMovieListViewModel.getMovies().observe(getViewLifecycleOwner(), movieModels -> {
            if (movieModels == null || movieModels.isEmpty()) return;
            mMovieRecyclerViewAdapter.setMovieList(movieModels);
        });

        mMovieListViewModel.getMoviesGenres().observe(getViewLifecycleOwner(), movieGenreModels -> {
            if (movieGenreModels == null || movieGenreModels.isEmpty()) return;
            mCategoryRecyclerViewAdapter.setMovieGenreList(movieGenreModels);
            mMovieRecyclerViewAdapter.setMovieGenreList(movieGenreModels);
        });
    }

    @Override
    public void onItemClick(final View viewItem, int position) {

        View parentView = (View) viewItem.getParent();

        /* Handles click event on movie category recycler view. */
        if (mCategoryRecyclerView.getTag().equals(parentView.getTag())) {
            int selectedGenre = mCategoryRecyclerViewAdapter.getSelectedMovieGenre(position).getId();
            mMovieListViewModel.searchMovieApi(null, selectedGenre, 1);
        }

        /* Handles click event on movie recycler view. */
        if (mMovieRecyclerView.getTag().equals(parentView.getTag())) {
            // Puts selected movie into bundle.
            Bundle movieBundle = new Bundle();
            movieBundle.putParcelable("movie", mMovieRecyclerViewAdapter.getSelectedMovie(position));

            // Displays movie fragment with passed bundle information.
            mMovieFragment.setBundle(movieBundle);
            mMovieFragment.setFragment();
        }
    }

    @Override
    public void onItemLongClick(int position) {

    }

}