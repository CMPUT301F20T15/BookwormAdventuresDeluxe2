package com.example.bookwormadventuresdeluxe2;

import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.bookwormadventuresdeluxe2.Utilities.DetailView;
import com.example.bookwormadventuresdeluxe2.Utilities.UserCredentialAPI;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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

    private String source = "";

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
        /* Grabbing source fragment of book item after click*/
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            source = (String) bundle.getString(getString(R.string.book_click_source_fragment));
        }

        this.bookDetailView = inflater.inflate(R.layout.fragment_borrow_detail_view, null, false);
        ((TextView) bookDetailView.findViewById(R.id.app_header_title)).setText(source);

        // setup back button
        super.onCreateView(inflater, container, savedInstanceState);

        this.btn1 = this.bookDetailView.findViewById(R.id.borrowDetail_btn1);
        this.btn2 = this.bookDetailView.findViewById(R.id.borrowDetail_btn2);
        this.exchange = this.bookDetailView.findViewById(R.id.borrow_exchange_location);

        switch (selectedBook.getStatus())
        {
            case Available:
                this.btn1.setText(getString(R.string.request_book));

                this.btn1.setOnClickListener(this::btnRequestBook);

                this.btn1.setVisibility(View.VISIBLE);
                break;
                
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

                //TODO: get pickup location from book
//        this.bookDetailView.findViewById(R.id.borrow_exchange).setVisibility(View.VISIBLE);

                this.btn1.setOnClickListener(this::btnSetLocation);
                this.btn2.setOnClickListener(this::btnReturnBook);

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

    /**
     * Send request to book owner
     *
     * @param view The view that was clicked on
     */
    private void btnRequestBook(View view)
    {
        this.bookDocument.update(getString(R.string.requesters),
                FieldValue.arrayUnion(UserCredentialAPI.getInstance().getUsername()));
        this.bookDocument.update(getString(R.string.status), getString(R.string.requested));
    }

    private void btnSetLocation(View view)
    {
        //TODO: actually do the stuff
        // launch SetLocation
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
        user.setText(book.getOwner());

        clickUsername(user, book.getOwner());
    }

    /**
     * Opens user profile on TextView click
     *
     * @param textView TextView in view
     * @param username Book owner's username
     */
    public void clickUsername(TextView textView, String username)
    {
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /* Pulling UserProfileObject from database */
                FirebaseUserGetSet.getUser(username, new FirebaseUserGetSet.UserCallback()
                {
                    @Override
                    public void onCallback(UserProfileObject userObject)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.profile_object), userObject);
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.frame_container, profileFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });
    }

    /**
     * Takes the user back to the main Requests screen
     *
     * @param v The view that was clicked on
     */
    public void onBackClick(View v)
    {
        /* Source fragment was Search, return to search books*/
        if (source.equals(getString(R.string.search_title)))
        {
            SearchFragment fragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
        /* Source fragment was Borrow, return to Borrow */
        else if (source.equals(getString(R.string.borrow)))
        {
            RequestsFragment fragment = new RequestsFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            args.putBoolean(getString(R.string.borrow), true);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}