package com.example.everz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.everz.databinding.ActivitySuccessBinding;


public class SuccessActivity extends AppCompatActivity {
    private ActivitySuccessBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(SuccessActivity.this, LoginActivity.class));
            finishAffinity();
        });

    }
}