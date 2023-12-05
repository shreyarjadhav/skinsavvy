package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManualSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_search);

        EditText ingredientsList = findViewById(R.id.ingredientsList);

        String[] values = ingredientsList.toString().split(",");

        Button getResults = findViewById(R.id.getResults);




    }

    public void navigateToResultsPage(View view){
        Intent intent = new Intent(this, ManualResult.class);
        startActivity(intent);
    }

}