package com.cs407.skincare;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<Ingredient> ingredientList = IngredientParser.parseCSV(this, "ingredients.csv");
        Log.d("MainActivity", "Number of ingredients parsed: " + ingredientList.size());

        IngredientParser.insertIngredients(db, ingredientList);

    }

}