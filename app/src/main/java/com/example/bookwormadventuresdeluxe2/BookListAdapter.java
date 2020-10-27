package com.example.bookwormadventuresdeluxe2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

// https://stackoverflow.com/questions/49277797/how-to-display-data-from-firestore-in-a-recyclerview-with-android
public class BookListAdapter extends FirestoreRecyclerAdapter<Book, BookListAdapter.BookListViewHolder>
{
    private ArrayList<Book> books;
    private Context context;
    public BookListAdapter.BookListViewHolder bookListViewHolder;
    private int caller;

    // Reference to the views for each item
    public static class BookListViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView author;
        public TextView isbn;
        public ImageView statusCircle;
        public ConstraintLayout bookItemLayout;

        public BookListViewHolder(ConstraintLayout bookItemLayout)
        {
            super(bookItemLayout);
            this.title = (TextView) bookItemLayout.getViewById(R.id.book_item_title);
            this.author = (TextView) bookItemLayout.getViewById(R.id.book_item_author);
            this.isbn = (TextView) bookItemLayout.getViewById(R.id.book_item_isbn);
            this.statusCircle = (ImageView) bookItemLayout.getViewById(R.id.book_item_status);
            this.bookItemLayout = (ConstraintLayout) bookItemLayout.getViewById(R.id.book_item);
        }
    }

    public BookListAdapter(Context context, FirestoreRecyclerOptions options, int caller)
    {
        super(options);
        this.context = context;
        this.caller = caller;
    }

    // Create new views
    public BookListAdapter.BookListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ConstraintLayout bookItem = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        BookListViewHolder bookListViewHolder = new BookListViewHolder((bookItem));
        this.bookListViewHolder = bookListViewHolder;
        return bookListViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookListViewHolder holder, int position, @NonNull Book book)
    {
        // Set the text on the item view for each book
        String documentId = getSnapshots().getSnapshot(position).getId();
        bookListViewHolder.title.setText(book.getTitle());
        bookListViewHolder.author.setText(book.getAuthor());
        bookListViewHolder.isbn.setText(book.getIsbn());

        switch(this.caller) {
            case R.id.my_books:
                book.setStatusCircleColor(book.getStatus(), bookListViewHolder.statusCircle);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    // Handles a click on an item in the recycler view
                    @Override
                    public void onClick(View v)
                    {
                        // Opens the book in detail view
                        MyBooksDetailViewFragment bookDetailFragment = new MyBooksDetailViewFragment();
                        bookDetailFragment.onFragmentInteraction(book, documentId);

                        ((MyBooksActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, bookDetailFragment).commit();
                    }
                });
                break;
            case R.id.requests:
                book.setStatusCircleColor(book.getStatus(), bookListViewHolder.statusCircle);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    // Handles a click on an item in the recycler view
                    @Override
                    public void onClick(View v)
                    {
                        // Opens the book in request view
                        RequestDetailViewFragment fragment = new RequestDetailViewFragment();
                        fragment.onFragmentInteraction(book, documentId);

                        ((MyBooksActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, fragment).commit();
                    }
                });
                break;
            case R.id.borrow:
                book.setStatusCircleColor(book.getStatus(), bookListViewHolder.statusCircle);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    // Handles a click on an item in the recycler view
                    @Override
                    public void onClick(View v)
                    {
                        // Opens the book in borrow view
                        BorrowDetailViewFragment fragment = new BorrowDetailViewFragment();
                        fragment.onFragmentInteraction(book, documentId);

                        ((MyBooksActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, fragment).commit();
                    }
                });
                break;
            default:
                Log.d("Error", "Error in BookListAdapter: caller not found");
        }

    }
}
