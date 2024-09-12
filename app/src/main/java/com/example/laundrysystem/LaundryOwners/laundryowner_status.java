package com.example.laundrysystem.LaundryOwners;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundrysystem.R;
import com.example.laundrysystem.User.LaundryMainAdapter;
import com.example.laundrysystem.LaundryOwners.LaundryMainModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class laundryowner_status extends AppCompatActivity {

    RecyclerView recyclerView;
    LaundryMainAdapter mainAdapter;

    Dialog dialog,dialog2;
    Button btnCancel, btnAvail, btnAccept, btnDecline;

    ImageButton btn_laundry, btn_status, btn_history, btn_profile;

    DatabaseReference transactionsRef, historyRef;
    LaundryMainModel selectedModel;
    String selectedModelKey;
    String shopName;
    DatabaseReference userTransactionRef;


    boolean isAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_status_screen);
         //bali muni anay antis sa dialog 2 annd muni nga dialog is batonon ka owner or hindi ang request ni user
        dialog = new Dialog(laundryowner_status.this);
        dialog.setContentView(R.layout.dialog_accept_user_avail);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        btnAccept = dialog.findViewById(R.id.btnAccept);
        btnDecline = dialog.findViewById(R.id.btnDecline);

        dialog2 = new Dialog(laundryowner_status.this);
        dialog2.setContentView(R.layout.dialog_asking_data);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);
        btnCancel = dialog2.findViewById(R.id.btnCancel);
        btnAvail = dialog2.findViewById(R.id.btnAvail);

        shopName = getIntent().getStringExtra("shop_name");
        transactionsRef = FirebaseDatabase.getInstance().getReference().child("Transaction").child(shopName);
        historyRef = FirebaseDatabase.getInstance().getReference().child("History").child(shopName);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedModel != null && selectedModelKey != null) {
                    selectedModel.setStatus("Processing");
                    updateStatus(selectedModelKey, "Processing");
                    updateStatus(selectedModelKey, "Processing");
                    dialog.dismiss();
                }
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedModelKey != null) {
                    transactionsRef.child(selectedModelKey).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userTransactionRef.child(selectedModelKey).removeValue();
                            Toast.makeText(laundryowner_status.this, "⛔ Transaction Declined", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(laundryowner_status.this, "⛔ Failed to Decline Transaction", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        btnAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedModel != null && selectedModelKey != null) {
                    selectedModel.setStatus("Done");
                    dialog2.dismiss();
                    insertUserHistory(selectedModel, selectedModelKey);
                    insertIntoHistoryAndDeleteFromTransactions(selectedModel, selectedModelKey);

                    deleteFromUserTransaction(selectedModelKey);
                }
            }
        });
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<LaundryMainModel> options =
                new FirebaseRecyclerOptions.Builder<LaundryMainModel>()
                        .setQuery(transactionsRef, LaundryMainModel.class)
                        .build();
        mainAdapter = new LaundryMainAdapter(options);

        mainAdapter.setOnItemClickListener(new LaundryMainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LaundryMainModel model, String key) {
                selectedModel = model;
                selectedModelKey = key;

                userTransactionRef = FirebaseDatabase.getInstance().getReference().child("UserTransaction").child(model.getName()).child(model.getName());

                if ("Processing".equals(model.getStatus())) {
                    dialog2.show();
                } else {
                    dialog.show();
                }
            }
        });

        recyclerView.setAdapter(mainAdapter);

        btn_laundry = findViewById(R.id.btn_laundry);
        btn_status = findViewById(R.id.btn_status);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);

        btn_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_status.this, laundryowner_home_screen.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_status.this, laundryowner_status.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_status.this, laundryowner_history.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_status.this, laundryowner_profile.class);
                intent.putExtra("shop_name",shopName);
                startActivity(intent);
            }
        });

    }

    private void insertIntoHistoryAndDeleteFromTransactions(LaundryMainModel model, String key) {
        historyRef.child(key).setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                transactionsRef.child(key).removeValue().addOnCompleteListener(deleteTask -> {
                    if (deleteTask.isSuccessful()) {
                        Toast.makeText(laundryowner_status.this, "\uD83D\uDC4D Transaction Moved to History", Toast.LENGTH_SHORT).show();
                        dialog2.dismiss();
                    } else {
                        Toast.makeText(laundryowner_status.this, "⛔ Failed to Delete Transaction", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void insertUserHistory(LaundryMainModel model, String key) {
        // Retrieve the user's name from the selected model
        String userName = model.getName();  // Assuming `model.getName()` gives the user's name

        // Reference to the specific user's history node
        DatabaseReference userHistoryRef = FirebaseDatabase.getInstance().getReference().child("UserHistory").child(userName);

        // Insert the model into the user's history
        userHistoryRef.child(key).setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(laundryowner_status.this, "\uD83D\uDC4D Transaction Added to User History", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(laundryowner_status.this, "⛔ Failed to Add Transaction to User History", Toast.LENGTH_SHORT).show();
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
    private void updateStatus(String key, String newStatus) {
        transactionsRef.child(key).child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
               // Toast.makeText(laundryowner_status.this, "⛔ Failed to Update Status", Toast.LENGTH_SHORT).show();
               // Log.d("UpdateStatus", "Status updated to " + newStatus + " for key " + key);
            }
            else {
               // Toast.makeText(laundryowner_status.this, "⛔ Failed to Update Status", Toast.LENGTH_SHORT).show();
               // Log.d("UpdateStatus", "Failed to update status for key " + key + ": " + task.getException());
            }
        });

        userTransactionRef.child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
             //   Log.d("UpdateStatus", "UserTransaction status updated to " + newStatus + " for user " + selectedModel.getName());
            } else {
             //   Log.d("UpdateStatus", "Failed to update status for UserTransaction key " + key);
            }
        });
    }

    private void deleteFromUserTransaction(String key) {
        // Reference to the specific user's transaction node
        DatabaseReference userTransactionRef = FirebaseDatabase.getInstance().getReference().child("UserTransaction").child(selectedModel.getName()).child(key);

        // Remove the model from UserTransaction
        userTransactionRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(laundryowner_status.this, "Transaction Removed from UserTransaction", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(laundryowner_status.this, "Failed to Remove Transaction from UserTransaction", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
