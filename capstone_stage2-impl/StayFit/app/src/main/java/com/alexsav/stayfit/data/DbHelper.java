package com.alexsav.stayfit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workouts.db";
    public static final String WORKOUTS_TABLE_NAME = "workouts";
    public static final String EXERCISES_TABLE_NAME = "exercises";
    private static final int VERSION_NUMBER = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WORKOUTS_TABLE = "CREATE TABLE " +
                WORKOUTS_TABLE_NAME +
                " (" +
                WorkoutsProvider.COLUMN_WORKOUT_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutsProvider.COLUMN_NAME + " TEXT" +
                ");";
        final String SQL_CREATE_EXERCISES_TABLE = "CREATE TABLE " +
                EXERCISES_TABLE_NAME +
                " (" +
                WorkoutsProvider.COLUMN_EXERCISE_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutsProvider.COLUMN_NAME + " TEXT, " +
                WorkoutsProvider.COLUMN_DESCRIPTION + " TEXT, " +
                WorkoutsProvider.COLUMN_WORKOUT_ID + " INTEGER" +
                ");";
        db.execSQL(SQL_CREATE_WORKOUTS_TABLE);
        db.execSQL(SQL_CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WORKOUTS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EXERCISES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
