package com.example.laundrysystem.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.laundrysystem.LaundryOwners.LaundryMainModel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.laundrysystem.LaundryOwners.LaundryMainModel;
import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class    user_history extends AppCompatActivity {
    public ImageButton btn_laundry;
    public ImageButton btn_status;
    public ImageButton btn_history;
    public ImageButton btn_profile;
    RecyclerView recyclerView;
    AdapterUser mainAdapter;

    TextView profileName;

    String nameFromDB, shop_name, Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history);

        btn_laundry = findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);

        Name = getIntent().getStringExtra("name");//get the intent of the user from the other class

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<LaundryMainModel> options =
                new FirebaseRecyclerOptions.Builder<LaundryMainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UserHistory").child(Name), LaundryMainModel.class)
                        .build();
        mainAdapter = new AdapterUser(options);
        recyclerView.setAdapter(mainAdapter);

        profileName = findViewById(R.id.profileName);
        userData();

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(user_home_screen.class);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(user_status.class);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(user_history.class);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(user_profile.class);
            }
        });
    }

    private void navigateToScreen(Class<?> targetActivity) {
        String userUsername = profileName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String addressFromDB = userSnapshot.child("address").getValue(String.class);
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String numberFromDB = userSnapshot.child("contactNumber").getValue(String.class);
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                    Intent intent = new Intent(user_history.this, targetActivity);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("address", addressFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("contactNumber", numberFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    public void userData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        profileName.setText(nameUser);
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


