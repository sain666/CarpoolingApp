package com.carpoolingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.carpoolingapp.R;
import com.carpoolingapp.utils.FirebaseHelper;
import com.carpoolingapp.utils.SharedPrefsHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameText, userEmailText, ratingText, numRidesTest;
    private TextView editProfileOption, changePasswordOption, paymentMethodsOption, rideHistoryOption;
    private MaterialButton logoutButton;
    private BottomNavigationView bottomNav;

    private FirebaseHelper firebaseHelper;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        initFirebase();
        setupToolbar();
        setupBottomNav();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        userNameText = findViewById(R.id.userNameText);
        userEmailText = findViewById(R.id.userEmailText);
        ratingText = findViewById(R.id.ratingText);
        numRidesTest = findViewById(R.id.numRidesText);
        editProfileOption = findViewById(R.id.editProfileOption);
        changePasswordOption = findViewById(R.id.changePasswordOption);
        paymentMethodsOption = findViewById(R.id.paymentMethodsOption);
        rideHistoryOption = findViewById(R.id.rideHistoryOption);
        logoutButton = findViewById(R.id.logoutButton);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void initFirebase() {
        firebaseHelper = FirebaseHelper.getInstance();
        prefsHelper = new SharedPrefsHelper(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                try {
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "Error opening Home", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return false;
                }
            } else if (itemId == R.id.nav_create) {
                try {
                    Intent intent = new Intent(ProfileActivity.this, CreateRideActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "Error opening Create Ride", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return false;
                }
            } else if (itemId == R.id.nav_messages) {
                try {
                    Intent intent = new Intent(ProfileActivity.this, MessagesActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "Error opening Messages", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return false;
                }
            } else if (itemId == R.id.nav_profile) {
                // Already on profile
                return true;
            }
            return false;
        });
    }

    private void loadUserData() {
        userNameText.setText(prefsHelper.getUserName());
        userEmailText.setText(prefsHelper.getUserEmail());
        ratingText.setText((prefsHelper.getUserRating().contains(".")) ? prefsHelper.getUserRating() : prefsHelper.getUserRating() + ".0" );
        numRidesTest.setText(prefsHelper.getUserTotalRides() + " rides");
    }

    private void setupListeners() {
        editProfileOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Edit Profile coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        changePasswordOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Change Password coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        paymentMethodsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Payment Methods coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        rideHistoryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Ride History coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        firebaseHelper.getAuth().signOut();
        prefsHelper.clearUserData();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
        }
    }
}