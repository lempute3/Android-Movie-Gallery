package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.app.uiutils.UIFragmentSwitcher;
import com.example.recycler.MovieRecyclerViewAdapter;

public class WatchlistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MovieRecyclerViewAdapter mMovieRecyclerViewAdapter;

    private FragmentManager mFragmentManager;
    private FrameLayout mFragmentLayout;
    private UIFragmentSwitcher mMovieFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);

        /*FRAGMENTS*/
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentLayout = view.findViewById(R.id.w_frame_layout);
        mMovieFragment = new UIFragmentSwitcher(mFragmentLayout.getId(), mFragmentManager, new MovieFragment());

        /*RECYCLERVIEW*/
        mRecyclerView = view.findViewById(R.id.w_recycler_view);
        ConfigureRecycleView();

        return view;
    }

    private void ConfigureRecycleView() {

    }
}