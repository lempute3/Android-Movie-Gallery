package com.example.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.app.viewmodels.MovieListViewModel;
import com.example.recycler.MovieRecyclerViewAdapter;
import com.example.recycler.RecyclerViewInterface;
import com.example.tmdb.models.MovieModel;

public class WatchlistFragment extends Fragment implements RecyclerViewInterface {

    private MovieListViewModel mMovieListViewModel;

    private RecyclerView mMovieRecyclerView;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    private FragmentManager mFragmentManager;
    private FrameLayout mFragmentLayout;
    private UIFragmentSwitcher mMovieFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);

        /*VIEW-MODELS*/
        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        /*FRAGMENTS*/
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentLayout = view.findViewById(R.id.w_frame_layout);
        mMovieFragment = new UIFragmentSwitcher(mFragmentLayout.getId(), mFragmentManager, new MovieFragment());

        /*RECYCLERVIEW*/
        mMovieRecyclerView = view.findViewById(R.id.w_recycler_view);

        ConfigureMovieRecycleView();
        ObserveAnyChange();

        return view;
    }

    private void ConfigureMovieRecycleView()
    /* Initializes movie recycler view layout. */
    {
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext(), this);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMovieRecyclerView.setAdapter(mMovieRecyclerViewAdapter);
        mMovieListViewModel.searchMovieGenresApi();
        mMovieListViewModel.searchUserWatchlist();
    }

    private void ObserveAnyChange()
        /* This method is used for observing live-data change in View-Model. */
    {
        mMovieListViewModel.getUserWatchlist().observe(getViewLifecycleOwner(), movieModels -> {
            if (movieModels == null || movieModels.isEmpty()) return;

            mMovieRecyclerViewAdapter.setMovieList(movieModels);
        });

        mMovieListViewModel.getMoviesGenres().observe(getViewLifecycleOwner(), movieGenreModels -> {
            if (movieGenreModels == null || movieGenreModels.isEmpty()) return;
            mMovieRecyclerViewAdapter.setMovieGenreList(movieGenreModels);
        });
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}