package com.example.bookwormadventuresdeluxe2.NotificationUtility;

/**
 * Body of the notification
 */
public class NotificationData
{
    public Data data;
    public String to;

    public NotificationData(Data data, String to)
    {
        this.data = data;
        this.to = to;
    }

    public NotificationData()
    {
    }

}
