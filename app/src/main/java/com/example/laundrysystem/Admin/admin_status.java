package com.example.laundrysystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class admin_status extends AppCompatActivity {

    ImageButton btn_addForm;
    ImageButton btn_history, btn_status, btn_profile, btn_laundry, btn_delete;

    TextView laundryName, location, services;

    private DatabaseReference reference;
    private String firstLaundryOwnerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_status);

        btn_addForm = findViewById(R.id.btn_addForm);
        btn_laundry = findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);
        btn_delete = findViewById(R.id.imgBtnDelete);

        laundryName = findViewById(R.id.laundryName);
        services = findViewById(R.id.services);
        location = findViewById(R.id.location);

        reference = FirebaseDatabase.getInstance().getReference("LaundryOwner");

        fetchLaundryOwnerDetails();

        btn_addForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_status.this, admin_addingform.class);
                startActivity(intent);
            }
        });

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_status.this, admin_home_screen.class);
                startActivity(intent);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_status.this, admin_status.class);
                startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_status.this, admin_history_screen.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_status.this, admin.class);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFirstLaundryOwner();
            }
        });
    }

    private void fetchLaundryOwnerDetails() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean foundFirst = false;
                    for (DataSnapshot laundrySnapshot : snapshot.getChildren()) {
                        if (!foundFirst) {
                            foundFirst = true;
                            firstLaundryOwnerKey = laundrySnapshot.getKey();  // Get the key of the first laundry owner

                            StringBuilder servicesBuilder = new StringBuilder();
                            String shopName = laundrySnapshot.child("shop_name").getValue(String.class);
                            String location = laundrySnapshot.child("location").getValue(String.class);

                            // Assuming 'services' is a child node under each LaundryOwner
                            for (DataSnapshot serviceSnapshot : laundrySnapshot.child("Prices").getChildren()) {
                                String serviceName = serviceSnapshot.getKey();
                                String price = serviceSnapshot.getValue(String.class);
                                servicesBuilder.append(serviceName).append(": ").append(price).append("\n");
                            }

                            // Update TextViews with data
                            if (shopName != null) laundryName.setText(shopName);
                            if (location != null) admin_status.this.location.setText(location);
                            if (servicesBuilder.length() > 0) services.setText(servicesBuilder.toString());

                            break;
                        }
                    }
                } else {
                    Toast.makeText(admin_status.this, "\uD83D\uDE05 Oops, No Laundry Owners Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin_status.this, "\uD83D\uDEAB Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFirstLaundryOwner() {
        if (firstLaundryOwnerKey != null) {
            reference.child(firstLaundryOwnerKey).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(admin_status.this, "\uD83D\uDDD1 Laundry Services Deleted", Toast.LENGTH_SHORT).show();
                    fetchLaundryOwnerDetails();  // Fetch the next available laundry owner
                } else {
                    Toast.makeText(admin_status.this, "⛔ Oops, Failed to Delete Laundry Service", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(admin_status.this, "\uD83D\uDD0E No Laundry Service to Delete", Toast.LENGTH_SHORT).show();
        }
    }
}
