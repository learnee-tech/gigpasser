package com.example.gigpasser;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;

        import de.hdodenhof.circleimageview.CircleImageView;

public class doctorregistrationactivity extends AppCompatActivity {

    private TextView backbutton;
    private CircleImageView profile_image ;
    private TextView registerName,registerIdNumber,registerPhoneNumber,registermail,registerpassword,registerhosname;
    private Button registerbutton;
    private Uri resulturi;
    private FirebaseAuth mAuth;
    private DatabaseReference userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorregistrationactivity);
        backbutton=findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(doctorregistrationactivity.this, loginactivity.class);
                startActivity(intent);

            }
        });
        profile_image=findViewById(R.id.profile_image);
        registerIdNumber=findViewById(R.id.registerIdNumber);
        registerPhoneNumber=findViewById(R.id.registerPhoneNumber);
        registermail=findViewById(R.id.registermail);
        registerpassword=findViewById(R.id.registerpassword);
        registerhosname = findViewById(R.id.registerhosname);
        registerbutton = findViewById(R.id.registerbutton);
        ProgressDialog loader = new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                final String email = registermail.getText().toString().trim();
                final String password = registerpassword.getText().toString().trim();
                final String name = registerName.getText().toString().trim();
                final String idnumber = registerIdNumber.getText().toString().trim();
                final String phone = registerPhoneNumber.getText().toString().trim();
                final String hosname=registerhosname.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    registermail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerpassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(hosname)) {
                    registerhosname.setError("age is required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    registerName.setError("Name is required");
                    return;
                }
                if (TextUtils.isEmpty(idnumber)) {
                    registerIdNumber.setError("Id is required");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    registerPhoneNumber.setError("Phone Number is required");
                    return;
                }
                else
                {
                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String error=task.getException().toString();
                                Toast.makeText(doctorregistrationactivity.this,"Error"+error,Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId=mAuth.getCurrentUser().getUid();
                                userdatabase= FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                                HashMap userInfo=new HashMap();
                                userInfo.put("id",currentUserId);
                                userInfo.put("name",name);
                                userInfo.put("email",email);
                                userInfo.put("phone number",phone);
                                userInfo.put("age",hosname);
                                userInfo.put("type","Doctor");

                                userdatabase.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(doctorregistrationactivity.this,"Data set successfully",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(doctorregistrationactivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();

                                        }
                                        finish();
                                        //loader.dismiss();
                                    }
                                });

                                if(resulturi!=null){
                                    final StorageReference filepath= FirebaseStorage.getInstance().getReference().child(currentUserId);
                                    Bitmap bitmap=null;
                                    try {
                                        bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resulturi);
                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                    byte[] data=byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask= filepath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(doctorregistrationactivity.this,"Image Upload failed",Toast.LENGTH_SHORT).show();
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
                                                        userdatabase.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    Toast.makeText(doctorregistrationactivity.this, "Image url added successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent=new Intent(doctorregistrationactivity.this,mainactivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();

                                }
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            resulturi=data.getData();
            profile_image.setImageURI(resulturi);
        }
    }
}