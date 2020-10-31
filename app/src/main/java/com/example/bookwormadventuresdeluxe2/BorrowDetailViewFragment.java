package com.example.bookwormadventuresdeluxe2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookwormadventuresdeluxe2.Utilities.DetailView;

/**
 * Holds the view for seeing details on a book in the borrowed tab
 * The user will be able to interact with borrow options on the book
 */
//TODO: add status specific buttons, functions, and labels
public class BorrowDetailViewFragment extends DetailView
{
    private Button btn1;
    private Button btn2;
    private TextView exchange;

    public BorrowDetailViewFragment()
    {
        // Required empty public constructor
    }

    public static BorrowDetailViewFragment newInstance(String param1, String param2)
    {
        BorrowDetailViewFragment fragment = new BorrowDetailViewFragment();
        return fragment;
    }

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

        //TODO: do different stuff based on book status

        // case request
        // only able to view book details

        // case accept
//        this.btn1.setText(getString(R.string.view_location));
//
//        this.btn1.setOnClickListener(this::btnViewLocation);
//
//        this.btn1.setVisibility(View.VISIBLE);

        // case bPending
//        this.btn1.setText(getString(R.string.view_location));
//        this.btn2.setText(getString(R.string.scan));
//
//        this.btn1.setOnClickListener(this::btnViewLocation);
//        this.btn2.setOnClickListener(this::btnScan);
//
//        this.btn1.setVisibility(View.VISIBLE);
//        this.btn2.setVisibility(View.VISIBLE);

        // case Borrow
//        this.btn1.setText(getString(R.string.set_location));
//        this.btn2.setText(getString(R.string.return_book));
//
//        //TODO: get pickup location from book
//        this.bookDetailView.findViewById(R.id.borrow_exchange).setVisibility(View.VISIBLE);
//
//        this.btn1.setOnClickListener(this::btnSetLocation);
//        this.btn2.setOnClickListener(this::btnReturnBook);
//
//        this.btn1.setVisibility(View.VISIBLE);
//        this.btn2.setVisibility(View.VISIBLE);

        // case rPending
//        this.btn1.setText(getString(R.string.wait_owner));
//
//        this.btn1.setVisibility(View.VISIBLE);
        return bookDetailView;
    }

    private void btnSetLocation(View view)
    {
        //TODO: actually do the stuff
        // launch SetLocation
    }

    private void btnReturnBook(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        // update book status
        // launch RequestFragment
    }

    private void btnScan(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        // update book status
        // launch RequestsFragment
    }

    private void btnViewLocation(View view)
    {
        //TODO: actually do the stuff
        // launch ViewLocation
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
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}