package com.example.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.tmdb.models.MovieGenreModel;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {

    private final RecyclerViewInterface mRecyclerViewInterface;
    private Context mContext;

    private List<MovieGenreModel> mMovieGenreList;

    public CategoryRecyclerViewAdapter(Context context, RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        mRecyclerViewInterface = recyclerViewInterface;
    }

    public void setMovieGenreList(List<MovieGenreModel> movieGenreList) {
        mMovieGenreList = movieGenreList;
        notifyDataSetChanged();
    }

    public MovieGenreModel getSelectedMovieGenre(int position) {
        if (mMovieGenreList == null || mMovieGenreList.size() <= 0)
            return null;
        return mMovieGenreList.get(position);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder
    /* A holder class to hold recycler view item components. */
    {
        private TextView mGenreTitle;

        public CategoryViewHolder(final View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            mGenreTitle = itemView.findViewById(R.id.category_list_label);

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
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    /* This method is used to inflate the view layout. */
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(view, mRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position)
    /* This is the main method for assigning values to the recycler view layout components. */
    {
        /* Retrieves movie genre names by selected view position. */
        String name = mMovieGenreList.get(position).getName().toUpperCase();
        holder.mGenreTitle.setText(name);
    }

    @Override
    public int getItemCount()
    /* Giving the recycler view number of how much items to display. */
    {
        if (mMovieGenreList != null) return mMovieGenreList.size();
        return 0;
    }
}
