package com.example.everz;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.viewpager.widget.PagerAdapter;

import com.example.everz.R;
import com.example.everz.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PagerAdapter adapter;
    private boolean doublePressToExit = false;

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {


            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                if (id == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                    getSupportActionBar().setTitle("Home");

                    return true;

                } else if (id == R.id.nav_services) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ServicesFragment()).commit();
                    getSupportActionBar().setTitle("Services");

                    return true;
                } else if (id == R.id.nav_about) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AboutFragment()).commit();
                    getSupportActionBar().setTitle("About");

                    return true;
                } else if (id == R.id.nav_contact) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ContactFragment()).commit();
                    getSupportActionBar().setTitle("Contact");

                    return true;
                } else if (id == R.id.nav_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ProfileFragment()).commit();
                    getSupportActionBar().setTitle("Profile");

                    return true;
                }
                return true;
            }

        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {

        if (doublePressToExit) {
            super.onBackPressed();


        }
        doublePressToExit = true;
        Toast.makeText(this, "Please click BACK again to exit !", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doublePressToExit = false, 2000);


    }
}