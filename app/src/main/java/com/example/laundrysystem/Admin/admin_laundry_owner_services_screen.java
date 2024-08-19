package com.example.laundrysystem.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;

public class admin_laundry_owner_services_screen extends AppCompatActivity {

    ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_laundry_owner_services_screen);

        btn_back = (ImageButton) findViewById(R.id.btn_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_laundry_owner_services_screen.this, admin_home_screen.class);
                startActivity(intent);
            }
        });
    }
}
