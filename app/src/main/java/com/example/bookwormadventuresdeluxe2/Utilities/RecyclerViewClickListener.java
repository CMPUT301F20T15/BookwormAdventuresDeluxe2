package com.example.bookwormadventuresdeluxe2.Utilities;

/**
 * Defines a click listener and long click listener for a RecyclerView
 */

import android.view.View;

public interface RecyclerViewClickListener
{
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}