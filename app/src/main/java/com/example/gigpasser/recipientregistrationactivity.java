package com.example.gigpasser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class recipientregistrationactivity extends AppCompatActivity {

            private TextView backButton;
    private CircleImageView profile_image;
    private TextInputEditText registerFullName, registerIdNumber, registerPhoneNumber, leftLensNumber, rightLensNumber, registerEmail, registerPassword;
    private Button registerButton;
    private Spinner lensorframerequirement;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipientregistrationactivity);


        backButton = findViewById(R.id.backButton);
        profile_image = findViewById(R.id.profile_image);
        registerFullName = findViewById(R.id.registerFullName);
        registerIdNumber = findViewById(R.id.registerIdNumber);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        lensorframerequirement = findViewById(R.id.lensorframerequirement);
        leftLensNumber = findViewById(R.id.leftLensNumber);
        rightLensNumber = findViewById(R.id.rightLensNumber);
        registerButton = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(recipientregistrationactivity.this,loginactivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String fullName = registerFullName.getText().toString().trim();
                final String idNumber = registerIdNumber.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();
                final String lefteye = leftLensNumber.getText().toString().trim();
                final String righteye = rightLensNumber.getText().toString().trim();
                final String lensOrFrameRequirement = lensorframerequirement.getSelectedItem().toString().trim();

                if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is required");
                }
                if(TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is required");
                }
                if(TextUtils.isEmpty(fullName)){
                    registerFullName.setError("Full Name is required");
                }
                if(TextUtils.isEmpty(idNumber)){
                    registerIdNumber.setError("Id Number is required");
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    registerPhoneNumber.setError("Phone Number is required");
                }
                if(TextUtils.isEmpty(lefteye)){
                    leftLensNumber.setError("Left eye lens number is required");
                }
                if(TextUtils.isEmpty(righteye)){
                    rightLensNumber.setError("Right eye lens number is required");
                }
                if(lensOrFrameRequirement.equals("Select the object required")){
                    Toast.makeText(recipientregistrationactivity.this, "Select the object to donate", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    loader.setMessage("Registering you............");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(recipientregistrationactivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id",currentUserId);
                                userInfo.put("name",fullName);
                                userInfo.put("email",email);
                                userInfo.put("idNumber",idNumber);
                                userInfo.put("phoneNumber",phoneNumber);
                                userInfo.put("lefteye",lefteye);
                                userInfo.put("righteye",righteye);
                                userInfo.put("lensOrFrame",lensOrFrameRequirement);
                                userInfo.put("type","recipient");       // look at 6, 21,48
                                userInfo.put("search","recipient"+lensOrFrameRequirement);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(recipientregistrationactivity.this, "Data Set Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(recipientregistrationactivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                        loader.dismiss();
                                    }
                                });


                                Intent intent = new Intent(recipientregistrationactivity.this, mainactivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }
                        }
                    });
                }

            }
        });


    }
}