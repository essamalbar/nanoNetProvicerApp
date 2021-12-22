package com.crazyiter.nanonetprovicerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Button backBTN = findViewById(R.id.backBTN);
        backBTN.setOnClickListener(v -> finish());

        TextView title = findViewById(R.id.titleTV);
        title.setText(getIntent().getStringExtra("title"));

        TextView body = findViewById(R.id.errorTV);
        body.setText(getIntent().getStringExtra("body"));


    }

}