package com.carpoolingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.carpoolingapp.R;
import com.carpoolingapp.models.Ride;
import java.util.List;

public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RideViewHolder> {

    private Context context;
    private List<Ride> rideList;
    private OnRideClickListener onRideClickListener;

    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }

    public RideRequestAdapter(Context context, List<Ride> rideList, OnRideClickListener onRideClickListener) {
        this.context = context;
        this.rideList = rideList;
        this.onRideClickListener = onRideClickListener;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ride_request, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rideList.get(position);
        holder.bind(ride, onRideClickListener);
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView priceText, seatsText, fromText, toText, dateText, timeText, riderNameText;
        ImageView luggageIcon, petIcon, bikeIcon, snowboardIcon;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            priceText = itemView.findViewById(R.id.priceText);
            seatsText = itemView.findViewById(R.id.seatsText);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            riderNameText = itemView.findViewById(R.id.riderNameText);
            luggageIcon = itemView.findViewById(R.id.ic_luggage);
            petIcon = itemView.findViewById(R.id.ic_pet);
            bikeIcon = itemView.findViewById(R.id.ic_bike);
            snowboardIcon = itemView.findViewById(R.id.ic_snowboard);
        }

        public void bind(final Ride ride, final OnRideClickListener listener) {
            priceText.setText(String.format("$%.0f", ride.getPricePerSeat()));
            seatsText.setText(String.format("%d seats requested", ride.getAvailableSeats()));
            fromText.setText(ride.getFromLocation());
            toText.setText(ride.getToLocation());
            dateText.setText(ride.getDate());
            timeText.setText(ride.getTime());
            riderNameText.setText(ride.getDriverName()); // For a request, driverName is the requester

            // Handle amenity icons
            luggageIcon.setVisibility(ride.isAllowsLuggage() ? View.VISIBLE : View.GONE);
            petIcon.setVisibility(ride.isAllowsPets() ? View.VISIBLE : View.GONE);
            bikeIcon.setVisibility(ride.isAllowsBikes() ? View.VISIBLE : View.GONE);
            snowboardIcon.setVisibility(ride.isAllowsSnowboards() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> listener.onRideClick(ride));
        }
    }
}
