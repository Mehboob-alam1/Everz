package com.example.everz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationRepository {
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<Boolean> emailSent;
    private MutableLiveData<Boolean> emailVerified;

    public AuthenticationRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        emailSent = new MutableLiveData<>();
        emailVerified = new MutableLiveData<>();
    }


    public LiveData<FirebaseUser> getCurrentUser() {
        MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
        currentUser.setValue(firebaseAuth.getCurrentUser());
        return currentUser;

    }

    public LiveData<AuthResult> signIn(String email, String password) {
        MutableLiveData<AuthResult> signInResult = new MutableLiveData<>();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        signInResult.setValue(task.getResult());
                    } else {
                        signInResult.setValue(null);
                    }
                }).addOnFailureListener(e -> {
                    signInResult.setValue(null);
                });
        return signInResult;
    }


    // Add other authentication methods as needed


    public LiveData<AuthResult> signUp(String email, String password) {
        MutableLiveData<AuthResult> signUpResult = new MutableLiveData<>();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> signUpResult.setValue(task.getResult()));

        return signUpResult;
    }

    public LiveData<Boolean> verifyEmailIdSentEmail(FirebaseUser FirebaseUser) {

        FirebaseUser.sendEmailVerification()

                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        emailSent.setValue(true);

                    } else {
                        emailSent.setValue(false);
                    }

                }).addOnCanceledListener(() -> emailSent.setValue(false));

        return emailSent;

    }

    public LiveData<Boolean>  checkIfEmailVerified(FirebaseUser FirebaseUser) {

        if (FirebaseUser != null) {



             emailVerified.setValue(FirebaseUser.isEmailVerified());


            return emailVerified;

        }
        emailVerified.setValue(false);

        return emailVerified;

    }
}
