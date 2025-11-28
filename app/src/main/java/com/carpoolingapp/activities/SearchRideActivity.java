package com.carpoolingapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.carpoolingapp.R;
import com.carpoolingapp.adapters.RideAdapter;
import com.carpoolingapp.models.Ride;
import com.carpoolingapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class SearchRideActivity extends AppCompatActivity {

    private RecyclerView ridesRecyclerView;
    private View emptyState;
    private TextView emptyText;
    private RideAdapter adapter;
    private List<Ride> rideList;
    private FirebaseHelper firebaseHelper;

    private String searchFrom, searchTo, searchDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);

        // Get search parameters
        searchFrom = getIntent().getStringExtra("from");
        searchTo = getIntent().getStringExtra("to");
        searchDate = getIntent().getStringExtra("date");

        initViews();
        setupToolbar();
        setupRecyclerView();
        searchRides();
    }

    private void initViews() {
        ridesRecyclerView = findViewById(R.id.ridesRecyclerView);
        emptyState = findViewById(R.id.emptyState);
        emptyText = findViewById(R.id.emptyText);
        firebaseHelper = FirebaseHelper.getInstance();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search Results");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rideList = new ArrayList<>();
        adapter = new RideAdapter(this, rideList, ride -> {
            Toast.makeText(this, "Ride from " + ride.getFromLocation() + " to " + ride.getToLocation(), Toast.LENGTH_SHORT).show();
        });
        ridesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ridesRecyclerView.setAdapter(adapter);
    }

    private void searchRides() {
        firebaseHelper.getRidesRef()
                .orderByChild("status")
                .equalTo("active")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rideList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ride ride = snapshot.getValue(Ride.class);
                            if (ride != null) {
                                ride.setRideId(snapshot.getKey());

                                // ONLY show rides that are "hosting" type
                                if (!"hosting".equals(ride.getRideType())) {
                                    continue;
                                }

                                // Filter by location and date
                                boolean matchesFrom = searchFrom == null || ride.getFromLocation().toLowerCase().contains(searchFrom.toLowerCase());
                                boolean matchesTo = searchTo == null || ride.getToLocation().toLowerCase().contains(searchTo.toLowerCase());
                                boolean matchesDate = searchDate == null || ride.getDate().equals(searchDate);

                                if (matchesFrom && matchesTo && matchesDate) {
                                    rideList.add(ride);
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (rideList.isEmpty()) {
                            emptyState.setVisibility(View.VISIBLE);
                            ridesRecyclerView.setVisibility(View.GONE);
                            emptyText.setText("No rides found matching your search");
                        } else {
                            emptyState.setVisibility(View.GONE);
                            ridesRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SearchRideActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}