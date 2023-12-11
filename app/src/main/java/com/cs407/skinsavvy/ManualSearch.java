package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log; // Added import statement

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManualSearch extends AppCompatActivity {
    String[] key;
    int[] value;
    EditText ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_search);

        ingredientsList = findViewById(R.id.ingredientsList);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("source")) {
            String sourceIdentifier = intent.getStringExtra("source");
            if ("CameraClass".equals(sourceIdentifier)) {
                String result = intent.getStringExtra("ingredients");
                if (result != null) {
                    ingredientsList.setText(result);
                }
            }
        }
    }


    private <T> List<T> getColVal(String columnName, Class<T> valueType) {
        List<T> valuesList = new ArrayList<>();
        SQLiteDatabase db = new SkincareDatabaseHelper(this).getReadableDatabase();

        Cursor cursor = null;
        try {
            String[] col = {columnName};

            cursor = db.query(
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
                    int columnIndex = cursor.getColumnIndexOrThrow(columnName);

                    if (valueType == Integer.class) {
                        int columnValue = cursor.getInt(columnIndex);
                        valuesList.add(valueType.cast(columnValue));
                    } else if (valueType == String.class) {
                        String columnValue = cursor.getString(columnIndex);
                        valuesList.add(valueType.cast(columnValue));
                    }

                } while (cursor.moveToNext());
            } else {
                Log.d("getColumnVal", "No data " + columnName);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return valuesList;
    }


    public void navigateToResultsPage(View view) {

        key = ingredientsList.getText().toString().split(",");
        value = new int[key.length];

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

            //Take care of skin type

            if (cursor != null && cursor.moveToFirst()) {
                int isOily = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_OILY));
                int isDry = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_DRY));
                int isAcne = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ACNE));
                int isCombo = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_COMBO));

                List<Integer> oily = getColVal(SkincareDatabaseHelper.COLUMN_OILY, Integer.class);
                List<Integer> dry = getColVal(SkincareDatabaseHelper.COLUMN_DRY, Integer.class);
                List<Integer> acne = getColVal(SkincareDatabaseHelper.COLUMN_ACNE, Integer.class);
                List<Integer> combo = getColVal(SkincareDatabaseHelper.COLUMN_COMBO, Integer.class);

                List<String> ingredientsL = getColVal(SkincareDatabaseHelper.COLUMN_INGREDIENT_NAME, String.class);
                String[] ingredients = ingredientsL.toArray(new String[0]);

                for (int j = 0; j < key.length; j++) {
                    int def = 0;
                    for (int i = 0; i < ingredients.length; i++) {
                        if (ingredients[i].trim().equals(key[j].trim())) {

                            if (isOily == 1) {
                                if (oily.get(i) == -1) {
                                    def = -1;
                                    break;
                                }
                                def = Math.max(def, oily.get(i));
                            }
                            if (isDry == 1) {
                                if (dry.get(i) == -1) {
                                    def = -1;
                                    break;
                                }
                                def = Math.max(def, dry.get(i));
                            }
                            if (isAcne == 1) {
                                if (acne.get(i) == -1) {
                                    def = -1;
                                    break;
                                }
                                def = Math.max(def, acne.get(i));
                            }
                            if (isCombo == 1) {
                                if (combo.get(i) == -1) {
                                    def = -1;
                                    break;
                                }
                                def = Math.max(def, combo.get(i));
                            }
                        }
                    }
                    value[j] = def;
                }
            }

            //Take care of allergies
            String allergiesData = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ALLERGIES));
            List<String> allergiesList = Arrays.asList(allergiesData.split(","));
            for (int i = 0; i < key.length; i++) {
                for (int j = 0; j < allergiesList.size(); j++) {
                    if (key[i].trim().equals(allergiesList.get(j).trim())) {
                        value[j] = -2;
                    }
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            surveyDb.close();
        }

        Intent intent = new Intent(this, ManualResult.class);
        intent.putExtra("key", key);
        intent.putExtra("val", value);
        startActivity(intent);
    }
}
