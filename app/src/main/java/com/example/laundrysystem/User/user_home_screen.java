package com.example.laundrysystem.User;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class user_home_screen extends AppCompatActivity {

    private Dialog dialog;
    private Button btnCancel, btnAvail;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private TextView profileName;
    private String name, address, contactNumber;

    private ImageButton btn_profile, btn_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_screen);

        dialog = new Dialog(user_home_screen.this);
        dialog.setContentView(R.layout.dialog_insert_data);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnAvail = dialog.findViewById(R.id.btnAvail);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnAvail.setOnClickListener(view -> dialog.dismiss());

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileName = findViewById(R.id.profileName);
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        contactNumber = getIntent().getStringExtra("contactNumber");
        userData();

        ImageButton btn_laundry = findViewById(R.id.btn_laundry);
        ImageButton btn_status = findViewById(R.id.btn_status);
         btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);

        showImg();

        btn_laundry.setOnClickListener(view -> laundry());
        btn_status.setOnClickListener(view -> status());
        btn_history.setOnClickListener(view -> history());
        btn_profile.setOnClickListener(view -> profile());

        setupRecyclerView();
        setupSearchView();



    }

    private void setupRecyclerView() {
        Query query = FirebaseDatabase.getInstance().getReference().child("LaundryOwner");

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(query, MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel model) {
                //Toast.makeText(user_home_screen.this, "Clicked: " + model.getShop_name(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(user_home_screen.this, user_laundry_owner_services_screen.class);
                intent.putExtra("shop_name", model.getShop_name());
                intent.putExtra("location", model.getLocation());
                intent.putExtra("contact_number", model.getContact_number());
                intent.putExtra("username", profileName.getText().toString());
                intent.putExtra("address",address);
                intent.putExtra("contactNumber",contactNumber);
                startActivity(intent);
                intent.removeExtra("name");
                intent.removeExtra("location");
                intent.removeExtra("contact_number");
                intent.removeExtra("username");
                intent.removeExtra("address");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }

    private void userData() {
        String nameUser = getIntent().getStringExtra("name");
        profileName.setText(nameUser);
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
                            btn_profile.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(user_home_screen.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseStorage", "Failed to load image", e);
            }
        });
    }

    private void laundry() {
        // Handle laundry button click
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
                    // Proceed to the user profile screen
                    Intent intent = new Intent(user_home_screen.this, user_home_screen.class);
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

    private void status() {
        // Handle status button click
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
                    // Proceed to the user profile screen
                    Intent intent = new Intent(user_home_screen.this, user_status.class);
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

    private void history() {
        // Handle history button click
       // Intent intent = new Intent(user_home_screen.this, user_history.class);
       // intent.putExtra(name,"name");
        //startActivity(intent);

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
                    // Proceed to the user profile screen
                    Intent intent = new Intent(user_home_screen.this, user_history.class);
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
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String addressFromDB = userSnapshot.child("address").getValue(String.class);
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String numberFromDB = userSnapshot.child("contactNumber").getValue(String.class);
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                    // Proceed to the user profile screen
                    Intent intent = new Intent(user_home_screen.this, user_profile.class);
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

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchLaundryOwner(newText);
                return true;
            }
        });
    }

    private void searchLaundryOwner(String searchText) {
        Query query = FirebaseDatabase.getInstance().getReference().child("LaundryOwner")
                .orderByChild("location")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();

        mainAdapter.updateOptions(options);
        mainAdapter.startListening(); // Ensure the adapter starts listening to the new query
        recyclerView.scrollToPosition(0); // Scroll to top after search
    }
}

