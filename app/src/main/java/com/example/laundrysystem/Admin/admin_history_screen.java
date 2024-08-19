package com.example.laundrysystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.LaundryOwners.LaundryMainModel;
import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class admin_history_screen  extends AppCompatActivity {

    RecyclerView recyclerView;
    com.example.laundrysystem.User.LaundryMainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton btn_history, btn_status, btn_profile, btn_laundry;

        setContentView(R.layout.admin_history_screen);
        btn_laundry = (ImageButton) findViewById(R.id.btn_laundry);
        btn_status = (ImageButton) findViewById(R.id.btn_status);
        btn_history = (ImageButton) findViewById(R.id.btn_history);
        btn_profile = (ImageButton) findViewById(R.id.btn_profile);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<LaundryMainModel> options =
                new FirebaseRecyclerOptions.Builder<com.example.laundrysystem.LaundryOwners.LaundryMainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History"), LaundryMainModel.class)
                        .build();
        mainAdapter = new com.example.laundrysystem.User.LaundryMainAdapter(options);
//        mainAdapter.setOnItemClickListener(new View(user_home));
        recyclerView.setAdapter(mainAdapter);

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_history_screen.this, admin_home_screen.class);
                startActivity(intent);
            }
        });
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_history_screen.this, admin_status.class);
                startActivity(intent);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_history_screen.this, admin_history_screen.class);
                startActivity(intent);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_history_screen.this, admin.class);
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
