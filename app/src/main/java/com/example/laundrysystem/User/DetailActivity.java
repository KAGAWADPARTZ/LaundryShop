package com.example.laundrysystem.User;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;

public class DetailActivity extends AppCompatActivity {

    private TextView nameTextView, servicesTextView, priceTextView, locationTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameTextView = findViewById(R.id.nameTextView);
        servicesTextView = findViewById(R.id.servicesTextView);
        priceTextView = findViewById(R.id.priceTextView);
        locationTextView = findViewById(R.id.locationTextView);

        // Get data from Intent
        String name = getIntent().getStringExtra("name");
        String services = getIntent().getStringExtra("contact_number");
        String price = getIntent().getStringExtra("price");
        String location = getIntent().getStringExtra("location");

        // Set data to TextViews
        nameTextView.setText(name);
        servicesTextView.setText(services);
        priceTextView.setText(price);
        locationTextView.setText(location);
    }
}
