package com.cs407.skinsavvy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// SurveyDatabaseHelper.java
public class SurveyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "survey.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SURVEY = "survey";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OILY = "oily";
    public static final String COLUMN_DRY = "dry";
    public static final String COLUMN_ACNE = "acne";
    public static final String COLUMN_COMBO = "combo";
    public static final String COLUMN_ALLERGIES = "allergies";

    public static final String COLUMN_USERID = "userid";



    private static final String CREATE_TABLE_SURVEY =
            "CREATE TABLE " + TABLE_SURVEY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERID + " TEXT, " +
                    COLUMN_OILY + " INTEGER, " +
                    COLUMN_DRY + " INTEGER, " +
                    COLUMN_ACNE + " INTEGER, " +
                    COLUMN_COMBO + " INTEGER, " +
                    COLUMN_ALLERGIES + " TEXT)";

    public SurveyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SURVEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY);
        onCreate(db);
    }
}