package com.alexsav.stayfit.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.adapters.SavedWorkoutPlanAdapter;
import com.alexsav.stayfit.data.ExercisesProvider;
import com.alexsav.stayfit.data.WorkoutsProvider;
import com.alexsav.stayfit.databinding.FragmentSavedWorkoutPlanBinding;
import com.alexsav.stayfit.model.Exercises;
import com.alexsav.stayfit.model.Workouts;

import java.util.ArrayList;

public class SavedWorkoutPlanFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public FragmentSavedWorkoutPlanBinding mFragmentSavedWorkoutPlanBinding;
    public RecyclerView.Adapter mWorkoutsAdapter;
    public RecyclerView.LayoutManager mWorkoutsLayout;
    public Context mContext;

    public SavedWorkoutPlanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSavedWorkoutPlanBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_saved_workout_plan, container, false);
        mContext = container.getContext();
        mWorkoutsLayout = new GridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false);
        mFragmentSavedWorkoutPlanBinding.recyclerViewSaved.setLayoutManager(mWorkoutsLayout);

        getLoaderManager().initLoader(0, null, this);
        if (getActivity() != null) {
            getActivity().setTitle(getString(R.string.title_saved_workout_plan));
        }
        return mFragmentSavedWorkoutPlanBinding.getRoot();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri workoutUri = WorkoutsProvider.WORKOUT_CONTENT_URI;
        return new CursorLoader(
                getContext(),
                workoutUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ArrayList<Workouts> workoutsArrayList = new ArrayList<>();
        Uri exerciseUri = ExercisesProvider.EXERCISE_CONTENT_URI;
        if (data != null && data.moveToFirst()) {
            while (!data.isAfterLast()) {
                Cursor exerciseCursor = mContext.getContentResolver()
                        .query(exerciseUri,
                                null,
                                null,
                                null,
                                null);
                int workoutId = data.getInt(data.getColumnIndex(WorkoutsProvider.COLUMN_WORKOUT_ID));
                String workoutName = data.getString(data.getColumnIndex(WorkoutsProvider.COLUMN_NAME));
                ArrayList<Exercises> exercisesArrayList = new ArrayList<>();

                if (exerciseCursor != null && exerciseCursor.moveToFirst()) {
                    while (!exerciseCursor.isAfterLast()) {
                        int matchingId = exerciseCursor
                                .getInt(exerciseCursor
                                        .getColumnIndex(WorkoutsProvider.COLUMN_WORKOUT_ID));
                        if (matchingId == workoutId) {
                            String exerciseName = exerciseCursor
                                    .getString(exerciseCursor
                                            .getColumnIndex(WorkoutsProvider.COLUMN_NAME));
                            String description = exerciseCursor
                                    .getString(exerciseCursor
                                            .getColumnIndex(WorkoutsProvider.COLUMN_DESCRIPTION));
                            Exercises newExercises = new Exercises(0, exerciseName,
                                    description, 0, new ArrayList<Integer>(),
                                    new ArrayList<Integer>());
                            exercisesArrayList.add(newExercises);
                        }
                        exerciseCursor.moveToNext();
                    }
                    exerciseCursor.close();
                }
                Workouts newWorkouts = new Workouts(workoutName, exercisesArrayList);
                workoutsArrayList.add(newWorkouts);
                data.moveToNext();
            }
        }
        mWorkoutsAdapter = new SavedWorkoutPlanAdapter(mContext, workoutsArrayList);
        mFragmentSavedWorkoutPlanBinding.recyclerViewSaved.setAdapter(mWorkoutsAdapter);

        if (mWorkoutsAdapter.getItemCount() == 0) {
            mFragmentSavedWorkoutPlanBinding.textViewEmptyWorkoutPlan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }


}
