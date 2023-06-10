package com.jjycoin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jjycoin.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Create_an_account extends AppCompatActivity {

    AppCompatEditText DateofBirth;
    EditText fullNameEditText, mobileNumberEditText, dateOfBirthEditText, usernameEditText, passwordEditText;
    AppCompatButton BackButton , SENDOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);
        getSupportActionBar().hide();
        BackButton = findViewById(R.id.backbutton_createaccount);
        SENDOTP = findViewById(R.id.SendOTP);
        fullNameEditText = findViewById(R.id.fullname);
        mobileNumberEditText = findViewById(R.id.mobilenumber);
        dateOfBirthEditText = findViewById(R.id.Dateofbirth);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.Password);

        DateofBirth = findViewById(R.id.Dateofbirth);
        BackButton.setOnClickListener(view -> {super.onBackPressed();});
        DateofBirth.setOnClickListener(view -> {showDatePickerDialog();});
        SENDOTP.setOnClickListener(view -> {
            if (validateFields())
            {
                Toast.makeText(this, "Otp sent", Toast.LENGTH_SHORT).show();
                getotp();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Create_an_account.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int currentYear = calendar.get(Calendar.YEAR);
                int userAge = currentYear - year;
                if (userAge > 18) {
                    Toast.makeText(Create_an_account.this, "User must be above 18 years", Toast.LENGTH_SHORT).show();
                } else {
                    // Format the date and display it in the EditText
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    DateofBirth.setText(simpleDateFormat.format(calendar.getTime()));
                    DateofBirth.setError(null);
                }
            }
        }, year, month, dayOfMonth);

        // Set the maximum date to 18 years back
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, -18);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        // Show the date picker
        datePickerDialog.show();
    }
    private boolean validateFields() {
        String fullname = fullNameEditText.getText().toString().trim();
        String mobilenumber = mobileNumberEditText.getText().toString().trim();
        String Dateofbirth = dateOfBirthEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (fullname.isEmpty()) {
            fullNameEditText.setError("Full Name is required");
            fullNameEditText.requestFocus();
            return false;
        }

        if (mobilenumber.isEmpty()) {
            mobileNumberEditText.setError("Mobile Number is required");
            mobileNumberEditText.requestFocus();
            return false;
        }

        if (Dateofbirth.isEmpty()) {
            dateOfBirthEditText.setError("Date of Birth is required");
            dateOfBirthEditText.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    private void signupApi() {
        if (!validateFields()) {
            return;
        }
        // Get values from fields
        String fullname = fullNameEditText.getText().toString().trim();
        String mobilenumber = mobileNumberEditText.getText().toString().trim();
        String Dateofbirth = dateOfBirthEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Create parameters and data arrays
        String[] field = new String[5];
        field[0] = "fullname";
        field[1] = "mobilenumber";
        field[2] = "Dateofbirth";
        field[3] = "username";
        field[4] = "password";

        String[] data = new String[5];
        data[0] = fullname;
        data[1] = mobilenumber;
        data[2] = Dateofbirth;
        data[3] = username;
        data[4] = password;

        // Call the API in a separate thread
        new Thread(() -> {
            PutData putData = new PutData("https://jjysmartbusiness.com/_API/Signup.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    Log.i(TAG, result);
                }
            }
        }).start();
    }
    public void getotp()
    {
// Create a LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();

// Inflate the custom layout for the dialog
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);

// Get the edit text view from the inflated layout
        final EditText etOtp = alertLayout.findViewById(R.id.et_otp);

// Build the AlertDialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter OTP")
                .setView(alertLayout)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the input text from the edit text view
                        String otp = etOtp.getText().toString().trim();

                        if (otp.length() < 6) {
                            // Show an error message to enter correct OTP
                            etOtp.setError("Enter a correct OTP");
                        } else {
                            // Do something with the input text
                            signupApi();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

// Show the dialog
        dialog.show();

    }






}