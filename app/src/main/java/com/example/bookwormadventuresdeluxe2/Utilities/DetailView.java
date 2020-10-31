package com.example.bookwormadventuresdeluxe2.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bookwormadventuresdeluxe2.Book;
import com.example.bookwormadventuresdeluxe2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * abstract class representing all the DetailView fragments
 * Implements parts of methods common to all DetailViews
 */
public abstract class DetailView extends Fragment
{
    protected Book selectedBook;
    protected String selectedBookId;
    protected ImageButton backButton;
    protected View bookDetailView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Make the desired custom header buttons visible and set their click listeners
        this.backButton = bookDetailView.findViewById(R.id.app_header_back_button);
        this.backButton.setVisibility(View.VISIBLE);
        this.backButton.setOnClickListener(this::onBackClick);

        if (this.selectedBook != null)
        {
            updateView(this.selectedBook);
        }

        return bookDetailView;
    }

    /**
     * Update the textfields for book detail view based on the given book
     *
     * @param book The book containing the data to populate the textfields with
     */
    public void updateView(Book book)
    {
        // Set the content based on the book that was selected
        TextView title = bookDetailView.findViewById(R.id.book_details_title);
        title.setText(book.getTitle());

        TextView authorName = bookDetailView.findViewById(R.id.book_details_author);
        authorName.setText(book.getAuthor());

        TextView description = bookDetailView.findViewById(R.id.book_details_description);
        description.setText(book.getDescription());

        TextView isbn = bookDetailView.findViewById(R.id.book_details_isbn);
        isbn.setText(book.getIsbn());

        ImageView bookPhoto = bookDetailView.findViewById(R.id.book_details_image);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference dateRef = storageReference.child(book.getImageReference());
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Log.v("HERE 1", String.valueOf(downloadUrl));
                new DownloadImageTask(bookPhoto).execute(downloadUrl.toString());

//                Bitmap bm = getBitmapFromURL(book.getImageReference());
//                if (bm != null)
//                {
//                    bookPhoto.setImageBitmap(bm);
//                }
//                else
//                {
//                    Log.v("HERE 2", "fail");
//
//                }
            }
        });


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();
//        StorageReference bookPhotoReference = storageReference.child(book.getImageReference());

        Log.v("HERE 1", book.getImageReference());

    }

    public Bitmap getBitmapFromURL(String src)
    {
        try
        {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e)
        {
            Log.v("HERE ", "fail 2");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Receives and sets the selected book from the calling fragment
     *
     * @param selectedBook The book that was selected from calling fragment
     */
    public void onFragmentInteraction(Book selectedBook, String documentId)
    {
        this.selectedBook = selectedBook;
        this.selectedBookId = documentId;
    }

    /**
     * Takes the user back to the main Requests screen
     *
     * @param v The view that was clicked on
     */
    abstract public void onBackClick(View v);
}
