package com.carpoolingapp.activities;

// File: CarpoolingApp/app/src/main/java/com/carpooling/app/activities/LoginActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.carpoolingapp.R;
import com.carpoolingapp.utils.FirebaseHelper;
import com.carpoolingapp.utils.SharedPrefsHelper;
import com.carpoolingapp.utils.ValidationHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private View registerText;

    private FirebaseHelper firebaseHelper;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initFirebase();
        setupListeners();
    }

    private void initViews() {
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);
    }

    private void initFirebase() {
        firebaseHelper = FirebaseHelper.getInstance();
        prefsHelper = new SharedPrefsHelper(this);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        String emailError = ValidationHelper.getEmailError(email);
        String passwordError = ValidationHelper.getPasswordError(password);

        if (emailError != null) {
            emailInputLayout.setError(emailError);
            return;
        }
        emailInputLayout.setError(null);

        if (passwordError != null) {
            passwordInputLayout.setError(passwordError);
            return;
        }
        passwordInputLayout.setError(null);

        // Show loading
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Firebase authentication
        firebaseHelper.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseHelper.getCurrentUser();
                        if (user != null) {
                            firebaseHelper.getUserRef(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Object ratingObj = dataSnapshot.child("rating").getValue();
                                    Object totalRidesObj = dataSnapshot.child("totalRides").getValue();
                                    Object phoneObj = dataSnapshot.child("phone").getValue();

                                    String rating = (ratingObj != null) ? String.valueOf(ratingObj) : "0.0";
                                    String totalRides = (totalRidesObj != null) ? String.valueOf(totalRidesObj) : "0";
                                    String phone = (phoneObj != null) ? String.valueOf(phoneObj) : "000-000-0000";

                                    // save all user data at once
                                    prefsHelper.saveUserData(
                                            user.getUid(),
                                            user.getDisplayName() != null ? user.getDisplayName() : "User",
                                            user.getEmail(),
                                            rating,
                                            totalRides,
                                            phone
                                    );

                                    // Navigate to Home
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                // Log user in with blank stats if they don't exist
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    prefsHelper.saveUserData(
                                            user.getUid(),
                                            user.getDisplayName() != null ? user.getDisplayName() : "User",
                                            user.getEmail(),
                                            "0.0",
                                            "0",
                                            "000-000-0000"
                                    );
                                    // Navigate to Home
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } else {
                        loginButton.setEnabled(true);
                        loginButton.setText(R.string.login);
                        Toast.makeText(LoginActivity.this,
                                task.getException() != null ?
                                        task.getException().getMessage() :
                                        getString(R.string.error_login_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}