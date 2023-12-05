package com.cs407.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_ACNE = "acne";
    public static final String COLUMN_OILY = "oily";
    public static final String COLUMN_DRY = "dry";
    public static final String COLUMN_COMBO = "combo";

    private static final String CREATE_TABLE_INGREDIENTS =
            "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INGREDIENT_NAME + " TEXT, " +
                    COLUMN_ACNE + " INTEGER, " +
                    COLUMN_OILY + " INTEGER, " +
                    COLUMN_DRY + " INTEGER, " +
                    COLUMN_COMBO + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(db);
    }
}



