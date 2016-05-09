package com.example.grimi.cashless;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckPersonActivity extends AppCompatActivity {
    ArrayList<String> currentCheck = new ArrayList<String>();
    ArrayAdapter<String> itemsAdapter;
    ListView checkListView;
    EditText inputTip;
    double tip_per;

    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_personal);
        Button checkout = (Button)findViewById(R.id.button_checkout);
        Bundle extras = getIntent().getExtras();
        currentCheck = extras.getStringArrayList("items");

        inputTip = (EditText)findViewById(R.id.inputTip);
        total = extras.getInt("subtotal");

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currentCheck);
        checkListView = (ListView) findViewById(R.id.personCheck);
        checkListView.setAdapter(itemsAdapter);

        TextView subtotal = (TextView) findViewById(R.id.subtotal);
        subtotal.setText("Subtotal: $" + Integer.toString(total));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double tip_per = Double.parseDouble(inputTip.getText().toString());
                Intent intent = new Intent(CheckPersonActivity.this, PayActivity.class);
                double totalTip = (total*tip_per)/100 + total;
                intent.putExtra("total", Integer.toString(total));
                startActivity(intent);
                Toast.makeText(context, "El es de: " + totalTip, Toast.LENGTH_LONG).show();

            }
        });
    }
}
