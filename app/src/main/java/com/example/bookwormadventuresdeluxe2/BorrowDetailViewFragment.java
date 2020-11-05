package com.example.bookwormadventuresdeluxe2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.bookwormadventuresdeluxe2.Utilities.DetailView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidParameterException;

/**
 * Holds the view for seeing details on a book in the borrowed tab
 * The user will be able to interact with borrow options on the book
 */
public class BorrowDetailViewFragment extends DetailView
{
    private Button btn1;
    private Button btn2;
    private TextView exchange;
    private DocumentReference bookDocument;

    private static int SetLocationActivityResultCode = 7;

    public BorrowDetailViewFragment()
    {
        // Required empty public constructor
    }

    public static BorrowDetailViewFragment newInstance(String param1, String param2)
    {
        BorrowDetailViewFragment fragment = new BorrowDetailViewFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.bookDetailView = inflater.inflate(R.layout.fragment_borrow_detail_view, null, false);
        ((TextView) bookDetailView.findViewById(R.id.app_header_title)).setText(R.string.borrow);

        // setup back button
        super.onCreateView(inflater, container, savedInstanceState);

        this.btn1 = this.bookDetailView.findViewById(R.id.borrowDetail_btn1);
        this.btn2 = this.bookDetailView.findViewById(R.id.borrowDetail_btn2);
        this.exchange = this.bookDetailView.findViewById(R.id.borrow_exchange_location);

        switch (selectedBook.getStatus())
        {
            case Available:
            case Requested:
                break;

            case Accepted:
                this.btn1.setText(getString(R.string.view_location));

                this.btn1.setOnClickListener(this::btnViewLocation);

                this.btn1.setVisibility(View.VISIBLE);
                break;

            case bPending:
                this.btn1.setText(getString(R.string.view_location));
                this.btn2.setText(getString(R.string.scan));

                this.btn1.setOnClickListener(this::btnViewLocation);
                this.btn2.setOnClickListener(this::btnScan);

                this.btn1.setVisibility(View.VISIBLE);
                this.btn2.setVisibility(View.VISIBLE);
                break;

            case Borrowed:
                this.btn1.setText(getString(R.string.set_location));
                this.btn2.setText(getString(R.string.return_book));

                if(this.selectedBook.getPickUpAddress().equals("")) {
                    this.btn2.setBackgroundTintList(getResources().getColorStateList(R.color.tempPhotoBackground));
                    this.btn2.setTextColor(getResources().getColorStateList(R.color.colorPrimary));
                } else {
                    this.btn2.setOnClickListener(this::btnReturnBook);
//                    this.bookDetailView.findViewById(R.id.borrow_exchange).setVisibility(View.VISIBLE);
                }

                this.btn1.setOnClickListener(this::btnSetLocation);

                this.btn1.setVisibility(View.VISIBLE);
                this.btn2.setVisibility(View.VISIBLE);
                break;

            case rPending:
                this.btn1.setText(getString(R.string.wait_owner));
                this.btn1.setBackgroundTintList(getResources().getColorStateList(R.color.tempPhotoBackground));
                this.btn1.setTextColor(getResources().getColorStateList(R.color.colorPrimary));

                this.btn1.setVisibility(View.VISIBLE);
                break;

            default:
                throw new InvalidParameterException("Bad status passed to BorrowDetailView");
        }

        this.bookDocument = FirebaseFirestore
                .getInstance()
                .collection(getString(R.string.books_collection))
                .document(this.selectedBookId);

        return bookDetailView;
    }

    private void btnSetLocation(View view)
    {
        Intent setLocationActivityIntent = new Intent(getActivity(), SetLocationActivity.class);
        startActivityForResult(setLocationActivityIntent, SetLocationActivityResultCode);
    }

    /**
     * Updates the status of the book
     *
     * @param view The view that was clicked on
     */
    private void btnReturnBook(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        this.bookDocument.update(getString(R.string.status), getString(R.string.rPending));
        // notify owner
        onBackClick(view);
    }

    /**
     * Updates the status of the book
     *
     * @param view The view that was clicked on
     */
    private void btnScan(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        this.bookDocument.update(getString(R.string.status), getString(R.string.borrowed));
        onBackClick(view);
    }

    private void btnViewLocation(View view)
    {
        Intent viewLocationIntent = new Intent(getActivity(), ViewLocationActivity.class);
        viewLocationIntent.putExtra("location", this.selectedBook.getPickUpAddress());
        startActivity(viewLocationIntent);
    }

    /**
     * Update the textfields for book detail view based on the given book
     *
     * @param book The book containing the data to populate the textfields with
     */
    public void updateView(Book book)
    {
        // Set the content based on the book that was selected
        super.updateView(book);

        TextView status = bookDetailView.findViewById(R.id.book_details_status);
        status.setText(getString(R.string.owned_by));

        TextView user = bookDetailView.findViewById(R.id.book_request_user);
        user.setText("TODO: get owner");
    }

    /**
     * Takes the user back to the main Requests screen
     *
     * @param v The view that was clicked on
     */
    public void onBackClick(View v)
    {
        RequestsFragment fragment = new RequestsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putBoolean(getString(R.string.borrow), true);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == SetLocationActivityResultCode)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String pickUpLocation = data.getStringExtra("pickUpLocation");
                this.bookDocument.update(getString(R.string.firestore_pick_up_address), pickUpLocation);
                this.selectedBook.setPickUpAddress(pickUpLocation);
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                this.bookDocument.update(getString(R.string.firestore_pick_up_address), "");
                this.selectedBook.setPickUpAddress("");
            }
        }

        BorrowDetailViewFragment fragment = new BorrowDetailViewFragment();
        fragment.onFragmentInteraction(this.selectedBook, this.selectedBookId);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}