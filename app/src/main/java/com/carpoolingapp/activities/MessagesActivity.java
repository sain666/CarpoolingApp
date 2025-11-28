package com.carpoolingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.carpoolingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.View;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private BottomNavigationView bottomNav;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        initViews();
        setupToolbar();
        setupBottomNav();
        setupRecyclerView();

        // Show empty state by default
        showEmptyState();
    }

    private void initViews() {
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        bottomNav = findViewById(R.id.bottomNav);
        emptyState = findViewById(R.id.emptyState);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Messages");
        }
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_messages);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_create) {
                startActivity(new Intent(this, CreateRideActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_messages) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set up messages adapter when ready
    }

    private void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        messagesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_messages);
        }
    }
}