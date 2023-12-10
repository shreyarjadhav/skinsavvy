package com.cs407.skinsavvy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Get Hi message
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String name = "";
        TextView hello = findViewById(R.id.hello);

        if (acct != null) {
            name = acct.getDisplayName();
            hello.setText("Hi, " + name + "!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the Google Sign-In account
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        // Access the survey database to get user selections
        SQLiteDatabase surveyDb = new SurveyDatabaseHelper(this).getReadableDatabase();

        try {
            String[] projection = {
                    SurveyDatabaseHelper.COLUMN_OILY,
                    SurveyDatabaseHelper.COLUMN_DRY,
                    SurveyDatabaseHelper.COLUMN_ACNE,
                    SurveyDatabaseHelper.COLUMN_COMBO,
                    SurveyDatabaseHelper.COLUMN_ALLERGIES
            };

            // Update the selection and args to include the user ID
            String selection = SurveyDatabaseHelper.COLUMN_USERID + " = ?";
            String[] args = {acct != null ? acct.getId() : ""};

            // Query the database for the user's survey data
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
                String allergiesData = cursor.getString(cursor.getColumnIndexOrThrow(SurveyDatabaseHelper.COLUMN_ALLERGIES));


                TextView skintype = findViewById(R.id.skinTypeTextView);
                List<String> skinTypes = new ArrayList<>();
                if (isOily == 1) {
                    skinTypes.add("Oily");
                }
                if (isDry == 1) {
                    skinTypes.add("Dry");
                }
                if (isAcne == 1) {
                    skinTypes.add("Acne");
                }
                if (isCombo == 1) {
                    skinTypes.add("Combination");
                }
                String skintypeText = "Skin Type: " + TextUtils.join(", ", skinTypes);
                skintype.setText(skintypeText);

                TextView allergiesTextView = findViewById(R.id.allergiesTextView);
                List<String> allergiesList = Arrays.asList(allergiesData.split(","));
                StringBuilder allergiesText = new StringBuilder("Here is a list of ingredients that you are allergic to:\n");
                for (String allergy : allergiesList) {
                    allergiesText.append("• ").append(allergy.trim()).append("\n");
                }
                allergiesTextView.setText(allergiesText.toString());
            }

            if (cursor != null) {
                cursor.close();
            }
        } finally {
            surveyDb.close();
        }
    }



    public void navigateToHomePage(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
