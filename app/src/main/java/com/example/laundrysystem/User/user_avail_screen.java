package com.example.laundrysystem.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class user_avail_screen extends AppCompatActivity {

    public ImageButton btn_back;

    Button btn_avail;
    TextView profileName,price,wash;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_avail_screen);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_avail = (Button) findViewById(R.id.btn_avail);

        price = findViewById(R.id.price);
        wash = findViewById(R.id.wash);

        userData();

        // Retrieve the list of checked items from the intent




        btn_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(user_avail_screen.this, user_laundry_owner_services_screen.class);
//                startActivity(intent);
                history();
            }
        });
    }



    //  FireBase
    public void userData(){
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        profileName.setText(nameUser);
    }

    //    Transfer to history
    public void history(){
        String userUsername = profileName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("name").getValue(String.class);

                    if(passwordFromDb.equals(userUsername)){
                        profileName.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String addressFromDB = snapshot.child(userUsername).child("address").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String numberFromDB = snapshot.child(userUsername).child("contactNumber").getValue(String.class);
                        String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                        String imgUrlFromDB = snapshot.child(userUsername).child("image").getValue(String.class);
                        // Proceed to the user home screen
                        Intent intent = new Intent(user_avail_screen.this, user_laundry_owner_services_screen.class);
                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("address",addressFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("contactNumber",numberFromDB);
                        intent.putExtra("password",passwordFromDB);
                        intent.putExtra("image",imgUrlFromDB);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
