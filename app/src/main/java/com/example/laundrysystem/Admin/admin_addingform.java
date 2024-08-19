package com.example.laundrysystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class        admin_addingform extends AppCompatActivity {

    EditText username,email,password,contact_number,shop_name,location;
    Button btn_Add;
    ImageButton btn_back;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_addingform_screen);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        contact_number =(EditText) findViewById(R.id.contact_number);
        shop_name = (EditText) findViewById(R.id.Shops_Name);
        location = (EditText) findViewById(R.id.location);
        btn_Add = (Button) findViewById(R.id.btn_edit_profile_userprofile);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_addingform.this, admin_home_screen.class);
                startActivity(intent);
            }
        });
        btn_Add.setOnClickListener(view -> {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("LaundryOwner");

            String  Username = username.getText().toString(),
                    Email = email.getText().toString(),
                    Password = password.getText().toString(),
                    Contact_Number = contact_number.getText().toString(),
                    Shop_name = shop_name.getText().toString(),
                    Location = location.getText().toString();

            if(Location.contains("iloilo") || Location.contains("ILOILO") || Location.contains("Iloilo") || Location.contains("IloIlo"))
            {
            HelperClass helper = new HelperClass(Username,Email,Password,Shop_name,Location,Contact_Number);

            reference.child(Shop_name).setValue(helper);

            Toast.makeText(admin_addingform.this, "✅ Successfully Added New Laundry!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(admin_addingform.this, admin_home_screen.class);
            startActivity(intent);

            }
            else
            {
                Toast.makeText(admin_addingform.this,"Sorry it is only available in ILOILO for now", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
