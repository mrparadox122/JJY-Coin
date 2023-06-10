package com.jjycoin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.jjycoin.ui.SendCoinsValue;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.Objects;

public class SendCoins extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 200;

    EditText SendMannualy;
    AppCompatButton SendMannuallyNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coins);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            options.setPrompt("Scanner");
            options.setCameraId(0);  // Use a specific camera of the device
            options.setBeepEnabled(true);
            options.setBarcodeImageEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        }

        AppCompatButton scanner_view = findViewById(R.id.scanner_view);
        scanner_view.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            options.setPrompt("Scanner");
            options.setCameraId(0);  // Use a specific camera of the device
            options.setBeepEnabled(true);
            options.setBarcodeImageEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
        SendMannualy = findViewById(R.id.editText);
        SendMannuallyNext = findViewById(R.id.SendMannuallyNext);
        SendMannuallyNext.setOnClickListener(v -> {
            if (!SendMannualy.getText().toString().matches("") || SendMannualy.getText().toString().length() > 4)
            {
                SendCoinsValue.account_Number = SendMannualy.getText().toString();
                startActivity(new Intent(SendCoins.this, SendCoinsValue.class));
            }
            else
            {
                PopupDialog.getInstance(this)
                        .setStyle(Styles.ALERT)
                        .setHeading("Uh-Oh")
                        .setDescription("Invalid Account Number")
                        .setCancelable(false)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera preview
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
                options.setPrompt("Scanner");
                options.setCameraId(0);  // Use a specific camera of the device
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(true);
                barcodeLauncher.launch(options);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(SendCoins.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SendCoins.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    SendCoinsValue.account_Number = result.getContents();
                    startActivity(new Intent(SendCoins.this, SendCoinsValue.class));
                }
            });
}