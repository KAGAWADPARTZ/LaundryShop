package com.example.laundrysystem.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class user_edit_profile extends AppCompatActivity {

    EditText profileName, profileAddress, profileEmail, profilePassword, profileNumber;
    CircleImageView user_profile;
    DatabaseReference reference;
    ImageButton btn_back;
    Button btn_save_profile_userprofile, btn_uploadImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String imageUrl, previousName, previousImageUrl;
    private Uri selectedImageUri; // Store the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_profile);

        // Initialize UI elements
        btn_back = findViewById(R.id.btn_back);
        profileName = findViewById(R.id.profileName);
        profileAddress = findViewById(R.id.profileAddress);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        profileNumber = findViewById(R.id.profileNumber);
        user_profile = findViewById(R.id.user_profile);
        btn_save_profile_userprofile = findViewById(R.id.btn_save_profile_userprofile);
        btn_uploadImage = findViewById(R.id.btn_uploadImage);

        // Firebase reference
        reference = FirebaseDatabase.getInstance().getReference("users");

        // Show existing user data and image
        showUserData();
        showImg();

        // Image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                Bitmap resizedBitmap = resizeBitmap(bitmap, 500);
                                user_profile.setImageBitmap(resizedBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Upload image button listener
        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
            }
        });

        // Save profile button listener
        btn_save_profile_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle saving data and image on button click
                //String name = getIntent().getStringExtra("name");
                DeleteFolder();
                handleSaveProfile();

                Intent intent = new Intent(user_edit_profile.this, user_profile.class);
                intent.putExtra("name", profileName.getText().toString().trim());
                intent.putExtra("email", profileEmail.getText().toString().trim());
                intent.putExtra("contactNumber", profileNumber.getText().toString().trim());
                intent.putExtra("address", profileAddress.getText().toString().trim());
                intent.putExtra("password", profilePassword.getText().toString().trim());

                startActivity(intent);
            }
        });

        // Back button listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(user_edit_profile.this, user_profile.class);
                Intent intent = new Intent(user_edit_profile.this, user_profile.class);
                intent.putExtra("name", profileName.getText().toString().trim());
                intent.putExtra("email", profileEmail.getText().toString().trim());
                intent.putExtra("contactNumber", profileNumber.getText().toString().trim());
                intent.putExtra("address", profileAddress.getText().toString().trim());
                intent.putExtra("password", profilePassword.getText().toString().trim());
                startActivity(intent);
                laundry();
            }
        });
    }

    private void handleSaveProfile() {
        String newName = profileName.getText().toString().trim();

        if (!newName.equals(previousName)) {
            // User changed their name, handle renaming
            insertUserData(newName);
        } else {
            // User didn't change their name, simply update data
            insertUserData(previousName);
        }
    }

    private void uploadImageToFirebase(String name) {
        if (selectedImageUri != null) {
            // Create Firebase Storage reference with a unique filename
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //StorageReference storageRef = storage.getReference().child("user").child(name + "/" + UUID.randomUUID().toString() + ".jpg");
            StorageReference storageRef = storage.getReference().child("user").child(name + "/" + "1"+ ".jpg");

            // Start the upload task
            storageRef.putFile(selectedImageUri)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_edit_profile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseStorage", "Upload failed", e);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Retrieve the download URL and store it
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    reference.child(name).child("profileImageUrl").setValue(imageUrl);
                                    Toast.makeText(user_edit_profile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(user_edit_profile.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertUserData(String newName) {
        String email = profileEmail.getText().toString().trim(),
                password = profilePassword.getText().toString().trim(),
                contact = profileNumber.getText().toString().trim(),
                address = profileAddress.getText().toString().trim();

        if (!newName.isEmpty()) {
            // Create a new HelperClass object
            HelperClass helperClass = new HelperClass(newName, email, password, contact, address);

            // Insert the new data into the database under the new user's name
            reference.child(newName).setValue(helperClass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // If the name has changed, delete the old node
                    if (!newName.equals(previousName)) {
                        reference.child(previousName).removeValue().addOnCompleteListener(deleteTask -> {
                            if (deleteTask.isSuccessful()) {
                                // Optionally delete the old profile image associated with the old name


                                // Upload the new image if available
                                uploadImageToFirebase(newName);

                                Toast.makeText(user_edit_profile.this, "✅ Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(user_edit_profile.this, "⛔ Failed to delete old profile", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // If the name didn't change, just upload the image
                        uploadImageToFirebase(newName);
                        Toast.makeText(user_edit_profile.this, "✅ Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                    }

                    // Update the previousName variable with the new name
                    previousName = newName;
                } else {
                    Toast.makeText(user_edit_profile.this, "⛔ Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(user_edit_profile.this, "⛔ Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth) {
        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        int height = Math.round(maxWidth / aspectRatio);
        return Bitmap.createScaledBitmap(bitmap, maxWidth, height, false);
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
                Toast.makeText(user_edit_profile.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }

    private void DeleteFolder() {
        String name = getIntent().getStringExtra("name");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("user").child(name + "/" + "1" + ".jpg");

        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

    }

    public void showUserData() {
        String nameFromDB = getIntent().getStringExtra("name");

        reference.orderByChild("name").equalTo(nameFromDB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        String address = userSnapshot.child("address").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String contactNumber = userSnapshot.child("contactNumber").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);

                        profileName.setText(name);
                        profileAddress.setText(address);
                        profileEmail.setText(email);
                        profilePassword.setText(password);
                        profileNumber.setText(contactNumber);

                        // Save previous data for comparison
                        previousName = name;

                    }
                } else {
                    Toast.makeText(user_edit_profile.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(user_edit_profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void laundry() {
        String userUsername = profileName.getText().toString().trim();
        reference.orderByChild("name").equalTo(userUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String passwordFromDb = userSnapshot.child("name").getValue(String.class);

                    if (passwordFromDb.equals(userUsername)) {
                        profileName.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String addressFromDB = snapshot.child(userUsername).child("address").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String numberFromDB = snapshot.child(userUsername).child("contactNumber").getValue(String.class);
                        String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                        Intent intent = new Intent(user_edit_profile.this, user_profile.class);
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
            }
        });
    }
}