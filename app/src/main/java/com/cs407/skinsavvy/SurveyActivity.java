package com.cs407.skinsavvy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

    // method to retrieve allergies and nav to Profile Page
    // TODO store in survey database
    private void goToProfilePage() {
        allergiesData = allergiesEditText.getText().toString();

        // intent to nav to Profile Page
        Intent intent = new Intent(SurveyActivity.this, ProfilePage.class);

        // pass selected skin types to Profile Page
        intent.putExtra("IS_OILY", isOily);
        intent.putExtra("IS_DRY", isDry);
        intent.putExtra("IS_ACNE", isAcne);
        intent.putExtra("IS_COMBO", isCombo);
        intent.putExtra("ALLERGIES", allergiesData);    // pass allergies data Profile Page

        // start ProfilePage
        startActivity(intent);
        finish();   // finish SurveyActivity
    }
}
