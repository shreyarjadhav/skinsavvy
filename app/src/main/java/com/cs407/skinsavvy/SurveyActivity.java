package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SurveyActivity extends AppCompatActivity {
    private Button oilyButton, dryButton, acneProneButton, comboButton;
    private EditText allergiesEditText;
    private String selectedSkinType = "";
    private String allergiesData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // init UI elements
        oilyButton = findViewById(R.id.oily_button);
        dryButton = findViewById(R.id.dry_button);
        acneProneButton = findViewById(R.id.acne_prone_button);
        comboButton = findViewById(R.id.combo_button);
        allergiesEditText = findViewById(R.id.allergies_edit_text);

        // set onClickListeners for skin type buttons
        oilyButton.setOnClickListener(view -> selectSkinType("oily"));
        dryButton.setOnClickListener(view -> selectSkinType("dry"));
        acneProneButton.setOnClickListener(view -> selectSkinType("acne_prone"));
        comboButton.setOnClickListener(view -> selectSkinType("combo"));

        // handle form submission (submit button click)
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> goToHomePage());
    }

    // method to handle skin type selection
    private void selectSkinType(String skinType) {
        selectedSkinType = skinType;
    }

    // method to retrieve allergies and nav to Home Page
    private void goToHomePage() {
        allergiesData = allergiesEditText.getText().toString();

        // nav to Home Page
        Intent intent = new Intent(SurveyActivity.this, HomePage.class);
        intent.putExtra("SKIN_TYPE", selectedSkinType); // pass the selected skin type
        intent.putExtra("ALLERGIES", allergiesData);    // pass allergies data
        startActivity(intent);
        finish();
    }
}
