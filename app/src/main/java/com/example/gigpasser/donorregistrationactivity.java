package com.example.gigpasser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class donorregistrationactivity extends AppCompatActivity {


    private TextView backButton;
    private CircleImageView profile_image;
    private TextInputEditText registerFullName, registerIdNumber, registerPhoneNumber, leftLensNumber, rightLensNumber, registerEmail, registerPassword;
    private Button registerButton;
    private Spinner lensorframe;


    private Uri resultUri;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorregistrationactivity);

        backButton = findViewById(R.id.backButton);
        profile_image = findViewById(R.id.profile_image);
        registerFullName = findViewById(R.id.registerFullName);
        registerIdNumber = findViewById(R.id.registerIdNumber);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        lensorframe = findViewById(R.id.lensorframe);
        leftLensNumber = findViewById(R.id.leftLensNumber);
        rightLensNumber = findViewById(R.id.rightLensNumber);
        registerButton = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(donorregistrationactivity.this,loginactivity.class);
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
                final String lensOrFrame = lensorframe.getSelectedItem().toString().trim();
                final String lefteye = leftLensNumber.getText().toString().trim();
                final String righteye = rightLensNumber.getText().toString().trim();

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
                if(lensOrFrame.equals("Select the object to donate")){
                    Toast.makeText(donorregistrationactivity.this, "Select the object to donate", Toast.LENGTH_SHORT).show();

                return;
                }
                else{
                    loader.setMessage("Registering you............");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(donorregistrationactivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(donorregistrationactivity.this, error, Toast.LENGTH_SHORT).show();
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
                                userInfo.put("lensOrFrame",lensOrFrame);
                                userInfo.put("lefteye",lefteye);
                                userInfo.put("righteye",righteye);
                                userInfo.put("type","donor");       // look at 6, 21,48
                               userInfo.put("search","donor"+lensOrFrame);

                               userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                   @Override
                                   public void onComplete(@NonNull Task task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(donorregistrationactivity.this, "Data Set Successfully", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(donorregistrationactivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                       }
                                       finish();
                                       loader.dismiss();
                                   }
                               });

                               if(resultUri !=null){
                                   final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                           .child("profile images").child(currentUserId);
                                   Bitmap bitmap = null;

                                   try{
                                       bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);

                                   }catch (IOException e){
                                       e.printStackTrace();
                                   }
                                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                   bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                   byte[] data  = byteArrayOutputStream.toByteArray();
                                   UploadTask uploadTask = filePath.putBytes(data);

                                   uploadTask.addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(donorregistrationactivity.this,"Image Upload failed",Toast.LENGTH_SHORT).show();
                                       }
                                   });

                                   uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                       @Override
                                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                           if(taskSnapshot.getMetadata()!=null) {
                                               Task<Uri>result=taskSnapshot.getStorage().getDownloadUrl();
                                               result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                   @Override
                                                   public void onSuccess(Uri uri) {
                                                       String imageuri=uri.toString();
                                                       Map newImageMap=new HashMap();
                                                       newImageMap.put("profilepictureurl",imageuri);
                                                       userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                           @Override
                                                           public void onComplete(@NonNull Task task) {
                                                               if(task.isSuccessful())
                                                               {
                                                                   Toast.makeText(donorregistrationactivity.this, "Image url added successfully", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                                       finish();
                                                   }
                                               });
                                           }
                                       }
                                   });

                               }


                                Intent intent = new Intent(donorregistrationactivity.this, mainactivity.class);
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