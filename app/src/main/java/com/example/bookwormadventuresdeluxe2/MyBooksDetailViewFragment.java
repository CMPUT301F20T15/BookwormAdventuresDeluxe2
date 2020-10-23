package com.example.bookwormadventuresdeluxe2;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBooksDetailViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBooksDetailViewFragment extends Fragment
{
    public MyBooksDetailViewFragment()
    {
        // Required empty public constructor
    }

    public static MyBooksDetailViewFragment newInstance(String param1, String param2)
    {
        MyBooksDetailViewFragment fragment = new MyBooksDetailViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View bookDetailView = inflater.inflate(R.layout.fragment_my_books_detail_view, null, false);
        bookDetailView.findViewById(R.id.app_header_edit_button).setVisibility(View.VISIBLE);
        bookDetailView.findViewById(R.id.app_header_back_button).setVisibility(View.VISIBLE);
        return bookDetailView;

    }
}