package com.example.bookwormadventuresdeluxe2;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookwormadventuresdeluxe2.Utilities.Status;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder>
{
    private ArrayList<Book> books;
    private Context context;
    public BookListAdapter.BookListViewHolder bookListViewHolder;

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
        }
    }

    public BookListAdapter(ArrayList<Book> books, Context context)
    {
        this.books = books;
        this.context = context;
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

    public BookListAdapter.BookListViewHolder getViewHolder()
    {
        return this.bookListViewHolder;
    }

    // Replace the contents of the view with the appropriate data
    public void onBindViewHolder(BookListViewHolder bookListViewHolder, int position)
    {
        bookListViewHolder.title.setText(books.get(position).getTitle());
        bookListViewHolder.author.setText(books.get(position).getAuthor());
        bookListViewHolder.isbn.setText(books.get(position).getIsbn());

        setStatusCircleColor(books.get(position).getStatus(), bookListViewHolder.statusCircle);
    }

    @Override
    public int getItemCount()
    {
        return books.size();
    }

    /**
     * Sets the color of an image view based on the given status
     *
     * @param status       The status of the item displaying the imageView
     * @param statusCircle The reference to the imageView to re-color
     */
    public void setStatusCircleColor(Status status, ImageView statusCircle)
    {
        switch (status)
        {
            case AVAILABLE:
                statusCircle.getDrawable().setColorFilter(context.getResources().getColor(R.color.available), PorterDuff.Mode.SRC_ATOP);
                break;
            case BORROWED:
                statusCircle.getDrawable().setColorFilter(context.getResources().getColor(R.color.borrowed), PorterDuff.Mode.SRC_ATOP);
                break;
            case REQUESTED:
                statusCircle.getDrawable().setColorFilter(context.getResources().getColor(R.color.requested), PorterDuff.Mode.SRC_ATOP);
                break;
            case ACCEPTED:
                statusCircle.getDrawable().setColorFilter(context.getResources().getColor(R.color.accepted), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                /* We would not expect any other id */
                throw new IllegalArgumentException();
        }
    }

}
