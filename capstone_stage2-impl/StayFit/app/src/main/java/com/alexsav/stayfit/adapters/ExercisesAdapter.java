package com.alexsav.stayfit.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.model.Exercises;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder> {

    public static final String JSON_RESULTS = "results";
    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "name";
    public static final String JSON_DESCRIPTION = "description";
    public static final String JSON_CATEGORY = "category";
    public static final String JSON_MUSCLES = "muscles";
    public static final String JSON_EQUIPMENT = "equipment";

    public Context context;
    public ArrayList<Exercises> exercisesList;
    public ArrayList<Exercises> selectedExercisesList;

    public ExercisesAdapter(Context context, String json) {
        this.context = context;
        exercisesList = new ArrayList<>();
        selectedExercisesList = new ArrayList<>();
        extractJson(json);
    }

    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView exercisesLayout = new CardView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);
        exercisesLayout.setLayoutParams(layoutParams);
        exercisesLayout.setContentPadding(16, 26, 26, 16);
        exercisesLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
        exercisesLayout.setRadius(40);
        exercisesLayout.setMaxCardElevation(15);
        exercisesLayout.setCardElevation(8);


        return new ExercisesViewHolder(exercisesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder holder, int i) {

        holder.exercisesId = exercisesList.get(i).getId();

        holder.nameText.setText(exercisesList.get(i).getName());
        holder.nameText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        holder.nameText.setTypeface(Typeface.DEFAULT_BOLD);
        holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.nameText.setPadding(10, 15, 10, 10);

        holder.descriptionText.setText(exercisesList.get(i).getDescription());
        holder.descriptionText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.descriptionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        holder.descriptionText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.descriptionText.setPadding(5, 90, 5, 5);

        final ExercisesViewHolder exercisesViewHolder = holder;
        holder.textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesViewHolder.onViewSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    private void extractJson(String json) {
        try {
            JSONArray exercisesArray = new JSONObject(json).getJSONArray(JSON_RESULTS);
            for (int i = 0; i < exercisesArray.length(); i++) {
                int id = exercisesArray.getJSONObject(i).getInt(JSON_ID);
                String name = exercisesArray.getJSONObject(i).getString(JSON_NAME);
                String description = Html.fromHtml
                        (exercisesArray
                                .getJSONObject(i)
                                .getString(JSON_DESCRIPTION))
                        .toString();

                int categories = exercisesArray.getJSONObject(i).getInt(JSON_CATEGORY);
                ArrayList<Integer> musclesList = new ArrayList<>();
                JSONArray jsonMuscleArray = exercisesArray
                        .getJSONObject(i)
                        .getJSONArray(JSON_MUSCLES);

                for (int j = 0; j < jsonMuscleArray.length(); j++) {
                    musclesList.add(jsonMuscleArray.getInt(j));
                }

                ArrayList<Integer> equipmentsList = new ArrayList<>();
                JSONArray jsonEquipmentsArray = exercisesArray
                        .getJSONObject(i)
                        .getJSONArray(JSON_EQUIPMENT);

                for (int j = 0; j < jsonEquipmentsArray.length(); j++) {
                    musclesList.add(jsonEquipmentsArray.getInt(j));
                }

                Exercises newExercises = new Exercises(
                        id,
                        name,
                        description,
                        categories,
                        musclesList,
                        equipmentsList);
                exercisesList.add(newExercises);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Exercises> getExercisesList() {
        return exercisesList;
    }

    public void setExercisesList(ArrayList<Exercises> exercisesList) {
        this.exercisesList = exercisesList;
    }

    public ArrayList<Exercises> getSelectedExercisesList() {
        return selectedExercisesList;
    }

    public void setSelectedExercisesList(ArrayList<Exercises> selectedExercisesList) {
        this.selectedExercisesList = selectedExercisesList;
    }

    public class ExercisesViewHolder extends RecyclerView.ViewHolder {
        public int exercisesId;
        public CardView textHolder;
        public TextView nameText;
        public TextView descriptionText;
        public boolean selectedExercises;

        public ExercisesViewHolder(CardView view) {
            super(view);
            textHolder = view;
            nameText = new TextView(context);
            descriptionText = new TextView(context);
            textHolder.addView(nameText);
            textHolder.addView(descriptionText);
            selectedExercises = false;
        }

        public void onViewSelected() {
            if (selectedExercises) {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
                selectedExercises = false;
                for (int i = 0; i < selectedExercisesList.size(); i++) {
                    if (selectedExercisesList.get(i).getId() == exercisesId) {
                        selectedExercisesList.remove(i);
                        break;
                    }
                }
            } else {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                selectedExercises = true;
                for (int i = 0; i < exercisesList.size(); i++) {
                    if (exercisesList.get(i).getId() == exercisesId) {
                        selectedExercisesList.add(exercisesList.get(i));
                    }
                }
            }
        }
    }
}
