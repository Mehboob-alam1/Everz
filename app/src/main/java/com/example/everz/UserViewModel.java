package com.example.everz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository= new UserRepository();
    }

    public void saveUserData(User user){
        userRepository.saveUserData(user);
    }


}
