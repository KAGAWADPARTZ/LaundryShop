package com.example.laundrysystem.LaundryOwners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class laundryowner_home_screen extends AppCompatActivity {

    ImageButton btn_laundry, btn_status, btn_history, btn_profile;
    Button btn_add_edit;
    DatabaseReference reference;
    TextView priceWash, priceDry, priceFold, priceFabric, priceDetergent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_home_screen);

        btn_laundry = findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);
        btn_add_edit = findViewById(R.id.btn_add_edit);

        priceWash = findViewById(R.id.priceWash);
        priceDry = findViewById(R.id.priceDry);
        priceFold = findViewById(R.id.priceFold);
        priceFabric = findViewById(R.id.priceFabric);
        priceDetergent = findViewById(R.id.priceDetergent);

        String shopName = getIntent().getStringExtra("shop_name");

        fetchLaundryOwnerDetails(shopName);//display laundryowner prices

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_home_screen.this, laundryowner_home_screen.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_home_screen.this, laundryowner_status.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_home_screen.this, laundryowner_history.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_home_screen.this, laundryowner_profile.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });

        btn_add_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_home_screen.this, laundryowner_addservices.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });
    }

    private void fetchLaundryOwnerDetails(String shopName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LaundryOwner")
                .child(shopName).child("Prices");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String washPrice = snapshot.child("Wash").getValue(String.class);
                    String dryPrice = snapshot.child("Dry").getValue(String.class);
                    String foldPrice = snapshot.child("Fold").getValue(String.class);
                    String fabricPrice = snapshot.child("Fabric").getValue(String.class);
                    String detergentPrice = snapshot.child("Detergent").getValue(String.class);

                    if (washPrice != null) priceWash.setText(washPrice);
                    if (dryPrice != null) priceDry.setText(dryPrice);
                    if (foldPrice != null) priceFold.setText(foldPrice);
                    if (fabricPrice != null) priceFabric.setText(fabricPrice);
                    if (detergentPrice != null) priceDetergent.setText(detergentPrice);
                } else {
                    Toast.makeText(laundryowner_home_screen.this, "⚠️Oops, Laundry Service Details Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(laundryowner_home_screen.this, "⛔ Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}