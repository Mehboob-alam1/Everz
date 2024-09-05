package com.example.simurgh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {


    private DatabaseReference databaseReference;
    private TextView txtUserName,txtEmail,txtPhoneNumber,btnForgotPassword;
    private LinearLayout btnLogout;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        txtUserName=view.findViewById(R.id.txtUserName);
        mAuth=FirebaseAuth.getInstance();

        txtEmail=view.findViewById(R.id.txtEmail);
        txtPhoneNumber=view.findViewById(R.id.txtPhoneNumber);
        btnForgotPassword=view.findViewById(R.id.btnForgotPassword);
        btnLogout=view.findViewById(R.id.btnLogout);
 databaseReference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


 btnLogout.setOnClickListener(v -> {

     FirebaseAuth.getInstance().signOut();
     startActivity(new Intent(getContext(), LoginActivity.class));
     getActivity().finishAffinity();
 });

 btnForgotPassword.setOnClickListener(v -> {
     showRecoverPasswordDialog();
 });
 databaseReference.addValueEventListener(new ValueEventListener() {
     @Override
     public void onDataChange(@NonNull DataSnapshot snapshot) {
         if (snapshot.exists()){

           User user=  snapshot.getValue(User.class);
           txtUserName.setText(user.getUserName().toUpperCase());
             txtEmail.setText(user.getEmail());
             txtPhoneNumber.setText(user.getPhoneNumber());
         }
     }

     @Override
     public void onCancelled(@NonNull DatabaseError error) {

     }
 });
        return view;
    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(getContext());
        final EditText emailet= new EditText(getContext());

        // write the email using which you registered
        emailet.setHint("Enter email to send a link");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", (dialog, which) -> {
            String email=emailet.getText().toString().trim();
            beginRecovery(email);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(getContext());
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if(task.isSuccessful())
            {
                // if isSuccessful then done message will be shown
                // and you can change the password
                Toast.makeText(getContext(),"Done sent",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(),"Error Occurred",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            loadingBar.dismiss();
            Toast.makeText(getContext(),"Error Failed",Toast.LENGTH_LONG).show();
        });
    }
}