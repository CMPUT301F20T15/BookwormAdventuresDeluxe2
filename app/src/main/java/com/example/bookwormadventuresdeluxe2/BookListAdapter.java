package com.example.bookwormadventuresdeluxe2;

/**
 * BookListAdapter is a FirestoreRecycler data which acts as middleware between the books
 * on Firestore and the UI that displays them by providing view updaters and onClickListeners
 * for items in the RecyclerView.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookwormadventuresdeluxe2.Utilities.DetailView;
import com.example.bookwormadventuresdeluxe2.Utilities.UserCredentialAPI;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// https://stackoverflow.com/questions/49277797/how-to-display-data-from-firestore-in-a-recyclerview-with-android
public class BookListAdapter extends FirestoreRecyclerAdapter<Book, BookListAdapter.BookListViewHolder>
{
    private Context context;
    private int caller;

    // Reference to the views for each item
    public static class BookListViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView author;
        public TextView isbn;
        public ImageView statusCircle;
        public ImageView bookPhoto;
        public ConstraintLayout bookItemLayout;

        /**
         * A viewHolder for the books
         *
         * @param bookItemLayout The layout to reference
         */
        public BookListViewHolder(ConstraintLayout bookItemLayout)
        {
            super(bookItemLayout);
            this.title = (TextView) bookItemLayout.getViewById(R.id.book_item_title);
            this.author = (TextView) bookItemLayout.getViewById(R.id.book_item_author);
            this.isbn = (TextView) bookItemLayout.getViewById(R.id.book_item_isbn);
            this.statusCircle = (ImageView) bookItemLayout.getViewById(R.id.book_item_status);
            this.bookItemLayout = (ConstraintLayout) bookItemLayout.getViewById(R.id.book_item);
            this.bookPhoto = (ImageView) bookItemLayout.getViewById(R.id.book_item_image);
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
        BookListViewHolder bookListViewHolder = new BookListViewHolder(bookItem);
        return bookListViewHolder;
    }

    private View.OnClickListener launchDetailView(DetailView bookDetailFragment, Book book, String documentId)
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            // Handles a click on an item in the recycler view
            @Override
            public void onClick(View v)
            {
                // Opens the book in detail view
                bookDetailFragment.onFragmentInteraction(book, documentId);

                ((MyBooksActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, bookDetailFragment).commit();
            }
        };
        return listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookListViewHolder holder, int position, @NonNull Book book)
    {
        // Set the text on the item view for each book
        String documentId = getSnapshots().getSnapshot(position).getId();
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.isbn.setText(book.getIsbn());
        DetailView detailView;
        String user = UserCredentialAPI.getInstance().getUsername();
        book.setStatusCircleColor(holder.statusCircle, user);
        book.setPhoto(book, holder.bookPhoto);

        switch (this.caller)
        {
            case R.id.my_books:
                detailView = new MyBooksDetailViewFragment();
                break;
            case R.id.requests:
                detailView = new RequestDetailViewFragment();
                break;
            case R.id.borrow:
                detailView = new BorrowDetailViewFragment();
                break;
            default:
                throw new IllegalArgumentException();
        }

        holder.itemView.setOnClickListener(launchDetailView(detailView, book, documentId));
    }
}
