package com.cs407.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadData = findViewById(R.id.btnLoadData);
        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                loadCSVData();
            } catch (IOException e) {
                Log.e("LoadDataTask", "Error loading CSV data: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
            Log.d("LoadDataTask", "Data loaded successfully");
        }
    }

    private void loadCSVData() throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("ingredients.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        try {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData.length == 5) {
                    String ingredientName = rowData[0];
                    int acne = Integer.parseInt(rowData[1]);
                    int oily = Integer.parseInt(rowData[2]);
                    int dry = Integer.parseInt(rowData[3]);
                    int combo = Integer.parseInt(rowData[4]);

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_INGREDIENT_NAME, ingredientName);
                    values.put(DatabaseHelper.COLUMN_ACNE, acne);
                    values.put(DatabaseHelper.COLUMN_OILY, oily);
                    values.put(DatabaseHelper.COLUMN_DRY, dry);
                    values.put(DatabaseHelper.COLUMN_COMBO, combo);

                    long newRowId = db.insert(DatabaseHelper.TABLE_INGREDIENTS, null, values);

                    if (newRowId == -1) {
                        Log.e("DatabaseError", "Error inserting data into the database");
                    }
                }
            }
        } finally {
            reader.close();
            db.close();
        }
    }


}


