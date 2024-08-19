package com.example.laundrysystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.example.laundrysystem.User.MainAdapter;
import com.example.laundrysystem.User.MainModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class admin_home_screen extends AppCompatActivity {

    // Firebase
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    // TextViews for displaying data
    CircleImageView laundry_img;
    TextView laundryName, services, location;
    public ImageButton btn_laundry;
    public ImageButton btn_status;
    public ImageButton btn_history;
    public ImageButton btn_profile;
    public ImageButton btn_laundryProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_screen);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("LaundryOwner"),MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel model) {
                // Handle the item click event here
////                dialog.show();
//
                    Toast.makeText(admin_home_screen.this, "\uD83D\uDC64 Admin is Now Monitoring: " + model.getShop_name(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(admin_home_screen.this, admin_laundry_owner_services_screen.class);
                    intent.putExtra("name", model.getShop_name());
                    intent.putExtra("location", model.getLocation());
                    intent.putExtra("contact_number", model.getContact_number());
                    startActivity(intent);
            }
        });



//        mainAdapter.setOnItemClickListener(new View(user_home));
        recyclerView.setAdapter(mainAdapter);

        // Initialize buttons
        btn_laundry = findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);
//        btn_laundryProfile = findViewById(R.id.btn_laundryProfile);
//
//        // Initialize TextViews
//        laundryName = findViewById(R.id.laundryName);
//        services = findViewById(R.id.services);
//        location = findViewById(R.id.location);

        // Set button click listeners
//        btn_laundryProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(admin_home_screen.this, user_laundry_owner_services_screen.class);
//                startActivity(intent);
//            }
//        });

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_home_screen.this, admin_home_screen.class);
                startActivity(intent);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_home_screen.this, admin_status.class);
                startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_home_screen.this, admin_history_screen.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_home_screen.this, admin.class);
                startActivity(intent);
            }
        });

        // Create a list of LaundryOwner objects
//        fetchLaundryOwnerDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}
