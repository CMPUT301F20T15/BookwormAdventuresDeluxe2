/**
 * CreateAccountActivityTest.java
 *
 * Android tests for CreateAccountActivity, tests multiple
 * input combinations and buttons. Also tests successfully
 * creating an account.
 */

package com.example.bookwormadventuresdeluxe2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookwormadventuresdeluxe2.Utilities.EditTextValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Tests for create account screen
 */
@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest
{
    private Solo solo;

    private Context appContext;
    private Resources r;

    private EditText usernameText;
    private EditText emailText;
    private EditText phoneNumberText;
    private EditText password1Text;
    private EditText password2Text;

    private FirebaseAuth fbAuth;
    private FirebaseFirestore fb;
    private CollectionReference colRef;

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule =
            new ActivityTestRule<>(CreateAccountActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(),rule.getActivity());

        /* Gets context for app resources */
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        /* Gets resource files */
        r = appContext.getResources();

        usernameText = (EditText) solo.getView(R.id.create_username);
        emailText = (EditText) solo.getView(R.id.create_email);
        phoneNumberText = (EditText) solo.getView(R.id.create_phone_number);
        password1Text = (EditText) solo.getView(R.id.create_password);
        password2Text = (EditText) solo.getView(R.id.confirm_password);

        fb = FirebaseFirestore.getInstance();
        colRef = fb.collection(r.getString(R.string.users_collection));
    }

    /**
     * Gets the activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests create account button functionality with no input
     */
    @Test
    public void emptyCreateAccountTest()
    {
        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.EMPTY));

        Assert.assertNotNull(usernameText.getError());
        Assert.assertNotNull(emailText.getError());
        Assert.assertNotNull(phoneNumberText.getError());
        Assert.assertNotNull(password1Text.getError());
        Assert.assertNotNull(password2Text.getError());
    }

    /**
     * Tests create account button functionality with spaces input
     */
    @Test
    public void spacesCreateAccountTest()
    {
        solo.enterText(usernameText, r.getString(R.string.space));
        solo.enterText(emailText, r.getString(R.string.space));
        solo.enterText(phoneNumberText, r.getString(R.string.space));
        solo.enterText(password1Text, r.getString(R.string.space));
        solo.enterText(password2Text, r.getString(R.string.space));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.EMPTY));

        Assert.assertNotNull(usernameText.getError());
        Assert.assertNotNull(emailText.getError());
        Assert.assertNotNull(phoneNumberText.getError());
        Assert.assertNotNull(password1Text.getError());
        Assert.assertNotNull(password2Text.getError());
    }

    /**
     * Tests create account button with a taken username
     */
    @Test
    public void takenUsernameTest()
    {
        solo.enterText(usernameText, r.getString(R.string.test_account1_username));

        solo.enterText(emailText, r.getString(R.string.test_create_account_email));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));
        solo.enterText(password1Text, r.getString(R.string.test_account1_password));
        solo.enterText(password2Text, r.getString(R.string.test_account1_password));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.USERNAMETAKEN));

        Assert.assertNotNull(usernameText.getError());
    }

    /**
     * Tests create account button with a taken email
     */
    @Test
    public void takenEmailTest()
    {
        solo.enterText(emailText, r.getString(R.string.test_account1_email));

        solo.enterText(usernameText, r.getString(R.string.test_create_account_username));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));
        solo.enterText(password1Text, r.getString(R.string.test_account1_password));
        solo.enterText(password2Text, r.getString(R.string.test_account1_password));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.EMAILTAKEN));

        Assert.assertNotNull(emailText.getError());
    }

    /**
     * Tests create account button for a non-email string
     */
    @Test
    public void invalidEmailTest()
    {
        solo.enterText(emailText, r.getString(R.string.wrong_email));

        solo.enterText(usernameText, r.getString(R.string.test_create_account_username));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));
        solo.enterText(password1Text, r.getString(R.string.test_account1_password));
        solo.enterText(password2Text, r.getString(R.string.test_account1_password));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.INVALIDEMAIL));

        Assert.assertNotNull(emailText.getError());
    }

    /**
     * Tests create account button with non-matching passwords
     */
    @Test
    public void nonMatchingPasswordsTest()
    {
        solo.enterText(password2Text, r.getString(R.string.wrong_pass));

        solo.enterText(emailText, r.getString(R.string.test_create_account_email));
        solo.enterText(usernameText, r.getString(R.string.test_create_account_username));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));
        solo.enterText(password1Text, r.getString(R.string.test_account1_password));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.PASSWORDSDONTMATCH));

        Assert.assertNotNull(password1Text.getError());
        Assert.assertNotNull(password2Text.getError());
    }

    /**
     * Tests create account button with password less than 6 characters
     */
    @Test public void shortPasswordTest()
    {
        solo.enterText(password1Text, r.getString(R.string.short_pass));
        solo.enterText(password2Text, r.getString(R.string.short_pass));

        solo.enterText(emailText, r.getString(R.string.test_create_account_email));
        solo.enterText(usernameText, r.getString(R.string.test_create_account_username));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));

        solo.clickOnButton(r.getString(R.string.create_account));

        Assert.assertTrue(solo.waitForText(EditTextValidator.WEAKPASS));

        Assert.assertNotNull(password1Text.getError());
        Assert.assertNotNull(password2Text.getError());
    }

    /**
     * Tests successful account creation and signs out
     */
    @Test
    public void createAccountTest()
    {
        solo.enterText(emailText, r.getString(R.string.test_create_account_email));
        solo.enterText(usernameText, r.getString(R.string.test_create_account_username));

        solo.enterText(phoneNumberText, r.getString(R.string.test_account1_phone));
        solo.enterText(password1Text, r.getString(R.string.test_account1_password));
        solo.enterText(password2Text, r.getString(R.string.test_account1_password));

        solo.clickOnButton(r.getString(R.string.create_account));

        solo.waitForText(r.getString(R.string.navbar_text_label_4));

        solo.assertCurrentActivity(r.getString(R.string.wrong_activity), MyBooksActivity.class);

        signOut();
    }

    /**
     * Signs out of created test account after deleting account
     */
    public void signOut()
    {
        solo.clickOnText(r.getString(R.string.navbar_text_label_4));

        solo.waitForText(r.getString(R.string.sign_out));

        deleteCreateTestAccount();

        solo.clickOnButton(r.getString(R.string.sign_out));
    }

    /**
     * Deletes created tests account in FirebaseAuth and Firebase database
     */
    public void deleteCreateTestAccount()
    {
        colRef.whereEqualTo("username", r.getString(R.string.test_create_account_username))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                colRef.document(document.getId()).delete();
                                fbAuth = FirebaseAuth.getInstance();
                                fbAuth.getInstance().getCurrentUser().delete();
                            }
                        }
                    }
                });
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
