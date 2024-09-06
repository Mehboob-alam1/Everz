package com.example.everz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private ProgressBar progressBar;
    private Button btnOpenEmail, btnResendEmail;

    private String userId, username, firstname, lastname, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        auth = FirebaseAuth.getInstance();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        progressBar = findViewById(R.id.progress_bar);
        btnOpenEmail = findViewById(R.id.btn_open_email);
        btnResendEmail = findViewById(R.id.btn_resend_email);

        // Retrieve data from Intent
        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        firstname = getIntent().getStringExtra("firstname");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");

        btnOpenEmail.setOnClickListener(v -> openEmailClient());

        btnResendEmail.setOnClickListener(v -> resendVerificationEmail());

        // Start checking for email verification
        checkEmailVerification();
    }

    private void checkEmailVerification() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("EmailVerification", "Reload successful");
                    if (currentUser.isEmailVerified()) {
                        Log.d("EmailVerification", "Email verified");
                        saveUserDataAndProceed();
                    } else {
                        Log.d("EmailVerification", "Email not verified yet");
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Please verify your email before proceeding.", Toast.LENGTH_SHORT).show();
                        // Periodically check email verification status
                        new Handler().postDelayed(this::checkEmailVerification, 3000); // Check every 3 seconds
                    }
                } else {
                    Log.e("EmailVerification", "Failed to reload user", task.getException());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to reload user. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("EmailVerification", "User is not logged in");
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEmailClient() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailVerificationActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendVerificationEmail() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            currentUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EmailVerificationActivity.this, "Verification email resent.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EmailVerificationActivity.this, "Failed to resend verification email.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EmailVerificationActivity.this, "User not found. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataAndProceed() {
        User user = new User(userId, username, firstname, lastname, email, phone, password, "token");
        userViewModel.saveUserData(user);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Email verified! Proceeding...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EmailVerificationActivity.this, SuccessActivity.class));
        finish();
    }
}
