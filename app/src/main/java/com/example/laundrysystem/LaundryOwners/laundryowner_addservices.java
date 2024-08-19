package com.example.laundrysystem.LaundryOwners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class laundryowner_addservices extends AppCompatActivity {

    Button btn_next;
    ImageButton imageButton;
    DatabaseReference db;
    CheckBox Wash, Dry, Fold, Fabric, Detergent;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryowner_addservices_home_screen);
        imageButton = findViewById(R.id.imageButton);
        btn_next = findViewById(R.id.btn_next);

        Wash = findViewById(R.id.chkb_Wash);
        Dry = findViewById(R.id.chkb_Dry);
        Fold = findViewById(R.id.chkb_Fold);
        Fabric = findViewById(R.id.chkb_Fabric);
        Detergent = findViewById(R.id.chkb_Detergent);

        shopName = getIntent().getStringExtra("shop_name");//pasa ta sa pricelist kag insert sa database

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServicesChecker();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(laundryowner_addservices.this, laundryowner_home_screen.class);
                startActivity(intent);
            }
        });
    }

    private void ServicesChecker() {
        CheckBox[] holders = {Wash, Dry, Fold, Fabric, Detergent};
        ArrayList<String> services = new ArrayList<>();

        for (CheckBox holder : holders) {
            if (holder.isChecked()) {
                services.add(holder.getText().toString());
            }
        }

        Toast.makeText(laundryowner_addservices.this, "\uD83D\uDC4F You Are Almost Set!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(laundryowner_addservices.this, laundryowner_pricelist.class);
        intent.putStringArrayListExtra("services", services);
        intent.putExtra("shop_name", shopName);
        startActivity(intent);
    }
}
