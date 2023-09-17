package com.example.bloodbankprojecthit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bloodbankprojecthit.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.container.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, binding.drawerLayout, binding.container.toolbar, R.string.closeDrawer, R.string.openDrawer);
        //noinspection deprecation
        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(MainActivity.this);

        // Bottom Navigation
        binding.container.content.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bottom_nav_home) {
                loadFragment(new Home());
                // Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.home), Toast.LENGTH_SHORT).show();
            } else if (id == R.id.bottom_nav_donate) {
                Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.donate), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Bottom " + getString(R.string.request), Toast.LENGTH_SHORT).show();
            }

            return false;
        });
        binding.container.content.bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);


        // Firebase
        database = FirebaseDatabase.getInstance();
        auth =  FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        binding.container.content.txtName.setText(user.getDisplayName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_drawer_map_view) {
            Toast.makeText(this, R.string.map_view, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_profile) {
            loadFragment(new MyProfile());
        } else if (id == R.id.nav_drawer_requests) {
            Toast.makeText(this, R.string.requests, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_hospital_service) {
            Toast.makeText(this, R.string.hospital_service, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_blood_bank_service) {
            Toast.makeText(this, R.string.blood_bank_service, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_drawer_logout) {
            auth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_about_us) {
            loadFragment(new AboutUs());
        } else {
            Toast.makeText(this, R.string.rate_us, Toast.LENGTH_SHORT).show();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}