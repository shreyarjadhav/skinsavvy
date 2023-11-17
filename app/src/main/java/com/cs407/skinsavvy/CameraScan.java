package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CameraScan extends AppCompatActivity {
    Button capture_button, generate_button;
    TextView data_text;
    private static final int REQUEST_CAMERA_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        capture_button = findViewById(R.id.capture);
        generate_button = findViewById(R.id.ready);
        data_text = findViewById(R.id.text);

        if (ContextCompat.checkSelfPermission(CameraScan.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CameraScan.this, new String[] {
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanned_text = data_text.getText().toString();
                //switch to results page
                //generateResults(scanned_text);
                ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", scanned_text);
                clipBoard.setPrimaryClip(clip);
                Toast.makeText(CameraScan.this, "done", Toast.LENGTH_SHORT);
            }
        });
    }


}