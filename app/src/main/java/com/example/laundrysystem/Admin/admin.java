package com.example.laundrysystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.laundrysystem.R;
import com.example.laundrysystem.User.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class admin extends AppCompatActivity {

    public Button btn_user;
    public Button btn_admin;

    EditText loginUsername, loginPassword;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        btn_user = findViewById(R.id.btn_user);
        btn_admin = findViewById(R.id.btn_admin);
        btn_login = findViewById(R.id.btn_login);

        loginUsername = findViewById(R.id.username);
        loginPassword = findViewById(R.id.password);

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this, user.class);
                startActivity(intent);
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this, admin.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    // Validation failed
                    return;
                } else {
                    // Check if Admin exists
                    checkAdmin();
                }
            }
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username Cannot be Empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password Cannot be Empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkAdmin() {
        String adminUsername = loginUsername.getText().toString().trim();
        String adminPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(adminUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("password").getValue(String.class);

                    if (Objects.equals(passwordFromDb, adminPassword)) {
                        loginUsername.setError(null);
                        // Proceed to the user home screen
                        Intent intent = new Intent(admin.this, admin_home_screen.class);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User Does Not Exist!");
                    loginUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }
}
