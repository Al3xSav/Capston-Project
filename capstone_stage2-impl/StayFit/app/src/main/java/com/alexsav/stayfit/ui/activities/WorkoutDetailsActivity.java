package com.alexsav.stayfit.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.databinding.ActivityWorkoutDetailsBinding;
import com.alexsav.stayfit.model.Exercises;
import com.alexsav.stayfit.ui.fragments.ExercisesListFragment;
import com.alexsav.stayfit.utils.ConnectionUtils;
import com.alexsav.stayfit.widget.WorkoutWidgetProvider;

import java.util.ArrayList;

import static com.alexsav.stayfit.data.ExercisesProvider.EXERCISE_CONTENT_URI;
import static com.alexsav.stayfit.data.WorkoutsProvider.COLUMN_DESCRIPTION;
import static com.alexsav.stayfit.data.WorkoutsProvider.COLUMN_NAME;
import static com.alexsav.stayfit.data.WorkoutsProvider.COLUMN_WORKOUT_ID;
import static com.alexsav.stayfit.data.WorkoutsProvider.WORKOUT_CONTENT_URI;

public class WorkoutDetailsActivity extends AppCompatActivity {

    public ActivityWorkoutDetailsBinding mActivityWorkoutDetailsBinding;
    public ExercisesListFragment mExercisesListFragment;
    public MenuItem menuSave;
    public MenuItem menuDelete;
    public String mWorkoutName;
    public ArrayList<Exercises> mExercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityWorkoutDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_workout_details);

        mWorkoutName = getIntent().getStringExtra(getString(R.string.workouts_name_extra));
        mExercisesList = getIntent().getParcelableArrayListExtra(getString(R.string.exercises_list_extra));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mWorkoutName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            mExercisesListFragment = new ExercisesListFragment();
            mExercisesListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.workout_details_activity, mExercisesListFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workout_settings, menu);
        menuSave = menu.findItem(R.id.settings_save_workout);
        menuDelete = menu.findItem(R.id.settings_delete_workout);
        if (isSaved()) {
            menuSave.setVisible(false);
            menuDelete.setVisible(true);
        } else {
            menuSave.setVisible(true);
            menuDelete.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.settings_save_workout:
                saveWorkout();
                startActivity(intent);
                return true;
            case R.id.settings_delete_workout:
                deleteWorkout();
                startActivity(intent);
                return true;
            case R.id.settings_add_to_widget:
                addWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
        return true;
    }

    public void saveWorkout() {
        ContentValues mWorkoutValues = new ContentValues();
        mWorkoutValues.put(COLUMN_NAME, mWorkoutName);
        Uri workoutUri = getContentResolver().insert(WORKOUT_CONTENT_URI, mWorkoutValues);
        long parsedId = ContentUris.parseId(workoutUri);
        for (Exercises e : mExercisesList) {
            ContentValues mExerciseValues = new ContentValues();
            mExerciseValues.put(COLUMN_NAME, e.getName());
            mExerciseValues.put(COLUMN_DESCRIPTION, e.getDescription());
            mExerciseValues.put(COLUMN_WORKOUT_ID, parsedId);
            getContentResolver().insert(EXERCISE_CONTENT_URI, mExerciseValues);
            ConnectionUtils.makeSnackBar(mActivityWorkoutDetailsBinding.getRoot(), getString(R.string.toast_saved));
        }
        menuSave.setVisible(false);
        menuDelete.setVisible(true);
    }

    public void deleteWorkout() {
        String WHERE_PARAM = COLUMN_NAME + " = \"" + mWorkoutName + "\"";
        getContentResolver().delete(WORKOUT_CONTENT_URI, WHERE_PARAM, null);
        menuDelete.setVisible(false);
        menuSave.setVisible(true);
        ConnectionUtils.makeSnackBar(mActivityWorkoutDetailsBinding.getRoot(), getString(R.string.toast_delete));
    }

    public void addWidget() {
        Intent widgetIntent = new Intent(this, WorkoutWidgetProvider.class);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetIntent.putExtra(getString(R.string.workouts_name_extra), mWorkoutName);
        widgetIntent.putExtra(getString(R.string.exercises_list_extra), mExercisesList);
        sendBroadcast(widgetIntent);
        ConnectionUtils.makeSnackBar(mActivityWorkoutDetailsBinding.getRoot(), getString(R.string.toast_added_to_widget));
    }

    public boolean isSaved() {
        Cursor cursor = getContentResolver().query(
                WORKOUT_CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String workoutName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                if (mWorkoutName.equals(workoutName)) {
                    return true;
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return false;
    }
}
