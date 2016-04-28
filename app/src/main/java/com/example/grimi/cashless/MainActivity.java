package com.example.grimi.cashless;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<ArrayList<String>> tables = new ArrayList<ArrayList<String>>();
    ArrayList<String> restaurants = new ArrayList<String>();
    ArrayList<String> currentTables = new ArrayList<String>();
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<String> spinnerArrayAdapter2;
    int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://cashless.firebaseio.com/");


        ValueEventListener valueEventListener = myFirebaseRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                System.out.println("Success!!!!");
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                ArrayList<String> newRestaurants = new ArrayList<String>();
                tables.clear();
                restaurants.clear();
                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
//                    System.out.println("Restaurants");
                    restaurants.add(restaurantSnapshot.getKey());
//                    System.out.println(restaurantSnapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                    System.out.println(restaurantSnapshot.getKey());  //prints "Do you have data? You'll love Firebase."
                    ArrayList<String> restaurantTables = new ArrayList<String>();
                    for (DataSnapshot contentSnapshot : restaurantSnapshot.getChildren()) {
                        if(contentSnapshot.getKey() == "Mesas"){
                            ArrayList<String> newTables = new ArrayList<String>();
                            for (DataSnapshot tablesSnapshot : contentSnapshot.getChildren()) {
                                newTables.add(tablesSnapshot.getKey());
                            }
                            tables.add(newTables);
                        }
                    }
                }
                System.out.println("----------------");
//                System.out.println(tables);
                Spinner restaurantSpinner = (Spinner) findViewById(R.id.rest);
                if(spinnerArrayAdapter == null) {
                    spinnerArrayAdapter = new ArrayAdapter(MainActivity.this.getApplicationContext(), android.R.layout.simple_spinner_item, restaurants);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    restaurantSpinner.setAdapter(spinnerArrayAdapter);
                } else {
                    spinnerArrayAdapter.notifyDataSetChanged();
                }


                Spinner tablesSpinner = (Spinner) findViewById(R.id.mesa);
                if(spinnerArrayAdapter2 == null) {
//                    System.out.println("Position:");
//                    System.out.println(restaurantSpinner.getSelectedItemPosition());
                    currentTables.addAll(tables.get(selectedIndex));
                    spinnerArrayAdapter2 = new ArrayAdapter(MainActivity.this.getApplicationContext(), android.R.layout.simple_spinner_item, currentTables);
                    spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tablesSpinner.setAdapter(spinnerArrayAdapter2);
                } else {
                    currentTables.clear();
//                    System.out.println(tables);
//                    System.out.println(tables.get(selectedIndex));
                    currentTables.addAll(tables.get(selectedIndex));
                    spinnerArrayAdapter2.notifyDataSetChanged();
                }



            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println("Fail!!!!");
                System.out.println(error);
            }
        });

        Spinner restaurantSpinner = (Spinner) findViewById(R.id.rest);
        restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedIndex = position;
                //currentTables.clear();
                //currentTables.addAll(tables.get(position));
                //  spinnerArrayAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Button checkin = (Button)findViewById(R.id.button_checkin);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner tableSpinner = (Spinner) findViewById(R.id.mesa);
                String selectedTable = (String)tableSpinner.getSelectedItem();
                System.out.print(selectedTable);
                Intent intent = new Intent(MainActivity.this, CheckGroupActivity.class);
                intent.putExtra("table", selectedTable);

                startActivity(intent);
            }
        });
    }

}
