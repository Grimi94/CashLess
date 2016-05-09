package com.example.grimi.cashless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckPersonActivity extends AppCompatActivity {
    ArrayList<String> currentCheck = new ArrayList<String>();
    ArrayAdapter<String> itemsAdapter;
    ListView checkListView;

    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_personal);
        Button checkout = (Button)findViewById(R.id.button_checkout);
        Bundle extras = getIntent().getExtras();
        currentCheck = extras.getStringArrayList("items");
        total = extras.getInt("subtotal");

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currentCheck);
        checkListView = (ListView) findViewById(R.id.personCheck);
        checkListView.setAdapter(itemsAdapter);

        TextView subtotal = (TextView) findViewById(R.id.subtotal);
        subtotal.setText("Subtotal: $" + Integer.toString(total));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckPersonActivity.this, Paypal.class);
                startActivity(intent);
            }
        });
    }
}
