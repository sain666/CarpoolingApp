package com.carpoolingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.carpoolingapp.R;
import com.carpoolingapp.models.Receipt;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    private TextView receiptNumberText, bookingDateText, riderNameText, riderEmailText;
    private TextView driverNameText, driverEmailText, fromLocationText, toLocationText;
    private TextView rideDateText, rideTimeText, seatsText, pricePerSeatText, totalAmountText;
    private TextView paymentMethodText, statusText;
    private MaterialButton doneButton, emailButton;

    private Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        initViews();
        setupToolbar();
        loadReceiptData();
        setupListeners();
    }

    private void initViews() {
        receiptNumberText = findViewById(R.id.receiptNumberText);
        bookingDateText = findViewById(R.id.bookingDateText);
        riderNameText = findViewById(R.id.riderNameText);
        riderEmailText = findViewById(R.id.riderEmailText);
        driverNameText = findViewById(R.id.driverNameText);
        driverEmailText = findViewById(R.id.driverEmailText);
        fromLocationText = findViewById(R.id.fromLocationText);
        toLocationText = findViewById(R.id.toLocationText);
        rideDateText = findViewById(R.id.rideDateText);
        rideTimeText = findViewById(R.id.rideTimeText);
        seatsText = findViewById(R.id.seatsText);
        pricePerSeatText = findViewById(R.id.pricePerSeatText);
        totalAmountText = findViewById(R.id.totalAmountText);
        paymentMethodText = findViewById(R.id.paymentMethodText);
        statusText = findViewById(R.id.statusText);
        doneButton = findViewById(R.id.doneButton);
        emailButton = findViewById(R.id.emailButton);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Payment Receipt");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadReceiptData() {
        // Get receipt data from intent
        Intent intent = getIntent();
        String receiptId = intent.getStringExtra("receiptId");
        String bookingId = intent.getStringExtra("bookingId");
        String rideId = intent.getStringExtra("rideId");
        String riderName = intent.getStringExtra("riderName");
        String riderEmail = intent.getStringExtra("riderEmail");
        String driverName = intent.getStringExtra("driverName");
        String driverEmail = intent.getStringExtra("driverEmail");
        String fromLocation = intent.getStringExtra("fromLocation");
        String toLocation = intent.getStringExtra("toLocation");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        int seats = intent.getIntExtra("seats", 1);
        double pricePerSeat = intent.getDoubleExtra("pricePerSeat", 0.0);
        double totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
        String paymentMethod = intent.getStringExtra("paymentMethod");

        // Display data
        receiptNumberText.setText("#" + (receiptId != null ? receiptId.substring(0, Math.min(8, receiptId.length())) : bookingId));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        bookingDateText.setText(dateFormat.format(new Date()));

        riderNameText.setText(riderName);
        riderEmailText.setText(riderEmail);
        driverNameText.setText(driverName);
        driverEmailText.setText(driverEmail);
        fromLocationText.setText(fromLocation);
        toLocationText.setText(toLocation);
        rideDateText.setText(date);
        rideTimeText.setText(time);
        seatsText.setText(String.valueOf(seats));
        pricePerSeatText.setText("$" + String.format("%.2f", pricePerSeat));
        totalAmountText.setText("$" + String.format("%.2f", totalAmount));
        paymentMethodText.setText(paymentMethod != null ? paymentMethod : "Credit Card");
        statusText.setText("✓ PAID");
        statusText.setTextColor(getColor(R.color.status_active));
    }

    private void setupListeners() {
        boolean isDemo = getIntent().getBooleanExtra("isDemo", false);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDemo) {
                    // Demo: Go to chat
                    Intent intent = new Intent(ReceiptActivity.this, ChatActivity.class);
                    intent.putExtra("isDemo", true);
                    startActivity(intent);
                    finish();
                } else {
                    // Normal: Go to home
                    Intent intent = new Intent(ReceiptActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReceiptEmail();
            }
        });

        if (isDemo) {
            doneButton.setText("Continue to Chat →");
        }
    }

    private void sendReceiptEmail() {
        // Simulate sending email
        Toast.makeText(this, "📧 Receipt sent to both rider and driver emails!", Toast.LENGTH_LONG).show();

        // In a real app, you would:
        // 1. Use Firebase Cloud Functions
        // 2. Integrate with SendGrid or similar email service
        // 3. Send formatted email with receipt details
    }
}