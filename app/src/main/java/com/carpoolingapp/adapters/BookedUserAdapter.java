package com.carpoolingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carpoolingapp.R;
import com.carpoolingapp.models.Booking;
import com.carpoolingapp.models.User;
import com.carpoolingapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookedUserAdapter extends RecyclerView.Adapter<BookedUserAdapter.UserViewHolder> {

    private final List<Booking> bookings;
    private final Context context;
    private final OnUserActionListener listener;

    public interface OnUserActionListener {
        void onMessageClick(Booking booking);
        void onKickClick(Booking booking);
    }

    public BookedUserAdapter(List<Booking> bookings, Context context, OnUserActionListener listener) {
        this.bookings = bookings;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking, listener);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView userNameText;
        TextView ratingText;
        TextView seatsText;
        com.google.android.material.button.MaterialButton messageButton;
        com.google.android.material.button.MaterialButton kickButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userNameText = itemView.findViewById(R.id.userNameText);
            ratingText = itemView.findViewById(R.id.ratingText);
            seatsText = itemView.findViewById(R.id.seatsText);
            messageButton = itemView.findViewById(R.id.messageButton);
            kickButton = itemView.findViewById(R.id.kickButton);
        }

        void bind(Booking booking, OnUserActionListener listener) {
            // Set user name
            userNameText.setText(booking.getRiderName());

            // Set seats
            int seats = booking.getSeatsBooked();
            seatsText.setText(seats + (seats == 1 ? " seat" : " seats"));

            // Load user profile and rating
            FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
            firebaseHelper.getUserRef(booking.getRiderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Set rating
                        double rating = user.getRating();
                        if (rating > 0) {
                            ratingText.setText(String.format(Locale.US, "%.1f", rating));
                        } else {
                            ratingText.setText("0.0");
                        }

                        // Load profile image
                        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                            Glide.with(context)
                                    .load(user.getProfileImageUrl())
                                    .placeholder(R.drawable.avatar)
                                    .into(profileImage);
                        } else {
                            profileImage.setImageResource(R.drawable.avatar);
                        }
                    } else {
                        ratingText.setText("0.0");
                        profileImage.setImageResource(R.drawable.avatar);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ratingText.setText("0.0");
                    profileImage.setImageResource(R.drawable.avatar);
                }
            });

            // Set button listeners
            messageButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMessageClick(booking);
                }
            });

            kickButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onKickClick(booking);
                }
            });
        }
    }
}

