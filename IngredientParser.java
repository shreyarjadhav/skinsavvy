package com.cs407.skincare;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IngredientParser {
    public static List<Ingredient> parseCSV(Context context, String fileName) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(context.getAssets().open(fileName))) {
            CSVReader csvReader = new CSVReader(reader);
            String[] line;

            while ((line = csvReader.readNext()) != null) {
                String name = line[0];
                int acne = Integer.parseInt(line[1]);
                int oily = Integer.parseInt(line[2]);
                int dry = Integer.parseInt(line[3]);
                int combo = Integer.parseInt(line[4]);

                Ingredient ingredient = new Ingredient(0, name, acne, oily, dry, combo);
                ingredients.add(ingredient);
            }
        } catch (IOException | NumberFormatException | CsvValidationException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    public static void insertIngredients(SQLiteDatabase db, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put("name", ingredient.getName());
            values.put("acne", ingredient.getAcne());
            values.put("oily", ingredient.getOily());
            values.put("dry", ingredient.getDry());
            values.put("combo", ingredient.getCombo());

            db.insert("ingredients", null, values);
        }
    }
}
