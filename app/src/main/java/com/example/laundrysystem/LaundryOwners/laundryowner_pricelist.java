package com.example.laundrysystem.LaundryOwners;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class laundryowner_pricelist extends AppCompatActivity {
    ImageButton imageButton;
    Button btn_submit;
    EditText washPrice, dryPrice, foldPrice, fabricPrice, detergentPrice;

    DatabaseReference reference;
    FirebaseDatabase database;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_pricelist_home_screen);
        imageButton = findViewById(R.id.imageButton);
        btn_submit = findViewById(R.id.btn_submit);

        washPrice = findViewById(R.id.washPrice);
        dryPrice = findViewById(R.id.dryPrice);
        foldPrice = findViewById(R.id.foldPrice);
        fabricPrice = findViewById(R.id.fabricPrice);
        detergentPrice = findViewById(R.id.detergentPrice);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("LaundryOwner");

        // muna ang shop_name ka current laundry_owner
        shopName = getIntent().getStringExtra("shop_name");


        PriceServices();//disable ymn lng ni mga edittext nga wala na checkan ka LaundryOwner

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPrices();
                Intent intent = new Intent(laundryowner_pricelist.this, laundryowner_home_screen.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_pricelist.this, laundryowner_home_screen.class);
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });
    }

    private void PriceServices() {
        ArrayList<String> services = getIntent().getStringArrayListExtra("services");
        if (services != null) {
            if (!services.contains("Wash")) { //dri ya nalantaw tlng kung na check gd mn sang owner ang servies sa addservices nga class
                washPrice.setEnabled(false);
                washPrice.setBackgroundColor(Color.GRAY);
            }
            if (!services.contains("Dry")) {
                dryPrice.setEnabled(false);
                dryPrice.setBackgroundColor(Color.GRAY);
            }
            if (!services.contains("Fold")) {
                foldPrice.setEnabled(false);
                foldPrice.setBackgroundColor(Color.GRAY);
            }
            if (!services.contains("Fabric")) {
                fabricPrice.setEnabled(false);
                fabricPrice.setBackgroundColor(Color.GRAY);
            }
            if (!services.contains("Detergent")) {
                detergentPrice.setEnabled(false);
                detergentPrice.setBackgroundColor(Color.GRAY);
            }
        }
    }

    private void insertPrices() {
        if (shopName != null && !shopName.isEmpty()) {
            DatabaseReference shopRef = reference.child(shopName).child("Prices");//bali maubra child nga ngalan prices
            Map<String, Object> prices = new HashMap<>();
            // tapos muni values ka unod ya
            prices.put("Wash", washPrice.isEnabled() ? washPrice.getText().toString() : null);
            prices.put("Dry", dryPrice.isEnabled() ? dryPrice.getText().toString() : null);
            prices.put("Fold", foldPrice.isEnabled() ? foldPrice.getText().toString() : null);
            prices.put("Fabric", fabricPrice.isEnabled() ? fabricPrice.getText().toString() : null);
            prices.put("Detergent", detergentPrice.isEnabled() ? detergentPrice.getText().toString() : null);

            shopRef.setValue(prices)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(laundryowner_pricelist.this, "\uD83C\uDF89 Prices Inserted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(laundryowner_pricelist.this, "❌ Oops, Failed to Insert prices", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "\uD83E\uDD7A Leaving to Soon", Toast.LENGTH_SHORT).show();
        }
    }
}
