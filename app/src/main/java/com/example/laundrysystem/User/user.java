package com.example.laundrysystem.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.Admin.admin;
import com.example.laundrysystem.LaundryOwners.laundryowner_home_screen;
import com.example.laundrysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class user extends AppCompatActivity {

    public Button btn_user;
    public Button btn_admin;
    public Button btn_signUp;

    EditText loginUsername, loginPassword;
    Button btn_login;

    private Spinner SpinnerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        btn_user = findViewById(R.id.btn_user);
        btn_admin = findViewById(R.id.btn_admin);
        btn_login = findViewById(R.id.btn_login);
        btn_signUp = findViewById(R.id.btn_sign_up);

        SpinnerText = findViewById(R.id.SpinnerText);

        loginUsername = findViewById(R.id.username);
        loginPassword = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);


        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, user.class);
                startActivity(intent);
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, admin.class);
                startActivity(intent);
            }
        });

        String[] textSizes = getResources().getStringArray(R.array.List_Option);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerText.setAdapter(adapter);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    // Handle validation failure
                    return;
                }
                // Get the selected item from the spinner
                String selectedItem = SpinnerText.getSelectedItem().toString();

                // Check if the selected item is "Login as Laundry Owner"
                if (selectedItem.equals("Login as Laundry Owner")) {
                    checkLaundryOwner();
                } else {
                    checkUser();
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, signup_user.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername(){
        String val = loginUsername.getText().toString();
        if(val.isEmpty()){
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if(val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    loginUsername.setError(null);
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("password").getValue(String.class);
                    String name = userSnapshot.child("name").getValue(String.class); //gin kwa ko lang name ka current ga user
                    String address = userSnapshot.child("address").getValue(String.class);
                    String contactNumber = userSnapshot.child("contactNumber").getValue(String.class);
                    //kag pasa ko kda class asta mka labot sa pricelist
                    if(Objects.equals(passwordFromDb, userPassword)) {
                        loginUsername.setError(null);
                        // Proceed to the laundry owner home screen and pass shop_name
                        Intent intent = new Intent(user.this, user_home_screen.class);
                        intent.putExtra("name", name);
                        intent.putExtra("address",address);//kwaon ang value ka shop_name sang launndry_owner
                        intent.putExtra("contactNumber",contactNumber);  // pra kag pasa sa homescreen ka laundry owner
                        startActivity(intent);
                        intent.removeExtra("name");
                        intent.removeExtra("address");
                        loginUsername.setText("");
                        loginPassword.setText("");
                    } else {
                        loginUsername.setError("Invalid Credentials!");
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                        loginUsername.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    public void checkLaundryOwner() {
        String laundryOwnerUsername = loginUsername.getText().toString().trim();
        String laundryOwnerPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LaundryOwner");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(laundryOwnerUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    loginUsername.setError(null);
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("password").getValue(String.class);
                    String shopName = userSnapshot.child("shop_name").getValue(String.class); //gin kwa ko lang name ka current ga user
                    //kag pasa ko kda class asta mka labot sa pricelist
                    if(Objects.equals(passwordFromDb, laundryOwnerPassword)) {
                        loginUsername.setError(null);
                        // Proceed to the laundry owner home screen and pass shop_name
                        Intent intent = new Intent(user.this, laundryowner_home_screen.class);
                        intent.putExtra("shop_name", shopName); //kwaon ang value ka shop_name sang launndry_owner
                        startActivity(intent);                        // pra kag pasa sa homescreen ka laundry owner
                        intent.removeExtra("shop_name");
                        loginUsername.setText("");
                        loginPassword.setText("");

                    } else {
                        loginPassword.setError("Invalid Credentials!");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("LaundryOwner Does Not Exist!");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    public void displayOwner(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);



    }
}
