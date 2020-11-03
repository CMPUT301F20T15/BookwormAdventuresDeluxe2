package com.example.bookwormadventuresdeluxe2;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.example.bookwormadventuresdeluxe2.TestUtils.signIn;
import static com.example.bookwormadventuresdeluxe2.TestUtils.signOut;

@RunWith(AndroidJUnit4.class)
public class UploadPhotoTests
{
    private Solo solo;

    private Context appContext;
    private Resources resources;

    private EditText titleText;
    private EditText authorText;
    private EditText isbnText;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        /* Gets context for app resources */
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        /* Gets resource files */
        resources = appContext.getResources();

        /* Sign into the app */
        signIn(solo, resources);

        titleText = (EditText) solo.getView(R.id.book_details_title);
        authorText = (EditText) solo.getView(R.id.book_details_author);
        isbnText = (EditText) solo.getView(R.id.book_details_isbn);
    }

    @Test
    public void uploadPhotoOnNewBook()
    {
//        View v = View.inflate(rule.getActivity(), R.layout.fragment_my_books, null);
//        View fab = v.findViewById(R.id.my_books_add_button);
//        solo.clickOnView(fab);

//        solo.getView(R.id.my_books_add_button).callOnClick();

//        solo.enterText(titleText, "Test Title");
//        solo.enterText(authorText, "Test Author");
//        solo.enterText(isbnText, "11111111111");
//
//        solo.clickOnButton("Upload");
    }

    /**
     * Closes the activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
        signOut(solo, resources);
        solo.finishOpenedActivities();
    }
}
