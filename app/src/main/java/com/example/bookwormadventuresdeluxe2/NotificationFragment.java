package com.example.bookwormadventuresdeluxe2;

/**
 * Fragment responsible for showing the notifications that a user has received and allowing them
 * to interact with said notifications.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookwormadventuresdeluxe2.Utilities.Status;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NotificationFragment extends Fragment
{
    private RecyclerView notificationRecyclerView;
    private NotificationListAdapter notificationRecyclerAdapter;
    private RecyclerView.LayoutManager notificationRecyclerLayoutManager;
    private ArrayList<Notification> notificationList;

    private MaterialTextView appHeaderText;
    private ImageButton backButton;

    /**
     * Constructor for the fragment responsible for displaying notifications
     */
    public NotificationFragment()
    {
        this.notificationList = new ArrayList<Notification>();
        // TODO: replace this example with actual notifications from FireBase
        notificationList.add(new Notification(new Book("Hudson", "1984", "George Orwell", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
                "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis", "9780141036144", Status.Available, ""), "message1"));
        notificationList.add(new Notification(new Book("Hudson", "1984", "George Orwell", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
                "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis", "9780141036144", Status.Borrowed, ""), "message2"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View notificationView = inflater.inflate(R.layout.fragment_notification, container, false);

        // Set visibility of desired custom header buttons
        notificationView.findViewById(R.id.app_header_filter_button).setVisibility(View.VISIBLE);

        appHeaderText = notificationView.findViewById(R.id.app_header_title);
        appHeaderText.setText(R.string.notification_title);

        this.backButton = notificationView.findViewById(R.id.app_header_back_button);
        this.backButton.setVisibility(View.VISIBLE);
        this.backButton.setOnClickListener(this::onBackClick);

        return notificationView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        notificationRecyclerView = (RecyclerView) view.findViewById(R.id.notification_recycler_view);
        notificationRecyclerView.setHasFixedSize(true);

        notificationRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        notificationRecyclerView.setLayoutManager(notificationRecyclerLayoutManager);

        notificationRecyclerAdapter = new NotificationListAdapter(notificationList, this.getContext());
        notificationRecyclerView.setAdapter(notificationRecyclerAdapter);
    }

    /**
     * Listener for when the back button is clicked
     *
     * @param v The view this is called from
     */
    public void onBackClick(View v)
    {
        //TODO: remove notifications
        Fragment myBooksFragment = getFragmentManager().findFragmentByTag("myBooksFragment");
        getFragmentManager().beginTransaction().hide(ActiveFragmentTracker.activeFragment).show(myBooksFragment).commit();
        ActiveFragmentTracker.activeFragment = myBooksFragment;
    }
}