package com.example.laundrysystem.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.common.api.Status;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laundrysystem.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_user extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

//    public Button btn_sign_up;
    EditText name,email,password,contact_number, address;
    Button btn_sign_up;
    FirebaseDatabase database;
    DatabaseReference reference;

    ImageButton imageButton;

    CheckBox checkBox;
    GoogleApiClient googleApiClient;

    String SiteKey = "6LdakwsqAAAAAEudY-9rAk1lp0s0dJjwg-p_qxYw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        contact_number = findViewById(R.id.contact_number);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        imageButton =  (ImageButton) findViewById(R.id.imageButton);

//        Google Recaptcha
        checkBox = findViewById(R.id.CheckBox_Id);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if((status!=null) && status.isSuccess()){
                                        Toast.makeText(signup_user.this, "Successfully Verified!", Toast.LENGTH_SHORT).show();
                                        checkBox.setTextColor(Color.BLUE);
                                    }
                                }
                            });
                }else{
                    checkBox.setTextColor(Color.RED);
                    Toast.makeText(signup_user.this, "Oops, Verification Not Done Yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                        .addConnectionCallbacks(signup_user.this)
                                .build();
        googleApiClient.connect();

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateUsername() | !validatePassword() | !validateAddress() | !validateEmail() | !validateContactNumber() | !validateRecaptcha()){
                    // Handle validation failure

                    return;
                }else{
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    String Name = name.getText().toString(),
                            Email = email.getText().toString(),
                            Password = password.getText().toString(),
                            Contact = contact_number.getText().toString(),
                            Address = address.getText().toString();

                    HelperClass helperClass = new HelperClass(Name,Email,Password,Contact,Address);
                    reference.child(Name).setValue(helperClass);

                    Toast.makeText(signup_user.this, "Your Have Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signup_user.this, user.class);
                    startActivity(intent);
                }


            }
        });



//        Button Back
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup_user.this, user.class);
                startActivity(intent);
            }
        });
    }


    public Boolean validateUsername(){
        String val = name.getText().toString();
        if(val.isEmpty()){
            name.setError("Username cannot be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }
    public Boolean validateEmail(){
        String val = email.getText().toString();
        if(val.isEmpty()){
            email.setError("Email cannot be empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = password.getText().toString();
        if(val.isEmpty()){
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
    public Boolean validateContactNumber(){
        String val = contact_number.getText().toString();
        if(val.isEmpty()){
            contact_number.setError("Contact Number cannot be empty");
            return false;
        } else {
            contact_number.setError(null);
            return true;
        }
    }
    public Boolean validateAddress(){
        String val = address.getText().toString();
        if(val.isEmpty()){
            address.setError("Address cannot be empty");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    public Boolean validateRecaptcha(){
        String val = checkBox.getText().toString();
        if(val.isEmpty()){
            checkBox.setError("Recaptcha cannot be leave!");
            return  false;
        }else {
            checkBox.setError(null);
            return true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
