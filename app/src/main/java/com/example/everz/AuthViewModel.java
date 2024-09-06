package com.example.everz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private LiveData<FirebaseUser> firebaseUserLiveData;


    public AuthViewModel(@NonNull Application application) {
        super(application);
        authenticationRepository= new AuthenticationRepository();
      firebaseUserLiveData=  authenticationRepository.getCurrentUser();

    }

    public LiveData<AuthResult> signIn(String email,String password){
       return authenticationRepository.signIn(email,password);
    }

    public LiveData<AuthResult> signUp(String email,String password){
     return    authenticationRepository.signUp(email,password);
    }

    public LiveData<FirebaseUser> getFirebaseUserLiveData() {
        return firebaseUserLiveData;
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return authenticationRepository.getCurrentUser();
    }

    public LiveData<Boolean> verifyEmailIdSentEmail(FirebaseUser user){
        return authenticationRepository.verifyEmailIdSentEmail(user);
    }

    public LiveData<Boolean> checkIfEmailVerified(FirebaseUser user){
        return authenticationRepository.checkIfEmailVerified(user);
    }
}
