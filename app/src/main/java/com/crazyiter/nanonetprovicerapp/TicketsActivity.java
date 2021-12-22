package com.crazyiter.nanonetprovicerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.TicketsManager;

public class TicketsActivity extends AppCompatActivity {

    private TextView noTV;
    private RecyclerView ticketsRV;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        provider = Provider.getShared(this);
        VolleyManager.REQUEST_QUEUE = Volley.newRequestQueue(getApplicationContext());

        noTV = findViewById(R.id.noTV);
        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        ticketsRV = findViewById(R.id.ticketsRV);
        ticketsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setupDate();
        openTicket();

    }

    private void setupDate() {
        TicketsManager.getMyTickets(provider, tickets -> {
            if (tickets == null || tickets.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                ticketsRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                ticketsRV.setVisibility(View.VISIBLE);

                ticketsRV.setAdapter(new TicketAdapter(this, tickets));
            }
        });
    }

    private void openTicket() {
        try {
            String ticketId = getIntent().getStringExtra("ticketId");
            TicketsManager.findTicket(ticketId, t -> {
                if (t != null)
                    new TicketAnswersBottomDialog(t).show(getSupportFragmentManager());
            });
        } catch (Exception ignored) {
        }
    }
}