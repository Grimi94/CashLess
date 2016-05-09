package com.example.grimi.cashless;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckGroupActivity extends AppCompatActivity {
    ArrayList<String> currentCheck = new ArrayList<String>();
    ArrayAdapter<String> itemsAdapter;
    ListView checkListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_grupal);
        Button select = (Button) findViewById(R.id.button_select);

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, currentCheck);
        checkListView = (ListView) findViewById(R.id.groupCheck);
        checkListView.setAdapter(itemsAdapter);
        checkListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CheckGroupActivity.this)
                        .setTitle("Selected Items")
                        .setMessage("Are you sure you want to select these items? You will not be able to change them afterwards")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                Intent intent = new Intent(CheckGroupActivity.this, CheckPersonActivity.class);
                                SparseBooleanArray checked =  checkListView.getCheckedItemPositions();
                                int total = 0;
                                ArrayList<String> currentItems = new ArrayList<String>();
                                for(int i = 0; i < checked.size(); i++){
                                    if(checked.valueAt(i) == true) {
                                        String item = (String)currentCheck.get(i);
                                        int cost = Integer.parseInt(currentCheck.get(i).split("\\$")[1]);
                                        total += cost;
                                        currentItems.add(item);

                                    }
                                }
                                intent.putExtra("subtotal", total);
                                intent.putExtra("items", currentItems);
                                startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        Bundle extras = getIntent().getExtras();
        String table = extras.getString("table");
        String restaurant = extras.getString("restaurant");

        final Firebase myFirebaseRef = new Firebase("https://cashless.firebaseio.com/Restaurant/" + restaurant + "/Mesas");
        TextView titleView = (TextView) findViewById(R.id.restaurantTitle);
        titleView.setText(restaurant);


        ValueEventListener valueEventListener = myFirebaseRef.child(table).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                currentCheck.clear();

                for (DataSnapshot tablesSnapshot : snapshot.getChildren()) {
                    currentCheck.add(tablesSnapshot.getKey().toString() + "    $" + tablesSnapshot.getValue().toString());
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("Fail!!!!");
                System.out.println(error);
            }
        });
    }
}
