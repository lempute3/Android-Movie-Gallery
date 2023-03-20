package com.example.recycler;

import android.view.View;

public interface RecyclerViewInterface {
    void onItemClick(final View itemView, int position);
    void onItemLongClick(int position);
}
