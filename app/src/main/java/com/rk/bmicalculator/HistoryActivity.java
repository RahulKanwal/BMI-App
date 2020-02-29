package com.rk.bmicalculator;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView lvHistory;
    ArrayList<BMI> s = new ArrayList<>();
    ArrayAdapter<BMI> ad;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistory = (ListView) findViewById(R.id.lvHistory);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("bmi");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                s.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    BMI data = d.getValue(BMI.class);
                    s.add(data);
                }

                ad = new ArrayAdapter<BMI>(HistoryActivity.this, android.R.layout.simple_list_item_1, s);
                lvHistory.setAdapter(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
