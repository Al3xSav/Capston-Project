package com.alexsav.stayfit.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.model.Exercises;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class WorkoutWidgetProvider extends AppWidgetProvider {

    private static String mWorkoutName = "";
    private static ArrayList<Exercises> mExercisesList = new ArrayList<Exercises>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetTitle = mWorkoutName;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_workout_provider);
        if (!mWorkoutName.equals("") && !mExercisesList.isEmpty()) {
            views.setTextViewText(R.id.widget_workout_name, widgetTitle);
            views.setTextViewText(R.id.widget_exercises_text,
                    mExercisesList.get(0).getDescription());
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String workoutNameExtra = context.getString(R.string.workouts_name_extra);
        String exercisesListExtra = context.getString(R.string.exercises_list_extra);

        if (intent.hasExtra(workoutNameExtra) && intent.hasExtra(exercisesListExtra)) {
            mWorkoutName = intent.getStringExtra(workoutNameExtra);
            mExercisesList = intent.getParcelableArrayListExtra(exercisesListExtra);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context.getPackageName(),
                    WorkoutWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

