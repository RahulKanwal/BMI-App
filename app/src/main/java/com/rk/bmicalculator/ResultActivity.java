package com.rk.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    Button btnBack, btnShare, btnSave;
    TextView tvResult, tvGuide1, tvGuide2, tvGuide3, tvGuide4;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnSave = findViewById(R.id.btnSave);
        tvResult = findViewById(R.id.tvResult);
        tvGuide1 = findViewById(R.id.tvGuide1);
        tvGuide2 = findViewById(R.id.tvGuide2);
        tvGuide3 = findViewById(R.id.tvGuide3);
        tvGuide4 = findViewById(R.id.tvGuide4);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("bmi");

        Intent a = getIntent();
        Bundle extras = a.getExtras();
        int feets = extras.getInt("feet");
        int inches = extras.getInt("inch");
        final double weight = extras.getDouble("weight");
        double height = (feets + (inches / 12.0)) * 0.3048;
        double bmi = weight / (height * height);
        final String result;
        if (bmi < 18.5) {
            result = "Underweight";
            tvGuide1.setTextColor(Color.parseColor("#ff0000"));
        }
        else if (bmi < 25) {
            result = "Normal";
            tvGuide2.setTextColor(Color.parseColor("#ff0000"));
        }
        else if (bmi < 30) {
            result = "Overweight";
            tvGuide3.setTextColor(Color.parseColor("#ff0000"));
        }
        else {
            result = "Obese";
            tvGuide4.setTextColor(Color.parseColor("#ff0000"));
        }
        DecimalFormat formatter = new DecimalFormat("#.00");
        final String get_value = formatter.format(bmi);
        tvResult.setText("BMI = " + get_value + " and you are " + result);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(a);
                finish();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("myp1", MODE_PRIVATE);
                String name = sp.getString("name", "");
                String age = sp.getString("age", "");
                String phone = sp.getString("phone", "");
                String gender = sp.getString("gender", "");
                String msg = "Name: " + name + "\nAge: " + age + "\nPhone: " + phone + "\nGender: " + gender + "\nBMI: " + get_value + "\nYou are " + result;

                Intent a = new Intent(Intent.ACTION_SEND);
                a.putExtra(Intent.EXTRA_TEXT, msg);
                a.setType("text/plain");
                startActivity(a);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = Calendar.getInstance().getTime().toString();

                BMI b = new BMI(t, weight, get_value, result);
                myRef.push().setValue(b);
                Toast.makeText(ResultActivity.this, "Record Added", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(false);
            }
        });
    }
}
