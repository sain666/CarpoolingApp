package com.carpoolingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.carpoolingapp.R;
import com.carpoolingapp.adapters.RideAdapter;
import com.carpoolingapp.adapters.RideRequestAdapter;
import com.carpoolingapp.models.Ride;
import com.carpoolingapp.utils.FirebaseHelper;
import com.carpoolingapp.utils.SharedPrefsHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView userNameText;
    private MaterialButton myActiveListingsButton, myRideRequestsButton;
    private RecyclerView recyclerView;
    private View emptyState, searchCard;
    private BottomNavigationView bottomNav;

    private FirebaseHelper firebaseHelper;
    private SharedPrefsHelper prefsHelper;
    private RideAdapter rideAdapter;
    private RideRequestAdapter rideRequestAdapter;
    private List<Ride> activeListingsList;
    private List<Ride> rideRequestsList;
    private List<Ride> hostedRidesList = new ArrayList<>();
    private List<Ride> bookedRidesList = new ArrayList<>();

    private boolean isListingsMode = true; // true = My Active Listings, false = My Ride Requests
    private static final List<String> DATE_PATTERNS = Arrays.asList("MMM dd, yyyy", "yyyy-MM-dd", "dd/MM/yyyy");
    private static final List<String> TIME_PATTERNS = Arrays.asList("hh:mm a", "HH:mm", "HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initFirebase();
        setupListeners();
        setupBottomNav();
        loadUserData();
        setupRecyclerView();
        loadData();
        updateModeUI();
    }

    private void initViews() {
        userNameText = findViewById(R.id.userNameText);
        myActiveListingsButton = findViewById(R.id.myActiveListingsButton);
        myRideRequestsButton = findViewById(R.id.myRideRequestsButton);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNav = findViewById(R.id.bottomNav);
        searchCard = findViewById(R.id.searchView);
        emptyState = findViewById(R.id.emptyState);
    }

    private void initFirebase() {
        firebaseHelper = FirebaseHelper.getInstance();
        prefsHelper = new SharedPrefsHelper(this);
    }

    private void setupListeners() {
        myActiveListingsButton.setOnClickListener(v -> setActiveListingsMode());
        myRideRequestsButton.setOnClickListener(v -> setRideRequestsMode());

        searchCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SearchFormActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.profileImage).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_create) {
                startActivity(new Intent(HomeActivity.this, CreateRideActivity.class));
                return true;
            } else if (itemId == R.id.nav_messages) {
                startActivity(new Intent(HomeActivity.this, MessagesActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadUserData() {
        userNameText.setText(prefsHelper.getUserName());
    }

    private void setupRecyclerView() {
        activeListingsList = new ArrayList<>();
        rideRequestsList = new ArrayList<>();

        rideAdapter = new RideAdapter(this, activeListingsList, ride -> {
            Intent intent = new Intent(this, RideDetailActivity.class);
            intent.putExtra("rideId", ride.getRideId());
            startActivity(intent);
        });

        rideRequestAdapter = new RideRequestAdapter(this, rideRequestsList, ride -> {
            Intent intent = new Intent(this, RideDetailActivity.class);
            intent.putExtra("rideId", ride.getRideId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setActiveListingsMode() {
        isListingsMode = true;
        updateModeUI();
        loadData();
    }

    private void setRideRequestsMode() {
        isListingsMode = false;
        updateModeUI();
        loadData();
    }

    private void updateModeUI() {
        if (isListingsMode) {
            myActiveListingsButton.setBackgroundTintList(getColorStateList(R.color.status_active));
            myActiveListingsButton.setTextColor(getColor(R.color.white));
            myRideRequestsButton.setBackgroundTintList(getColorStateList(R.color.status_inactive));
            myRideRequestsButton.setTextColor(getColor(R.color.primary_blue));
        } else {
            myRideRequestsButton.setBackgroundTintList(getColorStateList(R.color.status_active));
            myRideRequestsButton.setTextColor(getColor(R.color.white));
            myActiveListingsButton.setBackgroundTintList(getColorStateList(R.color.status_inactive));
            myActiveListingsButton.setTextColor(getColor(R.color.primary_blue));
        }
    }

    private void loadData() {
        String userId = prefsHelper.getUserId();
        if (userId == null) return;

        if (isListingsMode) {
            loadMyActiveListings(userId);
        } else {
            loadMyRideRequests(userId);
        }
    }

    private void loadMyActiveListings(String userId) {
        recyclerView.setAdapter(rideAdapter);

        // Load rides the user is hosting
        firebaseHelper.getRidesRef()
                .orderByChild("driverId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hostedRidesList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ride ride = snapshot.getValue(Ride.class);
                            if (ride != null && "hosting".equals(ride.getRideType())) {
                                ride.setRideId(snapshot.getKey());
                                hostedRidesList.add(ride);
                            }
                        }
                        combineAndDisplayActiveListings();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(HomeActivity.this, "Failed to load hosted rides", Toast.LENGTH_SHORT).show();
                    }
                });

        // Load rides the user has booked (where the current user is the rider)
        // Bookings are stored with a "riderId" field in Booking.java, so we must query by "riderId"
        firebaseHelper.getBookingsRef().orderByChild("riderId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> rideIds = new ArrayList<>();
                        for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                            String rideId = bookingSnapshot.child("rideId").getValue(String.class);
                            if (rideId != null) {
                                rideIds.add(rideId);
                            }
                        }
                        fetchBookedRides(rideIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(HomeActivity.this, "Failed to load booked rides", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchBookedRides(List<String> rideIds) {
        bookedRidesList.clear();
        if (rideIds.isEmpty()) {
            combineAndDisplayActiveListings();
            return;
        }

        final int[] ridesToFetch = {rideIds.size()};

        for (String rideId : rideIds) {
            firebaseHelper.getRideRef(rideId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot rideSnapshot) {
                    if (rideSnapshot.exists()) {
                        Ride ride = rideSnapshot.getValue(Ride.class);
                        if (ride != null) {
                            ride.setRideId(rideSnapshot.getKey());
                            bookedRidesList.add(ride);
                        }
                    }
                    ridesToFetch[0]--;
                    if (ridesToFetch[0] == 0) {
                        combineAndDisplayActiveListings();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ridesToFetch[0]--;
                    if (ridesToFetch[0] == 0) {
                        combineAndDisplayActiveListings();
                    }
                    Toast.makeText(HomeActivity.this, "Failed to load a booked ride", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void combineAndDisplayActiveListings() {
        activeListingsList.clear();
        activeListingsList.addAll(hostedRidesList);

        for (Ride bookedRide : bookedRidesList) {
            if (!isRideInList(activeListingsList, bookedRide.getRideId())) {
                activeListingsList.add(bookedRide);
            }
        }

        // sort by earliest
        Collections.sort(activeListingsList, (ride1, ride2) ->
                Long.compare(getRideTimestamp(ride1), getRideTimestamp(ride2)));

        rideAdapter.notifyDataSetChanged();
        updateEmptyState(activeListingsList.isEmpty());
    }

    private boolean isRideInList(List<Ride> list, String rideId) {
        for (Ride ride : list) {
            if (ride.getRideId().equals(rideId)) {
                return true;
            }
        }
        return false;
    }

    private long getRideTimestamp(Ride ride) {
        if (ride == null) {
            return Long.MIN_VALUE;
        }

        long parsedTimestamp = parseScheduledTimestamp(ride.getDate(), ride.getTime());
        if (parsedTimestamp != Long.MIN_VALUE) {
            return parsedTimestamp;
        }

        if (ride.getCreatedAt() > 0) {
            return ride.getCreatedAt();
        }

        if (ride.getUpdatedAt() > 0) {
            return ride.getUpdatedAt();
        }

        return Long.MIN_VALUE;
    }

    private long parseScheduledTimestamp(String dateValue, String timeValue) {
        if (dateValue == null || dateValue.trim().isEmpty()) {
            return Long.MIN_VALUE;
        }

        for (String datePattern : DATE_PATTERNS) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
            dateFormat.setLenient(false);
            try {
                Date parsedDate = dateFormat.parse(dateValue);
                if (parsedDate == null) continue;

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                boolean timeApplied = applyTime(calendar, timeValue);
                if (!timeApplied) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                }
                return calendar.getTimeInMillis();
            } catch (ParseException ignored) {
            }
        }

        return Long.MIN_VALUE;
    }

    private boolean applyTime(Calendar calendar, String timeValue) {
        if (timeValue == null || timeValue.trim().isEmpty()) {
            return false;
        }

        for (String timePattern : TIME_PATTERNS) {
            SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
            timeFormat.setLenient(false);
            try {
                Date parsedTime = timeFormat.parse(timeValue);
                if (parsedTime == null) continue;

                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(parsedTime);
                calendar.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                calendar.set(Calendar.MILLISECOND, 0);
                return true;
            } catch (ParseException ignored) {
            }
        }
        return false;
    }

    private void loadMyRideRequests(String userId) {
        recyclerView.setAdapter(rideRequestAdapter);

        firebaseHelper.getRidesRef()
                .orderByChild("driverId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rideRequestsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ride ride = snapshot.getValue(Ride.class);
                            if (ride != null && "request".equals(ride.getRideType())) {
                                ride.setRideId(snapshot.getKey());
                                rideRequestsList.add(ride);
                            }
                        }
                        rideRequestAdapter.notifyDataSetChanged();
                        updateEmptyState(rideRequestsList.isEmpty());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(HomeActivity.this, "Failed to load ride requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_home);
        loadData();
    }
}
