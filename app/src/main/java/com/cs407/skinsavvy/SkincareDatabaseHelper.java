package com.cs407.skinsavvy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SkincareDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SkincareIngredients.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ingredients";
    public static final String COLUMN_NAME = "ingredient_name";
    public static final String COLUMN_ACNE = "acne";
    public static final String COLUMN_OILY = "oily";
    public static final String COLUMN_DRY = "dry";
    public static final String COLUMN_COMBO = "combo";

    public SkincareDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT PRIMARY KEY, " +
                COLUMN_ACNE + " INTEGER, " +
                COLUMN_OILY + " INTEGER, " +
                COLUMN_DRY + " INTEGER, " +
                COLUMN_COMBO + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}


