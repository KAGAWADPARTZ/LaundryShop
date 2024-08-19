package com.example.laundrysystem.LaundryOwners;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laundrysystem.User.user;

import com.example.laundrysystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class laundryowner_profile extends AppCompatActivity {

    Button btn_upload_profile, btn_logout, btn_save_profile;
    ImageButton btn_upload_image, btn_back;
    ImageView laundry_profile;
    TextView Laundry_shop_name, Laundry_address, Laundry_email, Laundry_username, Laundry_password, Laundry_contact_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_profile);

        // Initialize TextViews
        Laundry_shop_name = findViewById(R.id.laundryName);
        Laundry_address = findViewById(R.id.laundryAddress);
        Laundry_email = findViewById(R.id.laundryEmail);
        Laundry_username = findViewById(R.id.laundryUsername);
        Laundry_password = findViewById(R.id.laundryPassword);
        Laundry_contact_number = findViewById(R.id.laundryNumber);

        // Initialize Buttons
        btn_save_profile = findViewById(R.id.btn_save_profile);
        btn_upload_profile = findViewById(R.id.btn_upload_image_userprofile);
        btn_logout = findViewById(R.id.btn_logout_userprofile);
        btn_upload_image = findViewById(R.id.btn_upload_images);
        btn_back = findViewById(R.id.btn_back);
        laundry_profile = findViewById(R.id.laundry_profile);

        // Get the shop_name from intent
        String shopName = getIntent().getStringExtra("shop_name");

        if (shopName != null) {
            fetchLaundryOwnerDetails(shopName);
        } else {
            Toast.makeText(this, "Shop name not provided", Toast.LENGTH_SHORT).show();
        }

        // Handle Logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure user.class is an activity. If it's not, replace with appropriate activity.
                Intent intent = new Intent(laundryowner_profile.this, user.class);
                startActivity(intent);
            }
        });

        // Handle Back Button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_profile.this, laundryowner_home_screen.class);
                startActivity(intent);
            }
        });

        // Handle Profile Image Upload
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        laundry_profile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(laundryowner_profile.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_upload_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_profile.this, laundryowner_upload_images_screen.class);
                startActivity(intent);
            }
        });

    }

        private void fetchLaundryOwnerDetails(String shopName) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LaundryOwner").child(shopName);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String shopName = snapshot.child("shop_name").getValue(String.class);
                        String location = snapshot.child("location").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String username = snapshot.child("username").getValue(String.class);
                        String password = snapshot.child("password").getValue(String.class);
                        String contactNumber = snapshot.child("contact_number").getValue(String.class);

                        if (shopName != null) Laundry_shop_name.setText(shopName);
                        if (location != null) Laundry_address.setText(location);
                        if (email != null) Laundry_email.setText(email);
                        if (username != null) Laundry_username.setText(username);
                        if (password != null) Laundry_password.setText(password);
                        if (contactNumber != null) Laundry_contact_number.setText(contactNumber);
                    } else {
                        Toast.makeText(laundryowner_profile.this, "⚠️Oops, Laundry Service Details Not Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(laundryowner_profile.this, "\uD83D\uDEAB Failed to Fetch Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
}
