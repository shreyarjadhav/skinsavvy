package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManualSearch extends AppCompatActivity {
    Map<String, Integer> valueMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_search);

        EditText ingredientsList = findViewById(R.id.ingredientsList);

        String[] values = ingredientsList.toString().split(",");

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        SQLiteDatabase surveyDb = new SurveyDatabaseHelper(this).getReadableDatabase();


        try {
            String[] projection = {
                    SurveyDatabaseHelper.COLUMN_OILY,
                    SurveyDatabaseHelper.COLUMN_DRY,
                    SurveyDatabaseHelper.COLUMN_ACNE,
                    SurveyDatabaseHelper.COLUMN_COMBO,
                    SurveyDatabaseHelper.COLUMN_ALLERGIES
            };

            String selection = SurveyDatabaseHelper.COLUMN_USERID + " = ?";
            String[] args = {acct != null ? acct.getId() : ""};

            Cursor cursor = surveyDb.query(
                    SurveyDatabaseHelper.TABLE_SURVEY,
                    projection,
                    selection,
                    args,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {

                int isOily = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_OILY));
                int isDry = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_DRY));
                int isAcne = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ACNE));
                int isCombo = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_COMBO));


                int[] oily = getColumnValues(SkincareDatabaseHelper.COLUMN_OILY);
                int[] dry = getColumnValues(SkincareDatabaseHelper.COLUMN_DRY);
                int[] acne = getColumnValues(SkincareDatabaseHelper.COLUMN_ACNE);
                int[] combo = getColumnValues(SkincareDatabaseHelper.COLUMN_COMBO);

                String[] ingredients = {SkincareDatabaseHelper.COLUMN_INGREDIENT_NAME};
                for(String val : values){
                    for(int i = 0;i<ingredients.length;i++){
                        int def = 0;
                        if(isOily == 1){
                            def = Math.max(0, oily[i]);
                        }
                        if(isDry == 1){
                            def = Math.max(0, dry[i]);
                        }
                        if(isAcne == 1){
                            def = Math.max(0, dry[i]);
                        }
                        if(isCombo == 1){
                            def = Math.max(0, dry[i]);
                        }
                        valueMap.put(val,def);

                    }
                }

            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            surveyDb.close();
        }
    }

    private int[] getColumnValues(String columnName) {
        List<Integer> vals = new ArrayList<>();

        SQLiteDatabase db = new SkincareDatabaseHelper(this).getReadableDatabase();

        try {
            String[] col = {columnName};

            Cursor cursor = db.query(
                    SkincareDatabaseHelper.TABLE_INGREDIENTS,
                    col,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int columnValue = cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
                    vals.add(columnValue);
                } while (cursor.moveToNext());
            }


            if (cursor != null) {
                cursor.close();
            }
        } finally {

            db.close();
        }

        int[] valuesArray = new int[vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            valuesArray[i] = vals.get(i);
        }

        return valuesArray;
    }

    public void navigateToResultsPage(View view){
        Intent intent = new Intent(this, ManualResult.class);
        Map<String, Integer> valueMap = new HashMap<>();
        startActivity(intent);
    }

}