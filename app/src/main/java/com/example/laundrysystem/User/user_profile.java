package com.example.laundrysystem.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_profile extends AppCompatActivity {

    // Firebase Connection
    TextView profileName, profileAddress, profileEmail, profilePassword, profileNumber;
    CircleImageView user_profile;
    public Button btn_uploadImage;
    public Button btn_editProfile;
    public Button btn_logout;
    public ImageButton btn_back;

    // ActivityResultLauncher for selecting images
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize UI elements
        btn_editProfile = findViewById(R.id.btn_edit_profile_userprofile);
        btn_logout = findViewById(R.id.btn_logout_userprofile);
        btn_back = findViewById(R.id.btn_back);
        btn_uploadImage = findViewById(R.id.btn_uploadImage);

        // Firebase Connection
        profileName = findViewById(R.id.profileName);
        profileAddress = findViewById(R.id.profileAddress);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileNumber = findViewById(R.id.profileNumber);
        user_profile = findViewById(R.id.user_profile);

        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String contactNumber = getIntent().getStringExtra("contactNumber");

        // Show user data
        showUserData(); //display user info
        showImg();//display image
        // Button Back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laundry();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, user.class);
                startActivity(intent);
            }
        });



        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri selectedImage = result.getData().getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                                Bitmap resizedBitmap = resizeBitmap(bitmap, 500);
                                user_profile.setImageBitmap(resizedBitmap);

                                // Upload the image after the user selects it
                                uploadImageToFirebase(resizedBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Trigger the image picker when the upload button is clicked
        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, user_edit_profile.class);
                intent.putExtra("name", profileName.getText().toString());
                intent.putExtra("address", profileAddress.getText().toString());
                intent.putExtra("contactNumber", profileNumber.getText().toString());
                intent.putExtra("email", profileEmail.getText().toString());
                intent.putExtra("password", profilePassword.getText().toString());
                startActivity(intent);
            }
        });
    }

    // Resize bitmap to a specified width while maintaining aspect ratio
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth) {
        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        int height = Math.round(maxWidth / aspectRatio);
        return Bitmap.createScaledBitmap(bitmap, maxWidth, height, false);
    }



    // Show user data
    public void showUserData() {
        // Get Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        // Get the name passed through the Intent
        Intent intent = getIntent();
        String nameFromDB = intent.getStringExtra("name");

        // Query the database for the user by name
        Query query = reference.orderByChild("name").equalTo(nameFromDB);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, retrieve their data
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        String address = userSnapshot.child("address").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String contactNumber = userSnapshot.child("contactNumber").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);

                        // Display the retrieved data
                        profileName.setText(name);
                        profileAddress.setText(address);
                        profileEmail.setText(email);
                        profilePassword.setText(password);
                        profileNumber.setText(contactNumber);
                    }
                } else {
                    // User does not exist, handle this case
                    Toast.makeText(user_profile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
                Toast.makeText(user_profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Navigate to user home screen
    public void laundry() {
        navigateTo(user_home_screen.class);
    }

    // Navigate to user edit profile screen
    public void editProfile() {
        navigateTo(user_edit_profile.class);
    }

    // Reusable method for navigation
    private void navigateTo(Class<?> targetActivity) {
        String userUsername = profileName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("name").getValue(String.class);

                    if (passwordFromDb.equals(userUsername)) {
                        profileName.setError(null);
                        String nameFromDB = userSnapshot.child("name").getValue(String.class);
                        String addressFromDB = userSnapshot.child("address").getValue(String.class);
                        String emailFromDB = userSnapshot.child("email").getValue(String.class);
                        String numberFromDB = userSnapshot.child("contactNumber").getValue(String.class);
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        Intent intent = new Intent(user_profile.this, targetActivity);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("address", addressFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("contactNumber", numberFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    // Upload image to Firebase
    private void uploadImageToFirebase(Bitmap bitmap) {
        if (bitmap != null) {
            String name = getIntent().getStringExtra("name");

            // Convert bitmap to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); // Compress to reduce size
            byte[] data = baos.toByteArray();

            // Create Firebase Storage reference with a unique filename
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("user").child(name + "/" + UUID.randomUUID().toString() + ".jpg");

            // Start the upload task
            UploadTask uploadTask = storageRef.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful uploads
                    Toast.makeText(user_profile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseStorage", "Upload failed", e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Upload successful, handle the success
                    Toast.makeText(user_profile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(user_profile.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void showImg() {
        String name = getIntent().getStringExtra("name");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("user").child(name);

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size() > 0) {
                    StorageReference imageRef = listResult.getItems().get(0);
                    imageRef.getBytes(5*1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            user_profile.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(user_profile.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }
}
