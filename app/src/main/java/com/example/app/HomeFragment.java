package com.example.app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.app.tmdb.request.RetrofitHelper;
import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.app.viewmodels.MovieListViewModel;
import com.example.recycler.MovieRecyclerViewAdapter;
import com.example.recycler.RecyclerViewInterface;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
    private MovieListViewModel mMovieListViewModel;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    private FragmentManager mFragmentManager;
    private FrameLayout mFragmentLayout;
    private UIFragmentSwitcher mMovieFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*FRAGMENTS*/
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentLayout = view.findViewById(R.id.h_frame_layout);
        mMovieFragment = new UIFragmentSwitcher(mFragmentLayout.getId(), mFragmentManager, new MovieFragment());

        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        mRecyclerView = view.findViewById(R.id.h_recycler_view);

        ConfigureRecycleView();
        ObserveAnyChange();

        return view;
    }

    private void ConfigureRecycleView()
    /* Initializes recycler view layout. */
    {
        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMovieRecyclerViewAdapter);

        // RecyclerView Pagination.
        // Loads new movie page.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollHorizontally(1))
                    mMovieListViewModel.searchNextPage();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void ObserveAnyChange()
    /* This method is used for observing live-data change in View-Model. */
    {
        mMovieListViewModel.getMovies().observe(getViewLifecycleOwner(), movieModels -> {

            if (movieModels == null || movieModels.isEmpty()) {
                Toast.makeText(getContext(), "No results found", Toast.LENGTH_LONG).show();
                return;
            }

            mMovieRecyclerViewAdapter.setMovieList(movieModels);
        });
    }

    @Override
    public void onItemClick(int position) {

        // Puts selected movie into bundle.
        Bundle movieBundle = new Bundle();
        movieBundle.putParcelable("movie", mMovieRecyclerViewAdapter.getSelectedMovie(position));

        mMovieFragment.setBundle(movieBundle);
        mMovieFragment.setFragment();
    }

    @Override
    public void onItemLongClick(int position) {

    }
}