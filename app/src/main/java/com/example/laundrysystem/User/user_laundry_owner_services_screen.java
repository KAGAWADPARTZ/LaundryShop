package com.example.laundrysystem.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.LaundryOwners.laundryowner_home_screen;
import com.example.laundrysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class user_laundry_owner_services_screen extends AppCompatActivity {


    Dialog dialog;
    Button btnCancel,btnAvail;

    public ImageButton btn_back, btn_profile;
    public Button btn_Avail;
    TextView profileName;
    TextView laundryName, location, priceWash,priceDry,priceFold,priceFabric,priceDetergent;
    String userLocation, shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_laundry_owner_services_screen);

        btn_back = findViewById(R.id.btn_back); //initialize tanan nga kilanlan
        btn_Avail = findViewById(R.id.btn_Avail);
        btn_profile = findViewById(R.id.btn_profile); //initialize tanan nga kilanlan
        laundryName = findViewById(R.id.laundryName);
        location = findViewById(R.id.location);
        profileName = findViewById(R.id.profileName); //initialize tanan nga kilanlan
        priceWash = findViewById(R.id.priceWash);
        priceDry = findViewById(R.id.priceDry); //initialize tanan nga kilanlan
        priceFold = findViewById(R.id.priceFold);
        priceFabric = findViewById(R.id.priceFabric);
        priceDetergent = findViewById(R.id.priceDetergent);

        dialog = new Dialog(user_laundry_owner_services_screen.this);
        dialog.setContentView(R.layout.dialog_insert_data);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAvail = dialog.findViewById(R.id.btnAvail);

        userLocation = getIntent().getStringExtra("address"); // kwaon ang intent halin sa user_home_screen pra ka retrive data
        shopName = getIntent().getStringExtra("shop_name");// kwaon ang intent halin sa user_home_screen pra ka retrive data
        displayPrices(shopName);
        displayShopName(shopName);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAvail.setOnClickListener(new View.OnClickListener() {

            Intent intent = getIntent();
            @Override
            public void onClick(View view) {
                String laundryShopName = laundryName.getText().toString().trim();
                String userUsername = intent.getStringExtra("name");
                String contactNumber = intent.getStringExtra("contactNumber");
                ClientInsertUser(laundryShopName, userUsername, userLocation,contactNumber);
                ClientInsertLaundry(laundryShopName, userUsername, userLocation,contactNumber);
                next();
            }
        });

        userData();

        btn_profile.setOnClickListener(view -> profile());

        btn_Avail.setOnClickListener(view -> {
            dialog.show();
        });

        btn_back.setOnClickListener(view -> history());
    }

    public void userData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        String userLocation = intent.getStringExtra("location");

        location.setText(userLocation);
        laundryName.setText(nameUser);
        String username = intent.getStringExtra("username");
        profileName.setText(username);
    }

    public void history() {
        String userUsername = profileName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next(); //intent nga ipasa sa user_history nga class
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String addressFromDB = userSnapshot.child("address").getValue(String.class); //intent nga ipasa sa user_history nga class
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String numberFromDB = userSnapshot.child("contactNumber").getValue(String.class);
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class); //intent nga ipasa sa user_history nga class
                    // Proceed to the user home screen
                    Intent intent = new Intent(user_laundry_owner_services_screen.this, user_home_screen.class);
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
            }
        });
    }

    public void profile() {
        String userUsername = profileName.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next(); //intent nga ipasa sa user_prpfile nga class
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String addressFromDB = userSnapshot.child("address").getValue(String.class); //intent nga ipasa sa user_prpfile nga class
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String numberFromDB = userSnapshot.child("contactNumber").getValue(String.class); //intent nga ipasa sa user_prpfile nga class
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                    // Proceed to the user profile screen
                    Intent intent = new Intent(user_laundry_owner_services_screen.this, user_profile.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("address", addressFromDB); //remove pra mag back cla nd mag crash
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("contactNumber", numberFromDB);//remove pra mag back cla nd mag crash
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void next() {
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
                    // Proceed to the user avail screen
                    Intent intent = new Intent(user_laundry_owner_services_screen.this, user_avail_screen.class);
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
            }
        });
    }

    private void ClientInsertUser(String laundryShopName, String userUsername, String userLocation, String number) { // user transaction
        // Reference to the transaction node
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserTransaction").child(userUsername).child(userUsername);

        String Pending= "Pending";

        // Create a map to hold the user's information
        Map<String, Object> clientData = new HashMap<>();
        clientData.put("name", userUsername);
        clientData.put("address", userLocation);
        clientData.put("shop_name", laundryShopName);
        clientData.put("contactNumber",number);
        clientData.put("status",Pending);


        // Add the user to the clients child using the username as the key
        reference.setValue(clientData).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "✅ YOUR REQUEST WAS SUCCESSFULLY SUBMITTED.", Toast.LENGTH_SHORT).show();
                // Successfully added client
            } else {
                Toast.makeText(this, "✅ YOUR REQUEST WAS FAILED.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClientInsertLaundry(String laundryShopName, String userUsername, String userLocation, String number) { // laundry transaction
        // Reference to the transaction node
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transaction")
                .child(laundryShopName)
               .child(userUsername);
        String Pending= "Pending";

        // Create a map to hold the user's information
        Map<String, Object> clientData = new HashMap<>();
        clientData.put("name", userUsername);
        clientData.put("address", userLocation);
        clientData.put("shop_name", laundryShopName);
        clientData.put("contactNumber",number);
        clientData.put("status",Pending);


        // Add the user to the clients child using the username as the key
        reference.setValue(clientData).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "✅ YOUR REQUEST WAS SUCCESSFULLY SUBMITTED.", Toast.LENGTH_SHORT).show();
                // Successfully added client
            } else {
                Toast.makeText(this, "✅ YOUR REQUEST WAS FAILED.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayPrices(String shopName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LaundryOwner").child(shopName).child("Prices");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String washPrice = snapshot.child("Wash").getValue(String.class);
                    String dryPrice = snapshot.child("Dry").getValue(String.class);
                    String foldPrice = snapshot.child("Fold").getValue(String.class);
                    String fabricPrice = snapshot.child("Fabric").getValue(String.class);
                    String detergentPrice = snapshot.child("Detergent").getValue(String.class);
                    String namelaundry = snapshot.child("shop_name").getValue(String.class);

                    if (washPrice != null) priceWash.setText(washPrice);
                    if (dryPrice != null) priceDry.setText(dryPrice);
                    if (foldPrice != null) priceFold.setText(foldPrice);
                    if (fabricPrice != null) priceFabric.setText(fabricPrice);
                    if (detergentPrice != null) priceDetergent.setText(detergentPrice);
                    if (namelaundry != null ) laundryName.setText(namelaundry);
                } else {
                    Toast.makeText(user_laundry_owner_services_screen.this, "⚠️Oops, Laundry Service Details Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_laundry_owner_services_screen.this, "⛔ Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayShopName(String shopName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LaundryOwner").child(shopName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() { //display shop_name
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String laundryname = snapshot.child("shop_name").getValue(String.class);
                    if ( laundryname != null) laundryName.setText( laundryname);
                } else {
                    Toast.makeText(user_laundry_owner_services_screen.this, "⚠️Oops, LaundryShop", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_laundry_owner_services_screen.this, "⛔ Failed to Fetch Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}