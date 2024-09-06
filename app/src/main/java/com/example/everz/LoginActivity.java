package com.example.everz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.everz.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;






public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
        });

        binding.btnSignIn.setOnClickListener(v -> {
            String password = binding.etPassword.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required");
            } else if (!isPasswordValid(password)) {
                binding.etPassword.setError("Password should be at least 8 characters");
            } else if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
            } else if (!isEmailValid(email)) {
                binding.etEmail.setError("Enter a valid email address");
            } else {
                signInWithEmailPassword(email, password);
            }
        });

        binding.btnForgotPassword.setOnClickListener(v -> {
            showRecoverPasswordDialog();

        });
    }
    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // write the email using which you registered
        emailet.setHint("Enter email to send a link");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(LoginActivity.this,"Done sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void signInWithEmailPassword(String email, String password) {
        binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
        authViewModel.signIn(email, password).observe(this, authResult -> {
            if (authResult != null) {
                FirebaseUser user = authResult.getUser();
                if (user != null) {
                    // Check if the email is verified
                    if (user.isEmailVerified()) {
                        updateUI();
                    } else {
                        // Redirect to EmailSentActivity if the email is not verified
                        redirectToEmailSentActivity(user);
                    }
                } else {
                    // Authentication failed, handle the error
                    binding.loadingLayout.getRoot().setVisibility(View.GONE);
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("AuthException", "Not authenticated yet");
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
            }
        });
    }

    private void redirectToEmailSentActivity(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
        intent.putExtra("userId", user.getUid());
        intent.putExtra("email", user.getEmail());
        // Add any additional data you want to pass (e.g., username, firstName, etc.)
        startActivity(intent);
        finish(); // Optional: Finish LoginActivity to prevent going back to it
    }

    private boolean isPasswordValid(String password) {
        // Password should be at least 8 characters
        return password.length() >= 8;
    }

    private boolean isEmailValid(String email) {
        // Simple regex for validating email address
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void updateUI() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        authViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                if (firebaseUser.isEmailVerified()) {
                    updateUI();
                } else {
                    // Redirect to EmailSentActivity if the email is not verified
                    redirectToEmailSentActivity(firebaseUser);
                }
            }
        });
    }
}
