package com.example.app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.tmdb.request.RetrofitHelper;
import com.example.app.viewmodels.MovieListViewModel;
import com.example.tmdb.models.MovieModel;

public class HomeFragment extends Fragment {

    private RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
    private MovieListViewModel mMovieListViewModel;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        mRecyclerView = view.findViewById(R.id.h_recycler_view);

        /*OBSERVERS*/
        ConfigureRecycleView();
        ObserveAnyChange();

        return view;
    }

    private void ConfigureRecycleView()
    /* Initializes recycler view layout. */
    {
        mMovieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMovieRecyclerViewAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void ObserveAnyChange()
        /* This method is used for observing live-data change in View-Model. */
    {
        Toast.makeText(getContext(), "Observed", Toast.LENGTH_LONG).show();
        mMovieListViewModel.getMovies().observe(getViewLifecycleOwner(), movieModels -> {
            if (movieModels != null) {
                mMovieRecyclerViewAdapter.setMovieList(movieModels);
            }
        });
    }
}