/**
 * CreateAccountActivity.java
 *
 * Activity for creating account. Requires all fields filled in,
 * matching passwords and password length greater than 6
 * characters to successfully create an account. Cannot overwrite
 * an account username or email that already exists. After account
 * creation, it automatically opens MyBooksActivity.
 */

package com.example.bookwormadventuresdeluxe2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookwormadventuresdeluxe2.Utilities.EditTextValidator;
import com.example.bookwormadventuresdeluxe2.Utilities.UserCredentialAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity implements FirebaseUserGetSet.UsernameTakenCallback
{
    private static final String TAG = "CreateAccountActivity";
    private Button createAccountButton;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private EditText confirmPassword;
    private ProgressBar progressBar;
    private ImageButton backButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseUser currentUser;
    private Boolean usernameInUse;

    /* FireStore Connection */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        TextView appHeaderTitle = findViewById(R.id.app_header_title);
        appHeaderTitle.setText(R.string.create_account);

        collectionReference = db.collection(getString(R.string.users_collection));
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                currentUser = firebaseAuth.getCurrentUser();
            }
        };

        createAccountButton = (Button) findViewById(R.id.create_acount_button_confirm);
        backButton = findViewById(R.id.app_header_back_button);
        editTextUsername = findViewById(R.id.create_username);
        editTextEmail = findViewById(R.id.create_email);
        editTextPhoneNumber = findViewById(R.id.create_phone_number);
        editTextPassword = findViewById(R.id.create_password);
        confirmPassword = findViewById(R.id.confirm_password);
        progressBar = findViewById(R.id.create_account_progressBar);

        /* Back Button Click Listener */
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this::onBackClick);

        /* Set click listener to create account*/
        createAccountButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Boolean noEmpties = true;

                /* Set Empty EditText Error codes */
                if (TextUtils.isEmpty(confirmPassword.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(confirmPassword);
                    noEmpties = false;
                }
                if (TextUtils.isEmpty(editTextPassword.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(editTextPassword);
                    noEmpties = false;
                }
                if (TextUtils.isEmpty(editTextPhoneNumber.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(editTextPhoneNumber);
                    noEmpties = false;
                }
                if (TextUtils.isEmpty(editTextEmail.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(editTextEmail);
                    noEmpties = false;
                }
                if (TextUtils.isEmpty(editTextUsername.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(editTextUsername);
                    noEmpties = false;
                }
                if (noEmpties)
                {
                    /* Check is passwords match and display error if not*/
                    if (EditTextValidator.passwordsMatch(editTextPassword, confirmPassword)
                            && !EditTextValidator.weakPass(editTextPassword, confirmPassword))
                    {
                        /* Show progress bar */
                        progressBar.setVisibility(View.VISIBLE);

                        /* Checks is username is available and passes it to createUser for errors*/
                        checkUsernameAvailability(editTextUsername.getText().toString().trim(),
                                new FirebaseUserGetSet.UsernameTakenCallback()
                                {
                                    @Override
                                    public void onUsernameTakenCallback(Boolean result)
                                    {
                                        usernameInUse = result;

                                        createUser(editTextUsername.getText().toString().trim(),
                                                editTextEmail.getText().toString().trim(),
                                                editTextPassword.getText().toString());
                                    }
                                });
                    }
                }
            }
        });
    }

    /**
     * Take user to login screen on back click
     *
     * @param view
     */
    private void onBackClick(View view)
    {
        super.onBackPressed();
    }

    /**
     * Check if username is available
     * On Success create user
     * On Failure add error to username editText
     * Source: https://stackoverflow.com/questions/48570270/firestore-query-checking-if-username-already-exists
     *
     * @param username Username requiring availability check
     * @param result Result of query callback
     */
    public void checkUsernameAvailability(String username, FirebaseUserGetSet.UsernameTakenCallback result)
    {
        /* Query to find username match*/
        Query userNameQuery = collectionReference.whereEqualTo("username", username);
        userNameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        /* Return result if username found */
                        result.onUsernameTakenCallback(true);
                    }
                }
                if (task.getResult().size() == 0)
                {
                    /* Return result if username not found*/
                    result.onUsernameTakenCallback(false);
                }
             }
        });
    }

    /**
     * Attempt to create an user
     * Take to MyBooksActivity on success
     * Show error message on failure
     * ProgressBar visibility set to Invisible inside nested calls due to asynchronous firebase methods
     *
     * @param username Username to be created
     * @param email Email to be created
     * @param password Password to be created
     */
    public void createUser(String username, String email, String password)
    {
        /* Create Firebase User */
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            /* Successful create account*/
                            if (!usernameInUse)
                            {
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;

                                /* Store User credentials in the global API*/
                                UserCredentialAPI userCredentialAPI = UserCredentialAPI.getInstance();
                                userCredentialAPI.setUserId(currentUser.getUid());
                                userCredentialAPI.setUsername(username);

                                createFirebaseAccount(currentUser.getUid());

                                /* Take user to My Books Activity */
                                Intent intent = new Intent(CreateAccountActivity.this,
                                        MyBooksActivity.class);
                                startActivity(intent);
                            }
                            else if (usernameInUse)
                            {
                                /* Delete FirebaseAuth account if username was taken*/
                                currentUser.delete();
                                EditTextValidator.usernameTaken(editTextUsername);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        else
                        {
                            try
                            {
                                /* Extract Firebase Error Code */
                                String errorCode = "";
                                errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode)
                                {
                                    /* Non-email string error */
                                    case "ERROR_INVALID_EMAIL":
                                        EditTextValidator.invalidEmail(editTextEmail);
                                        break;

                                    /* Taken email error */
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        EditTextValidator.emailTaken(editTextEmail);
                                        break;

                                    default:
                                        /* Unexpected Error code*/
                                        throw new Exception("Unexpected Firebase Error Code"
                                                + "inside click listener.");
                                }
                                if(usernameInUse)
                                {
                                    EditTextValidator.usernameTaken(editTextUsername);
                                }
                            } catch (Exception e)
                            {
                                /* Different type from errorCode, cannot be cast to the same object.
                                 * Sets EditText error to new type.
                                 *
                                 * Log message to debug
                                 */
                                editTextEmail.setError(task.getException().getMessage());
                                editTextEmail.requestFocus();
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }
                });

        /* Hide progress bar*/
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Creates Firebase database account for user with their information
     *
     * @param userId taken from FirebaseAuth instance
     */
    public void createFirebaseAccount(String userId)
    {
        /* Create new User object with credentials */
        Map<String, String> newUser = new HashMap<>();
        newUser.put("userId", userId);
        newUser.put("email", editTextEmail.getText().toString().trim());
        newUser.put("username", editTextUsername.getText().toString().trim());
        newUser.put("phoneNumber", editTextPassword.getText().toString());

        /* Save new user to Firestore */
        collectionReference.add(newUser);
    }

    /**
     * Set Current User and attach Auth state listener on Start of Activity
     */
    @Override
    protected void onStart()
    {
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();
    }

    /**
     * Detach Auth State Listener on idle Activity
     */
    @Override
    protected void onPause()
    {
        if (firebaseAuth != null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        super.onPause();
    }

    /**
     * Callback for username check, to time with email taken check
     *
     * @param result existence of username in database
     */
    @Override
    public void onUsernameTakenCallback(Boolean result)
    {

    }
}