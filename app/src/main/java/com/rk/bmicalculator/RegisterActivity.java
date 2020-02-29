package com.rk.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView tvPersonalDetails;
    EditText etName, etAge, etPhone;
    RadioGroup rgGender;
    Button btnRegister;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvPersonalDetails = findViewById(R.id.tvPersonalDetails);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        rgGender = findViewById(R.id.rgGender);
        btnRegister = findViewById(R.id.btnRegister);
        sp = getSharedPreferences("myp1", MODE_PRIVATE);

        String n = sp.getString("name", "");

        if (n.length() != 0) {
            Intent a = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(a);
            finish();
        }
        else {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // getting data from form
                    String name = etName.getText().toString();
                    String ageString = etAge.getText().toString();
                    String phone = etPhone.getText().toString();
                    int id = rgGender.getCheckedRadioButtonId();
                    RadioButton rb = findViewById(id);
                    String gender = rb.getText().toString();
                    Pattern pattern = Pattern.compile("^[ A-Za-z]+$");

                    // Validations
                    if (name.length() == 0) {
                        etName.setError("Empty Name");
                        etName.requestFocus();
                        return;
                    }
                    if (name.length() < 2) {
                        etName.setError("Invalid Name");
                        etName.requestFocus();
                        return;
                    }

                    if (!pattern.matcher(name).matches()) {
                        etName.setError("Name Contains Number or Special Characters");
                        etName.requestFocus();
                        return;
                    }

                    if (ageString.length() == 0) {
                        etAge.setError("Empty Age");
                        etAge.requestFocus();
                        return;
                    }
                    if (Integer.parseInt(ageString) < 15 || Integer.parseInt(ageString) > 115) {
                        etAge.setError("Age should be between 15 and 115");
                        etAge.requestFocus();
                        return;
                    }

                    if (phone.length() == 0) {
                        etPhone.setError("Empty Phone Number");
                        etPhone.requestFocus();
                        return;
                    }
                    if (phone.length() != 10) {
                        etPhone.setError("Phone Number Should be of 10 Digits");
                        etPhone.requestFocus();
                        return;
                    }

                    // Storing data in SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", name);
                    editor.putString("age", String.valueOf(Integer.parseInt(ageString)));
                    editor.putString("phone", phone);
                    editor.putString("gender", gender);
                    editor.commit();

                    Intent a = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(a);
                    finish();

                }
            });
        }
    }
}
