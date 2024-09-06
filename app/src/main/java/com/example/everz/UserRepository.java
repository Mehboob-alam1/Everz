package com.example.everz;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserRepository {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> ifUpload = new MutableLiveData<>();


    public UserRepository() {
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void saveUserData(User user) {

        databaseReference.child("users")
                .child(user.getUserId())
                .setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {

                    } else {

                    }
                }).addOnFailureListener(e -> {

                });


    }

}
