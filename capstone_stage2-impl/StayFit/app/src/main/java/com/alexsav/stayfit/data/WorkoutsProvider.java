package com.alexsav.stayfit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class WorkoutsProvider extends ContentProvider {

    public static final String PREFIX = "content://";
    public static final String AUTHORITY = "com.alexsav.stayfit.workoutsprovider";
    public static final Uri URI_BASE = Uri.parse(PREFIX + AUTHORITY);
    public static final Uri WORKOUT_CONTENT_URI =
            URI_BASE.buildUpon().appendPath(DbHelper.WORKOUTS_TABLE_NAME).build();

    public static final String INSERT_FAILED = "Failed to insert to ";
    public static final String UNKNOWN_FUNC = "Unsupported Function!";

    public static final String COLUMN_WORKOUT_ID = "workout_id";
    public static final String COLUMN_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";

    public DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor returnCursor;

        returnCursor = db.query(
                DbHelper.WORKOUTS_TABLE_NAME,
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long workoutId = db.insert(DbHelper.WORKOUTS_TABLE_NAME, null, values);

        if (workoutId > 0) {
            Uri resultUri = ContentUris.withAppendedId(uri, workoutId);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        } else {
            throw new SQLException(INSERT_FAILED + uri);
        }
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
        int deletedWorkouts = db.delete(DbHelper.WORKOUTS_TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedWorkouts;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException(UNKNOWN_FUNC);
    }
}
