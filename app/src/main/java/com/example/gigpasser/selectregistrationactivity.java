package com.example.gigpasser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class selectregistrationactivity extends AppCompatActivity {


    private Button donorButton, recipientButton,doctorButton;
    private TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectregistrationactivity);

        donorButton = findViewById(R.id.donorButton);
        doctorButton = findViewById(R.id.doctorButton);
        recipientButton = findViewById(R.id.recipientButton);
        backButton = findViewById(R.id.backButton);
        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectregistrationactivity.this, recipientregistrationactivity.class);
                startActivity(intent);
            }
        });
        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectregistrationactivity.this, donorregistrationactivity.class);
                startActivity(intent);
            }
        });
        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectregistrationactivity.this, doctorregistrationactivity.class);
                startActivity(intent);
            }
        });
       backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(selectregistrationactivity.this, loginactivity.class);
                startActivity(intent);
            }
        });


    }
}