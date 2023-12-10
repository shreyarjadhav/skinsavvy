package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ManualResult extends AppCompatActivity {
    private final int[] images = {
            R.drawable.orange,
            R.drawable.red,
            R.drawable.green,
            R.drawable.green
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_result);
        Intent intent = getIntent();

        String[] keyArr = intent.getStringArrayExtra("key");
        int[] valueArr = intent.getIntArrayExtra("val");

        TableLayout tableLayout = findViewById(R.id.table);

        if (keyArr != null && valueArr != null) {
            for (int i = 0; i < Math.min(keyArr.length, valueArr.length); i++) {
                TableRow tableRow = new TableRow(this);
                if(keyArr[i].trim().length() > 17){
                    keyArr[i] = keyArr[i].trim().substring(0,14)+"...";
                }
                TextView keyText = new TextView(this);
                keyText.setText(keyArr[i].trim());
                keyText.setPadding(12, 12, 12, 12);
                keyText.setTextSize(23);
                tableRow.addView(keyText);


                ImageView imageView = new ImageView(this);
                int valueIndex = getIndex(valueArr[i]);
                if (valueIndex >= 0 && valueIndex < images.length) {
                    imageView.setImageResource(images[valueIndex]);
                }

                imageView.setPadding(12, 12, 12, 12);
                imageView.setLayoutParams(new TableRow.LayoutParams(150, 150));
                tableRow.addView(imageView);

                tableLayout.addView(tableRow);



            }
        }
    }
    private int getIndex(int value) {
        switch (value) {
            case -2:
                return 0;
            case -1:
                return 1;
            case 0:
                return 2;
            case 1:
                return 3;
            default:
                return -1;
        }
    }
    public void navigateToHomePage(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
