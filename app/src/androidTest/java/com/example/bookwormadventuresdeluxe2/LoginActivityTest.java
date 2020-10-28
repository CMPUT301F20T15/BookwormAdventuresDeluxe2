package com.example.bookwormadventuresdeluxe2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;



import org.junit.runner.RunWith;

import android.content.res.Resources;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest
    {
        private Solo solo;
        private String TAG = "LoginActivityTest";

        Resources r;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private CollectionReference colRef = db.collection(r.getString(R.string.users_collection));

        public void createTestAccount()
        {
            firebaseAuth.createUserWithEmailAndPassword(r.getString(R.string.test_account_email), r.getString(R.string.test_account_password));
        }

        public void deleteTestAccount()
        {

        }
    }
