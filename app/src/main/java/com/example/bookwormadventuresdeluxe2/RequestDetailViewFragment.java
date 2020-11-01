package com.example.bookwormadventuresdeluxe2;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookwormadventuresdeluxe2.Utilities.DetailView;

/**
 * Holds the view for seeing details on a book in the Requested tab
 * The user will be able to interact with request options on the book
 */
//TODO: add status specific buttons, functions, and labels
public class RequestDetailViewFragment extends DetailView
{
    private Button btn1;
    private Button btn2;
    private TextView exchange;

    public RequestDetailViewFragment()
    {
        // Required empty public constructor
    }

    public static RequestDetailViewFragment newInstance(String param1, String param2)
    {
        RequestDetailViewFragment fragment = new RequestDetailViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.bookDetailView = inflater.inflate(R.layout.fragment_request_detail_view, null, false);
        ((TextView) bookDetailView.findViewById(R.id.app_header_title)).setText(R.string.requests_title);

        // Setup back button
        super.onCreateView(inflater, container, savedInstanceState);

        this.btn1 = this.bookDetailView.findViewById(R.id.requestDetail_btn1);
        this.btn2 = this.bookDetailView.findViewById(R.id.requestDetail_btn2);
        this.exchange = this.bookDetailView.findViewById(R.id.request_exchange_location);

        //TODO: do different stuff based on book status

        // case request
//        this.btn1.setText(getString(R.string.accept));
//        this.btn2.setText(getString(R.string.deny));
//
//        this.btn1.setOnClickListener(this::btnAccept);
//        this.btn2.setOnClickListener(this::btnDeny);
//
//        this.btn1.setVisibility(View.VISIBLE);
//        this.btn2.setVisibility(View.VISIBLE);

        // case accept
//        this.btn1.setText(getString(R.string.set_location_label));
//        this.btn2.setText(getString(R.string.lend_book));
//
//        this.btn1.setOnClickListener(this::btnSetLocation);
//        this.btn2.setOnClickListener(this::btnLendBook);
//
//        //TODO: get pickup location from book
//        this.bookDetailView.findViewById(R.id.request_exchange).setVisibility(View.VISIBLE);
//
//        this.btn1.setVisibility(View.VISIBLE);
//        this.btn2.setVisibility(View.VISIBLE);

        // case bPending
//        this.btn1.setText(getString(R.string.wait_borrower));
//
//        this.btn1.setVisibility(View.VISIBLE);

        // case rPending
//        this.btn1.setText(getString(R.string.accept_return));
//        this.btn2.setText(getString(R.string.view_location));
//
//        this.btn1.setOnClickListener(this::btnAcceptReturn);
//        this.btn2.setOnClickListener(this::btnViewLocation);
//
//        this.btn1.setVisibility(View.VISIBLE);
//        this.btn2.setVisibility(View.VISIBLE);

        return bookDetailView;
    }

    private void btnViewLocation(View view)
    {
        //TODO: actually do the stuff
        // launch ViewLocation
    }

    private void btnAcceptReturn(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        // update book status
        // launch RequestsFragment
    }

    private void btnLendBook(View view)
    {
        //TODO: actually do the stuff
        // Launch Scan ISBN
        // update book status
        // launch RequestsFragment
    }

    private void btnSetLocation(View view)
    {
        //TODO: actually do the stuff
        // launch SetLocation
    }

    private void btnDeny(View view)
    {
        //TODO: actually do the stuff
        // get borrower id
        // remove borrower from book request list
        // launch RequestsFragment
    }

    private void btnAccept(View view)
    {
        //TODO: actually do the stuff
        // change book status to accepted (clear book request list)
        // launch new RequestDetailViewFragment with updated book
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
        status.setText(book.getStatus().toString() + " " + getString(R.string.detail_join));

        TextView user = bookDetailView.findViewById(R.id.book_request_user);
        user.setText("TODO: get borrower");
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