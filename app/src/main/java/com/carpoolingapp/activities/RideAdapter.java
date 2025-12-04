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

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private Context context;
    private List<Ride> rideList;
    private OnRideClickListener listener;

    public interface OnRideClickListener {
        void onRideClick(Ride ride);
    }

    public RideAdapter(Context context, List<Ride> rideList, OnRideClickListener listener) {
        this.context = context;
        this.rideList = rideList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ride, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rideList.get(position);

        holder.fromText.setText(ride.getFromLocation());
        holder.toText.setText(ride.getToLocation());
        holder.dateText.setText(ride.getDate());
        holder.timeText.setText(ride.getTime());
        holder.priceText.setText("$" + String.format("%.2f", ride.getPricePerSeat()));
        holder.driverNameText.setText(ride.getDriverName());
        holder.seatsText.setText(ride.getAvailableSeats() + " seats");

        // Set tag color and text based on ride type
        if ("request".equals(ride.getRideType()) || "looking".equals(ride.getRideType())) {
            // Green tag for ride requests
            holder.rideTypeTag.setText("REQUEST");
            holder.rideTypeTag.setBackgroundResource(R.drawable.bg_tag_request);
        } else if ("hosting".equals(ride.getRideType())) {
            // Orange tag for hosting rides
            holder.rideTypeTag.setText("HOSTING");
            holder.rideTypeTag.setBackgroundResource(R.drawable.bg_tag_hosting);
        } else {
            // Default (shouldn't happen)
            holder.rideTypeTag.setText("RIDE");
            holder.rideTypeTag.setBackgroundResource(R.drawable.bg_tag_hosting);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRideClick(ride);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return rideList.size();
    }

    static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView fromText, toText, dateText, timeText, priceText, driverNameText, seatsText, rideTypeTag;
        ImageView luggageIcon, petIcon, bikeIcon, snowboardIcon;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            priceText = itemView.findViewById(R.id.priceText);
            driverNameText = itemView.findViewById(R.id.driverNameText);
            seatsText = itemView.findViewById(R.id.seatsText);

            // FIXED: Amenity icons with correct IDs
            luggageIcon = itemView.findViewById(R.id.ic_luggage);
            petIcon = itemView.findViewById(R.id.ic_pet);
            bikeIcon = itemView.findViewById(R.id.ic_bike);
            snowboardIcon = itemView.findViewById(R.id.ic_snowboard);
            rideTypeTag = itemView.findViewById(R.id.rideTypeTag);
        }
    }
}