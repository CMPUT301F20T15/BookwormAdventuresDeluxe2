package com.example.bookwormadventuresdeluxe2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookwormadventuresdeluxe2.Utilities.RecyclerViewClickListener;
import com.example.bookwormadventuresdeluxe2.Utilities.RecyclerViewTouchListener;
import com.example.bookwormadventuresdeluxe2.Utilities.Status;

import java.util.ArrayList;


/**
 * A {@link Fragment} subclass for navbar menu item 1.
 */
public class MyBooksFragment extends Fragment
{
    private RecyclerView myBooksRecyclerView;
    private BookListAdapter myBooksRecyclerAdapter;
    private RecyclerView.LayoutManager myBooksRecyclerLayoutManager;
    private ArrayList<Book> myBooksList;

    public MyBooksFragment()
    {
        this.myBooksList = new ArrayList<Book>();
        // Temporary books to show how listview looks
        myBooksList.add(new Book("1984", "George Orwell", "9780141036144", Status.AVAILABLE));
        myBooksList.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780446310789", Status.REQUESTED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //https://stackoverflow.com/questions/9469174/set-theme-for-a-fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_NoActionBar);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.fragment_my_books, container, false);

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_books, container, false);

    }

    // https://stackoverflow.com/questions/6495898/findviewbyid-in-fragment#:~:text=Use%20getView%20%28%29%20or%20the%20View%20parameter%20from,method%29.%20With%20this%20you%20can%20call%20findViewById%20%28%29.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        myBooksRecyclerView = (RecyclerView) view.findViewById(R.id.my_books_recycler_view);
        myBooksRecyclerView.setHasFixedSize(true);

        myBooksRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        myBooksRecyclerView.setLayoutManager(myBooksRecyclerLayoutManager);

        myBooksRecyclerAdapter = new BookListAdapter(myBooksList, this.getContext());
        myBooksRecyclerView.setAdapter(myBooksRecyclerAdapter);

        myBooksRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this.getContext(), myBooksRecyclerView, new RecyclerViewClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Log.v("HERE ", myBooksList.get(position).getTitle());
//                Intent myBooksDetailViewIntent = new Intent(getActivity(), MyBooksDetailViewActivity.class);
//                getActivity().startActivity(myBooksDetailViewIntent);
//                Toast.makeText(this, myBooksList.get(position).getTitle() + " is clicked!", Toast.LENGTH_SHORT).show();
//                replaceFragment(new MyBooksDetailViewFragment());
                MyBooksDetailViewFragment eventadd = new MyBooksDetailViewFragment();
                getFragmentManager().beginTransaction().replace(R.id.frame_container, eventadd).commit();
            }

            @Override
            public void onLongClick(View view, int position)
            {
//                Toast.makeText(this, myBooksList.get(position).getTitle() + " is long pressed!", Toast.LENGTH_SHORT).show();

            }
        }));
//        BookListAdapter.BookListViewHolder vh = myBooksRecyclerAdapter.bookListViewHolder;
//
    }

}