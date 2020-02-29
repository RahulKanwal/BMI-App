package com.rk.bmicalculator;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView tvTemp, tvWelcome, tvHeight, tvFeet, tvInch, tvWeight;
    EditText etWeight;
    Spinner spnFeet, spnInch;
    Button btnCalculate, btnHistory;
    SharedPreferences sp;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTemp = findViewById(R.id.tvTemp);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvHeight = findViewById(R.id.tvHeight);
        tvFeet = findViewById(R.id.tvFeet);
        tvInch = findViewById(R.id.tvInch);
        tvWeight = findViewById(R.id.tvWeight);
        etWeight = findViewById(R.id.etWeight);
        spnFeet = findViewById(R.id.spnFeet);
        spnInch = findViewById(R.id.spnInch);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnHistory = findViewById(R.id.btnHistory);
        sp = getSharedPreferences("myp1", MODE_PRIVATE);
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(2000);
        locationRequest.setInterval(4000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            tvTemp.setText("Temperature not found :(\nGive location permission in Settings");
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    try {
                        double lat = locationResult.getLastLocation().getLatitude();
                        double lon = locationResult.getLastLocation().getLongitude();
                        MyATask t1 = new MyATask();

                        String web = "http://api.openweathermap.org/data/2.5" + "/weather?units=metric&";
                        String que = "lat="+lat+"&lon="+lon;
                        String api = "&appid=c6e315d09197cec231495138183954bd";
                        String info = web + que + api;
                        t1.execute(info);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, getMainLooper());
        }

        String name = sp.getString("name", "");
        tvWelcome.setText("Welcome " + name);

        final ArrayList<Integer> feets = new ArrayList<>();
        feets.add(0);
        feets.add(1);
        feets.add(2);
        feets.add(3);
        feets.add(4);
        feets.add(5);
        feets.add(6);
        feets.add(7);
        feets.add(8);
        feets.add(9);
        feets.add(10);
        feets.add(11);
        feets.add(12);
        ArrayAdapter arrayAdapterFeets = new ArrayAdapter(this, android.R.layout.simple_spinner_item, feets);
        spnFeet.setAdapter(arrayAdapterFeets);

        final  ArrayList<Integer> inches = new ArrayList<>();
        inches.add(0);
        inches.add(1);
        inches.add(2);
        inches.add(3);
        inches.add(4);
        inches.add(5);
        inches.add(6);
        inches.add(7);
        inches.add(8);
        inches.add(9);
        inches.add(10);
        inches.add(11);
        ArrayAdapter arrayAdapterInches = new ArrayAdapter(this, android.R.layout.simple_spinner_item, inches);
        spnInch.setAdapter(arrayAdapterInches);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pf = spnFeet.getSelectedItemPosition();
                int f = feets.get(pf);
                int pi = spnInch.getSelectedItemPosition();
                int i = inches.get(pi);

                if (f == 0 && i == 0) {
                    Toast.makeText(MainActivity.this, "Height cannot be zero", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etWeight.getText().toString().length() == 0 || Double.parseDouble(etWeight.getText().toString()) == 0) {
                    etWeight.setError("Weight Empty or Equal to Zero");
                    etWeight.requestFocus();
                    return;
                }

                double w = Double.parseDouble(etWeight.getText().toString());

                Intent a = new Intent(MainActivity.this, ResultActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("feet", f);
                extras.putInt("inch", i);
                extras.putDouble("weight", w);
                a.putExtras(extras);
                startActivity(a);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(a);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.aboutUs == item.getItemId())  {
            Toast.makeText(this, "App made by One and Only Rahul !!! :)", Toast.LENGTH_LONG).show();
        }

        if (R.id.website == item.getItemId()) {
            Intent a = new Intent(Intent.ACTION_VIEW);
            a.setData(Uri.parse("http://" + "www.google.com"));
            startActivity(a);
        }
        return super.onOptionsItemSelected(item);
    }

    class MyATask extends AsyncTask<String, Void, Double> {
        double temp;

        @Override
        protected Double doInBackground(String... strings) {
            String json = "", line = "";

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    json = json + line + "\n";
                }
                JSONObject o = new JSONObject(json);
                JSONObject p = o.getJSONObject("main");
                temp = p.getDouble("temp");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return temp;
        }
        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvTemp.setText("Curent Temperature: " + aDouble);
        }
    }
}
