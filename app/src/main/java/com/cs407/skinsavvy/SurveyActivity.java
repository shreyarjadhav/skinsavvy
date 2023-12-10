package com.cs407.skinsavvy;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SurveyActivity extends AppCompatActivity {
    private Button oilyButton, dryButton, acneProneButton, comboButton;
    private EditText allergiesEditText;
    private boolean isOily = false;
    private boolean isDry = false;
    private boolean isAcne = false;
    private boolean isCombo = false;
    private String allergiesData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadDataTask().execute(); // to load ingredients.csv into database
        setContentView(R.layout.activity_survey);

        // init UI elements
        oilyButton = findViewById(R.id.oily_button);
        dryButton = findViewById(R.id.dry_button);
        acneProneButton = findViewById(R.id.acne_prone_button);
        comboButton = findViewById(R.id.combo_button);
        allergiesEditText = findViewById(R.id.allergies_edit_text);

        // set onClickListeners for skin type buttons
        acneProneButton.setOnClickListener(view -> selectSkinType("acne"));
        oilyButton.setOnClickListener(view -> selectSkinType("oily"));
        dryButton.setOnClickListener(view -> selectSkinType("dry"));
        comboButton.setOnClickListener(view -> selectSkinType("combo"));

        // handle form submission (submit button click)
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> goToProfilePage());
    }

    // method to handle multiple skin type selection
    private void selectSkinType(String skinType) {
        // toggle the boolean value
        switch (skinType) {
            case "oily":
                isOily = !isOily;
                updateButtonColors();
                break;

            case "dry":
                isDry = !isDry;
                updateButtonColors();
                break;

            case "acne":
                isAcne = !isAcne;
                updateButtonColors();
                break;

            case "combo":
                isCombo = !isCombo;
                updateButtonColors();
                break;

            default:
                break;
        }
    }

    // method to update button colors based on boolean variables
    private void updateButtonColors() {
        oilyButton.setBackgroundTintList(
                isOily ? ColorStateList.valueOf(Color.parseColor("#FFFFFF")) : ColorStateList.valueOf(Color.parseColor("#6750A4")));
        oilyButton.setTextColor(isOily ? Color.parseColor("#6750A4") : Color.parseColor("#FFFFFF"));

        dryButton.setBackgroundTintList(
                isDry ? ColorStateList.valueOf(Color.parseColor("#FFFFFF")) : ColorStateList.valueOf(Color.parseColor("#6750A4")));
        dryButton.setTextColor(isDry ? Color.parseColor("#6750A4") : Color.parseColor("#FFFFFF"));

        acneProneButton.setBackgroundTintList(
                isAcne ? ColorStateList.valueOf(Color.parseColor("#FFFFFF")) : ColorStateList.valueOf(Color.parseColor("#6750A4")));
        acneProneButton.setTextColor(isAcne ? Color.parseColor("#6750A4") : Color.parseColor("#FFFFFF"));

        comboButton.setBackgroundTintList(
                isCombo ? ColorStateList.valueOf(Color.parseColor("#FFFFFF")) : ColorStateList.valueOf(Color.parseColor("#6750A4")));
        comboButton.setTextColor(isCombo ? Color.parseColor("#6750A4") : Color.parseColor("#FFFFFF"));
    }

    // method to retrieve allergies and nav to Profile Page and store in survey database
    private void goToProfilePage() {
        allergiesData = allergiesEditText.getText().toString();

        // Convert boolean values to 1s and 0s
        int oilyValue = isOily ? 1 : 0;
        int dryValue = isDry ? 1 : 0;
        int acneValue = isAcne ? 1 : 0;
        int comboValue = isCombo ? 1 : 0;

        String userId = "";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            userId = acct.getId();
        }


        SQLiteDatabase surveyDb = new SurveyDatabaseHelper(this).getWritableDatabase();

        try {
            ContentValues surveyValues = new ContentValues();
            surveyValues.put(SurveyDatabaseHelper.COLUMN_OILY, oilyValue);
            surveyValues.put(SurveyDatabaseHelper.COLUMN_DRY, dryValue);
            surveyValues.put(SurveyDatabaseHelper.COLUMN_ACNE, acneValue);
            surveyValues.put(SurveyDatabaseHelper.COLUMN_COMBO, comboValue);
            surveyValues.put(SurveyDatabaseHelper.COLUMN_ALLERGIES, allergiesData);
            surveyValues.put(SurveyDatabaseHelper.COLUMN_USERID,userId);

            long newRowId = surveyDb.insert(SurveyDatabaseHelper.TABLE_SURVEY, null, surveyValues);

            if (newRowId == -1) {
                Log.e("SurveyDatabaseError", "Error inserting survey data into the database");
            }
        } finally {
            surveyDb.close();

            // Display a Toast message indicating the successful creation of the survey database
            Toast.makeText(SurveyActivity.this, "Survey database created successfully", Toast.LENGTH_SHORT).show();
        }




        SharedPreferences preferences = getSharedPreferences("survey_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("IS_OILY", isOily);
        editor.putBoolean("IS_DRY", isDry);
        editor.putBoolean("IS_ACNE", isAcne);
        editor.putBoolean("IS_COMBO", isCombo);
        editor.putString("ALLERGIES", allergiesData);

        editor.apply();

        Intent intent = new Intent(SurveyActivity.this, ProfilePage.class);
        intent.putExtra("intent_identifier", "survey");
        startActivity(intent);
        finish();

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
            Toast.makeText(SurveyActivity.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
            Log.d("LoadDataTask", "Data loaded successfully");
        }
    }

    private void loadCSVData() throws IOException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("ingredients.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        SQLiteDatabase db = new SkincareDatabaseHelper(this).getWritableDatabase();

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
                    values.put(SkincareDatabaseHelper.COLUMN_INGREDIENT_NAME, ingredientName);
                    values.put(SkincareDatabaseHelper.COLUMN_ACNE, acne);
                    values.put(SkincareDatabaseHelper.COLUMN_OILY, oily);
                    values.put(SkincareDatabaseHelper.COLUMN_DRY, dry);
                    values.put(SkincareDatabaseHelper.COLUMN_COMBO, combo);


                    // Insert with conflict ignore to avoid updating existing data
                    long newRowId = db.insertWithOnConflict(
                            SkincareDatabaseHelper.TABLE_INGREDIENTS,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_IGNORE
                    );

                    if (newRowId == -1) {
                        Log.d("DatabaseInfo", "Data already exists: " + ingredientName);
                    }
                }
            }
        } finally {
            reader.close();
            db.close();
        }
    }

}
