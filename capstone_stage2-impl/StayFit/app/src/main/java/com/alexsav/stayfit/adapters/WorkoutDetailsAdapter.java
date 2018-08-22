package com.alexsav.stayfit.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.model.Exercises;
import com.alexsav.stayfit.ui.activities.WorkoutDetailsActivity;
import com.alexsav.stayfit.ui.fragments.ExercisesImageFragment;

import java.util.ArrayList;

public class WorkoutDetailsAdapter extends RecyclerView.Adapter<WorkoutDetailsAdapter.WorkoutDetailsViewHolder> {

    public Context context;
    private ArrayList<Exercises> exercisesList;

    public WorkoutDetailsAdapter(Context context, ArrayList<Exercises> exercisesList) {
        this.context = context;
        this.exercisesList = exercisesList;
    }

    @NonNull
    @Override
    public WorkoutDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView exercisesLayout = new CardView(context);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);
        exercisesLayout.setLayoutParams(layoutParams);
        exercisesLayout.setContentPadding(5, 5, 5, 5);
        exercisesLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
        exercisesLayout.setMaxCardElevation(15);
        exercisesLayout.setRadius(40);
        exercisesLayout.setCardElevation(8);

        return new WorkoutDetailsAdapter.WorkoutDetailsViewHolder(exercisesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkoutDetailsViewHolder holder, int i) {

        holder.nameText.setText(exercisesList.get(i).getName());
        holder.nameText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        holder.nameText.setTypeface(Typeface.DEFAULT_BOLD);
        holder.nameText.setPadding(10, 15, 10, 10);

        holder.descriptionText.setText(exercisesList.get(i).getDescription());
        holder.descriptionText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.descriptionText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.descriptionText.setPadding(5, 80, 5, 60);

        holder.imageButton.setVisibility(View.GONE);

        if (exercisesList.get(i).getImagesUrlList().size() != 0) {
            FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL);
            imageParams.setMargins(16, 16, 16, 16);
            holder.imageButton.setLayoutParams(imageParams);
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.imageButton.setText(context.getString(R.string.button_hint_exercise));
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExercisesImageFragment exercisesImageFragment = new ExercisesImageFragment();
                    Bundle exercisesInfo = new Bundle();
                    exercisesInfo.putStringArrayList(
                            context.getString(R.string.images_list_extra),
                            exercisesList.get(holder.getAdapterPosition()).getImagesUrlList());
                    exercisesImageFragment.setArguments(exercisesInfo);
                    ((WorkoutDetailsActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.workout_details_activity, exercisesImageFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (exercisesList != null && !exercisesList.isEmpty()) {
            return exercisesList.size();
        } else {
            return 0;
        }
    }

    public class WorkoutDetailsViewHolder extends RecyclerView.ViewHolder {
        private CardView textHolder;
        private TextView nameText;
        private TextView descriptionText;
        private Button imageButton;

        public WorkoutDetailsViewHolder(CardView view) {
            super(view);
            textHolder = view;
            nameText = new TextView(context);
            descriptionText = new TextView(context);
            imageButton = new Button(context);
            textHolder.addView(nameText);
            textHolder.addView(descriptionText);
            textHolder.addView(imageButton);
        }
    }
}
