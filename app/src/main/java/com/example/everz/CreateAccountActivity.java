package com.example.everz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.everz.databinding.ActivityCreateAccountBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.imgArrowBack.setOnClickListener(v -> finish());

        binding.btnCreateAccount.setOnClickListener(v -> {
            if (validateInputs()) {
                binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
                createAccount();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(binding.etFirstName.getText().toString())) {
            binding.etFirstName.setError("First name is required");
            binding.etFirstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.etLastName.getText().toString())) {
            binding.etLastName.setError("Last name is required");
            binding.etLastName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.etUserName.getText().toString())) {
            binding.etUserName.setError("User name is required");
            binding.etUserName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.etPhoneNumber.getText().toString())) {
            binding.etPhoneNumber.setError("Phone number is required");
            binding.etPhoneNumber.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
            binding.etPassword.setError("Password is required");
            return false;
        } else if (!isPasswordValid(binding.etPassword.getText().toString().trim())) {
            binding.etPassword.setError("Password should be at least 8 characters");
            return false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError("Email is required");
            return false;
        } else if (!isEmailValid(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError("Enter a valid email address");
            return false;
        } else if (binding.etPhoneNumber.getText().length() > 11) {
            binding.etPhoneNumber.setError("Invalid phone number");
            binding.etPhoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    private void createAccount() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String firstname = binding.etFirstName.getText().toString().trim();
        String lastname = binding.etLastName.getText().toString().trim();
        String username = binding.etUserName.getText().toString().trim();
        String phone = binding.etPhoneNumber.getText().toString().trim();

        authViewModel.signUp(email, password).observe(this, authResult -> {
            if (authResult != null && authResult.getUser() != null) {
                // Send email verification
                sendVerificationEmail(authResult.getUser().getUid(), username, firstname, lastname, email, phone, password);
            } else {
                // Handle signup failure
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationEmail(String userId, String username, String firstname, String lastname, String email, String phone, String password) {
        authViewModel.verifyEmailIdSentEmail(authViewModel.getCurrentUser().getValue()).observe(this, emailSent -> {
            if (emailSent) {
                Toast.makeText(this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show();
                openEmailClient();  // Open the email client

                // Start EmailSentActivity and pass the required data
                Intent intent = new Intent(CreateAccountActivity.this, EmailVerificationActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("username", username);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                startActivity(intent);

                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                finish();
            } else {
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkEmailVerification(String userId, String username, String firstname, String lastname, String email, String phone, String password) {
        authViewModel.checkIfEmailVerified(authViewModel.getCurrentUser().getValue()).observe(this, emailVerified -> {
            if (emailVerified) {
                saveUserDataAndProceed(userId, username, firstname, lastname, email, phone, password);
            } else {
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                Toast.makeText(this, "Please verify your email before proceeding.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openEmailClient() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateAccountActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserDataAndProceed(String userId, String username, String firstname, String lastname, String email, String phone, String password) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                User user = new User(userId, username, firstname, lastname, email, phone, password, token);
                userViewModel.saveUserData(user);
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                startActivity(new Intent(CreateAccountActivity.this, SuccessActivity.class));
                finish();
            } else {
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                Toast.makeText(this, "Failed to retrieve FCM token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
