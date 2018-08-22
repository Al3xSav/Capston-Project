package com.alexsav.stayfit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ExercisesProvider extends ContentProvider {

    public static final String PREFIX = "content://";
    public static final String AUTHORITY = "com.alexsav.stayfit.exercisesprovider";
    public static final Uri URI_BASE = Uri.parse(PREFIX + AUTHORITY);
    public static final Uri EXERCISE_CONTENT_URI =
            URI_BASE.buildUpon().appendPath(DbHelper.EXERCISES_TABLE_NAME).build();
    public static final String UNKNOWN_FUNC = "Unsupported Function!";
    public DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long exercisesId = db.insert(DbHelper.EXERCISES_TABLE_NAME, null, values);

        if (exercisesId > 0) {
            Uri result = ContentUris.withAppendedId(uri, exercisesId);
            getContext().getContentResolver().notifyChange(result, null);
            return result;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor returnCursor;

        returnCursor = db.query(
                DbHelper.EXERCISES_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException(UNKNOWN_FUNC);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (null == selection) {
            selection = "1";
        }
        int deletedExercises = db.delete(DbHelper.EXERCISES_TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedExercises;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException(UNKNOWN_FUNC);
    }
}
