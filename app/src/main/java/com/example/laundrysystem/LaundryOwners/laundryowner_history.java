package com.example.laundrysystem.LaundryOwners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.example.laundrysystem.User.LaundryMainAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class laundryowner_history extends AppCompatActivity {

    RecyclerView recyclerView;
    LaundryMainAdapter mainAdapter;

    ImageButton btn_laundry,btn_status,btn_history,btn_profile;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_history_screen);
        String shopName = getIntent().getStringExtra("shop_name");

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<com.example.laundrysystem.LaundryOwners.LaundryMainModel> options =
                new FirebaseRecyclerOptions.Builder<com.example.laundrysystem.LaundryOwners.LaundryMainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History").child(shopName), LaundryMainModel.class)
                        .build();
        mainAdapter = new LaundryMainAdapter(options);
//        mainAdapter.setOnItemClickListener(new View(user_home));
        recyclerView.setAdapter(mainAdapter);

        btn_laundry =  findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history =  findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_history.this, laundryowner_home_screen.class);
                startActivity(intent);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_history.this, laundryowner_status.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_history.this, laundryowner_history.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_history.this, laundryowner_profile.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });
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
