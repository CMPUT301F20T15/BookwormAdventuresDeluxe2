package com.example.bookwormadventuresdeluxe2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    String source = "";

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
        super.updateView(book);

        TextView status = bookDetailView.findViewById(R.id.book_details_status);
        status.setText(getString(R.string.owned_by));

        TextView user = bookDetailView.findViewById(R.id.book_request_user);
        user.setText(book.getOwner());

        ImageView statusCircle = bookDetailView.findViewById(R.id.book_details_status_circle);
        book.setStatusCircleColor(book.getStatus(), statusCircle);

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
            Bundle args = new Bundle();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }

        /* Source fragment was Borrow, return to Requests fragment */
        else if (source.equals(getString(R.string.borrow)))
        {
            RequestsFragment fragment = new RequestsFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }
}