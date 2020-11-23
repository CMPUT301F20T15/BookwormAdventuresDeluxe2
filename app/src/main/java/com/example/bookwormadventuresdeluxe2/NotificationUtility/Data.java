package com.example.bookwormadventuresdeluxe2.NotificationUtility;

/**
 * Stores information to display in a notification
 */

public class Data
{
    private String Title;
    private String Message;

    public Data(String title, String message)
    {
        Title = title;
        Message = message;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        Message = message;
    }
}
