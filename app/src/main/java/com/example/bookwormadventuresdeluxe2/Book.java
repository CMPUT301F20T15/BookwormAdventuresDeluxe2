package com.example.bookwormadventuresdeluxe2;

import android.graphics.PorterDuff;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.example.bookwormadventuresdeluxe2.Utilities.Status;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable
{
    // Basic attributes for now, rest added as needed
    private String owner;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Status status;
    private ArrayList<String> requesters;
    private String borrower;

    // BookListAdapter which is now a FirestoreRecyclerAdapter requires empty constructor
    public Book()
    {

    }

    public Book(String title, String author, String description, String isbn, Status status)
    {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.description = description;
        this.status = status;
        this.requesters = new ArrayList<String>();
        this.owner = "";
        this.borrower = "";
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public String getOwner()
    {
        return owner;
    }

    public ArrayList<String> getRequesters()
    {
        return requesters;
    }

    /**
     * Sets the color of an image view based on the given status
     *
     * @param statusCircle The reference to the imageView to re-color
     * @param user         The user to find the status for
     */
    public void setStatusCircleColor(ImageView statusCircle, String user)
    {
        switch (getAugmentStatus(user))
        {
            case Available:
                statusCircle.getDrawable().setColorFilter(ResourcesCompat.getColor(GlobalApplication.getAppContext().getResources(), R.color.available, null), PorterDuff.Mode.SRC_ATOP);
                break;
            case Requested:
                statusCircle.getDrawable().setColorFilter(ResourcesCompat.getColor(GlobalApplication.getAppContext().getResources(), R.color.requested, null), PorterDuff.Mode.SRC_ATOP);
                break;
            case Accepted:
                statusCircle.getDrawable().setColorFilter(ResourcesCompat.getColor(GlobalApplication.getAppContext().getResources(), R.color.accepted, null), PorterDuff.Mode.SRC_ATOP);
                break;
            case Borrowed:
                statusCircle.getDrawable().setColorFilter(ResourcesCompat.getColor(GlobalApplication.getAppContext().getResources(), R.color.borrowed, null), PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                /* We would not expect any other id */
                throw new IllegalArgumentException();
        }
    }

    public Status getAugmentStatus(String user)
    {
        switch (status)
        {
            case Available:
            case Requested:
                if (this.requesters != null && (this.requesters.contains(user) || user.equals(this.owner) && this.requesters.size() > 0))
                {
                    return Status.Requested;
                }
                else
                {
                    return Status.Available;
                }
            case Accepted:
                return Status.Accepted;
            case bPending:
                if (user.equals(this.owner))
                {
                    return Status.Borrowed;
                }
                else
                {
                    return Status.Accepted;
                }
            case Borrowed:
                return Status.Borrowed;
            case rPending:
                if (user.equals(this.owner))
                {
                    return Status.Borrowed;
                }
                else
                {
                    return Status.Available;
                }
            default:
                /* We would not expect any other id */
                throw new IllegalArgumentException();
        }
    }

    public Notification createNotification(String message)
    {
        return new Notification(this, message);
    }
}


