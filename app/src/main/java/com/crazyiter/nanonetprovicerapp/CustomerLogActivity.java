package com.crazyiter.nanonetprovicerapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.LogModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerLogActivity extends AppCompatActivity {

    private RecyclerView logRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_log);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        logRV = findViewById(R.id.logRV);
        logRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setupDate();

    }

    private void setupDate() {
        String customerId = getIntent().getStringExtra("item");
        CustomerManager.getCustomer(customerId, c -> {
            JSONArray logs = c.getLogs();
            ArrayList<LogModel> logModels = new ArrayList<>();
            for (int i = logs.length(); i >= 0; i--) {
                try {
                    JSONObject object = logs.getJSONObject(i);
                    logModels.add(new LogModel(
                            object.getString("id"),
                            object.getString("dateTime"),
                            object.getString("body"),
                            object.getString("customerId")
                    ));
                    logRV.setAdapter(new LogsAdapter(this, logModels));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}