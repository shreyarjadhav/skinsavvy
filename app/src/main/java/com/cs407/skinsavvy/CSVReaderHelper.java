//package com.cs407.skinsavvy;
//
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.opencsv.CSVReader;
//import com.opencsv.exceptions.CsvException;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.List;
//
//public class CSVReaderHelper {
//
//    public static void readCSVAndInsertIntoDatabase(Context context) {
//        SkincareDatabaseHelper databaseHelper = new SkincareDatabaseHelper(context);
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//
//        try {
//            // Update the path to your CSV file
//            String csvFilePath = "C:\\Users\\Sofea\\Documents\\CS407\\Labs\\skinsavvy\\ingredients.csv";
//            CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
//            List<String[]> csvData = csvReader.readAll();
//
//            for (String[] row : csvData) {
//                ContentValues values = new ContentValues();
//                values.put(SkincareDatabaseHelper.COLUMN_NAME, row[0]);
//                values.put(SkincareDatabaseHelper.COLUMN_ACNE, Integer.parseInt(row[1]));
//                values.put(SkincareDatabaseHelper.COLUMN_OILY, Integer.parseInt(row[2]));
//                values.put(SkincareDatabaseHelper.COLUMN_DRY, Integer.parseInt(row[3]));
//                values.put(SkincareDatabaseHelper.COLUMN_COMBO, Integer.parseInt(row[4]));
//
//                db.insert(SkincareDatabaseHelper.TABLE_NAME, null, values);
//            }
//
//            // Log the data to the terminal
//            logDataFromDatabase(db);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CsvException e) {
//            throw new RuntimeException(e);
//        } finally {
//            db.close();
//        }
//    }
//
//    private static void logDataFromDatabase(SQLiteDatabase db) {
//        // Define the columns you want to retrieve
//        String[] projection = {
//                SkincareDatabaseHelper.COLUMN_NAME,
//                SkincareDatabaseHelper.COLUMN_ACNE,
//                SkincareDatabaseHelper.COLUMN_OILY,
//                SkincareDatabaseHelper.COLUMN_DRY,
//                SkincareDatabaseHelper.COLUMN_COMBO
//        };
//
//        // Perform a query on the database
//        Cursor cursor = db.query(
//                SkincareDatabaseHelper.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        // Log the data to the terminal
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndexOrThrow(SkincareDatabaseHelper.COLUMN_NAME));
//            int acne = cursor.getInt(cursor.getColumnIndexOrThrow(SkincareDatabaseHelper.COLUMN_ACNE));
//            int oily = cursor.getInt(cursor.getColumnIndexOrThrow(SkincareDatabaseHelper.COLUMN_OILY));
//            int dry = cursor.getInt(cursor.getColumnIndexOrThrow(SkincareDatabaseHelper.COLUMN_DRY));
//            int combo = cursor.getInt(cursor.getColumnIndexOrThrow(SkincareDatabaseHelper.COLUMN_COMBO));
//
//            // Log the data
//            Log.d("DatabaseOutput", "Name: " + name + ", Acne: " + acne + ", Oily: " + oily +
//                    ", Dry: " + dry + ", Combo: " + combo);
//        }
//
//        cursor.close();
//    }
//}
//
//
