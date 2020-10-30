package com.example.bookwormadventuresdeluxe2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bookwormadventuresdeluxe2.Utilities.EditTextValidator;
import com.example.bookwormadventuresdeluxe2.Utilities.UserCredentialAPI;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements FirebaseUserGetSet.UserCallback
{
    private static final String TAG = "MyProfileFragment";

    Button edit;
    Button signOutButton;

    MaterialTextView appHeaderText;

    TextView viewUsername;
    TextView viewEmail;
    TextView viewPhoneNumber;

    View view;

    FirebaseAuth firebaseAuth;

    UserProfileObject user;

    public MyProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        user = (UserProfileObject) bundle.getSerializable("myProfile");

        /* Inflate the layout for this fragment */
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        /* Set title */
        appHeaderText = view.findViewById(R.id.app_header_title);
        appHeaderText.setText(R.string.my_profile_title);

        /* Buttons */
        signOutButton = view.findViewById(R.id.profile_logout);
        signOutButton.setOnClickListener(this::signOut);

        edit = view.findViewById(R.id.profile_edit);
        edit.setOnClickListener(this::editFragment);

        /* Set display texts */
        viewUsername = view.findViewById(R.id.view_username);
        viewEmail = view.findViewById(R.id.view_email);
        viewPhoneNumber = view.findViewById(R.id.view_phone);

        viewUsername.setText(user.getUsername());
        viewEmail.setText(user.getEmail());
        viewPhoneNumber.setText(user.getPhoneNumber());

        /* Theme for popup dialog fragment */
        getContext().getTheme().applyStyle(R.style.BlackTextTheme, true);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    /**
     * Callback for UserProfileObject
     */
    @Override
    public void onCallback(UserProfileObject userObject)
    {

    }

    /**
     * Dialog fragment for editing email and phone number info
     */
    public void editFragment(View view)
    {
        final View editInfo = LayoutInflater.from(this.getContext()).inflate(R.layout.edit_profile, null);

        /* Set up the input */
        EditText inputEmail = editInfo.findViewById(R.id.edit_email);
        EditText inputPhone = editInfo.findViewById(R.id.edit_phone);

        /* Specify the type of input expected */
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputPhone.setInputType(InputType.TYPE_CLASS_PHONE);

        /* Setting text to user's details */
        inputEmail.setText(user.getEmail());
        inputPhone.setText(user.getPhoneNumber());

        /* Create popup dialog for editing profile */
        final AlertDialog builder = new AlertDialog.Builder(this.getContext()).create();
        builder.setView(editInfo);

        /* Set up the buttons */
        editInfo.findViewById(R.id.edit_confirm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                inputEmail.setError(null);
                inputPhone.setError(null);
                boolean hasValidationError = false;

                /* Checks if no changes were made */
                if (user.getEmail().equals(inputEmail.getText().toString())
                        && user.getPhoneNumber().equals(inputPhone.getText().toString()))
                {
                    builder.dismiss();
                    return;
                }

                /* Checks if email was empty and sets error */
                if (TextUtils.isEmpty(inputEmail.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(inputEmail);
                    hasValidationError = true;
                }

                /* Checks if phone number was empty and sets error*/
                if (TextUtils.isEmpty(inputPhone.getText().toString().trim()))
                {
                    EditTextValidator.isEmpty(inputPhone);
                    hasValidationError = true;
                }

                /* Checks if error is present and disables confirm button */
                if ((inputEmail.getError() != null) || hasValidationError)
                {
                    return;
                }

                FirebaseUserGetSet.changeAuthInfo(inputEmail,
                        inputPhone,
                        user.getDocumentId());

                if (inputEmail.getError() == null)
                {
                    /* Updating user object in Fragment*/
                    user.setEmail(inputEmail.getText().toString().trim());
                    user.setPhoneNumber(inputPhone.getText().toString().trim());

                    /* Updating TextView in fragment */
                    viewEmail.setText(inputEmail.getText().toString().trim());
                    viewPhoneNumber.setText(inputPhone.getText().toString().trim());

                    /* Closing dialog */
                    builder.dismiss();
                }
            }
        });

        editInfo.findViewById(R.id.edit_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                builder.dismiss();
            }
        });

        builder.show();
    }

    /**
     * Signs out of FirebaseAuth account
     */
    public void signOut(View view)
    {
        /*
         * Listener for signOut button to sign user out of firebase account
         * Source : https://stackoverflow.com/questions/53334017/back-button-will-bring-to-home-page-after-firebase-logout-on-app
         * */
        if (firebaseAuth != null)
        {
            firebaseAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            /* Take User back to Login Page */
            startActivity(intent);
        }
    }
}
