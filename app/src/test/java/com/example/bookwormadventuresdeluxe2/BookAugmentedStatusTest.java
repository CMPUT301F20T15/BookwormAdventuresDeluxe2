package com.example.bookwormadventuresdeluxe2;

import com.example.bookwormadventuresdeluxe2.Utilities.Status;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for the augmented book status based on context
 * These tests show the basic flow of a book exchange
 * starting from when the potential borrower looks at the book
 * to the borrower returning the book to the owner
 */
public class BookAugmentedStatusTest
{
    private String testBorrower;
    private Book book;

    /**
     * Assigns the attributes to be used in the tests
     */
    @Before
    public void setup()
    {
        testBorrower = "testBorrower";
        book = makeBook();
    }

    /**
     * Returns a book with test values in its fields
     *
     * @return Book
     */
    private Book makeBook()
    {
        return new Book("owner", "title", "author",
                "desc", "1234567890",
                Status.Available, "");
    }

    /**
     * Viewing a book that's available
     */
    @Test
    public void testGetAugmentStatusAvailable()
    {
        book.setStatus(Status.Available);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Available);
    }

    /**
     * User requested the book
     */
    @Test
    public void testGetAugmentStatusRequested()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.Available);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Requested);
    }

    /**
     * The book request has been accepted by the owner
     */
    @Test
    public void testGetAugmentStatusAccepted()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.Accepted);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Accepted);
    }

    /**
     * The book is being passed from owner to borrower
     * The owner is viewing the book
     */
    @Test
    public void testGetAugmentStatusbPendingOwner()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.bPending);
        assertEquals(book.getAugmentStatus("owner"), Status.Borrowed);
    }

    /**
     * The book is being passed from owner to borrower
     * The borrower is viewing the book
     */
    @Test
    public void testGetAugmentStatusbPendingBorrower()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.bPending);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Accepted);
    }

    /**
     * The book exchange has occured
     * The borrower now has the book
     */
    @Test
    public void testGetAugmentStatusBorrowed()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.Borrowed);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Borrowed);
    }

    /**
     * The book is being passed from borrower back to owner
     * The borrower is viewing the book
     */
    @Test
    public void testGetAugmentStatusRPendingBorrower()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.rPending);
        assertEquals(book.getAugmentStatus(testBorrower), Status.Available);
    }

    /**
     * The book is being passed from borrower back to owner
     * The owner is viewing the book
     */
    @Test
    public void testGetAugmentStatusRPendingOwner()
    {
        book.addRequester(testBorrower);
        book.setStatus(Status.rPending);
        assertEquals(book.getAugmentStatus("owner"), Status.Borrowed);
    }


}