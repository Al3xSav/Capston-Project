package com.alexsav.stayfit.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.model.Workouts;
import com.alexsav.stayfit.ui.activities.WorkoutDetailsActivity;

import java.util.ArrayList;

public class SavedWorkoutPlanAdapter extends RecyclerView.Adapter<SavedWorkoutPlanAdapter.SavedWorkoutPlanAdapterViewHolder> {

    public Context context;
    private ArrayList<Workouts> workoutPlanList;

    public SavedWorkoutPlanAdapter(Context context, ArrayList<Workouts> workoutPlanList) {
        this.context = context;
        this.workoutPlanList = workoutPlanList;
    }

    @NonNull
    @Override
    public SavedWorkoutPlanAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView workoutPlanLayout = new CardView(context);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);
        workoutPlanLayout.setLayoutParams(layoutParams);
        workoutPlanLayout.setContentPadding(16, 36, 26, 36);
        workoutPlanLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
        workoutPlanLayout.setRadius(40);
        workoutPlanLayout.setMaxCardElevation(15);
        workoutPlanLayout.setCardElevation(8);

        return new SavedWorkoutPlanAdapterViewHolder(workoutPlanLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedWorkoutPlanAdapterViewHolder holder, final int i) {

        holder.nameText.setText(workoutPlanList.get(i).getName());
        holder.nameText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        holder.nameText.setTypeface(Typeface.DEFAULT_BOLD);
        holder.nameText.setPadding(10, 10, 10, 10);

        holder.workoutPlanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkoutDetailsActivity.class);
                intent.putExtra(
                        context.getString(R.string.workouts_name_extra),
                        workoutPlanList.get(i).getName());
                intent.putParcelableArrayListExtra(
                        context.getString(R.string.exercises_list_extra),
                        workoutPlanList.get(i).getExercisesList());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (workoutPlanList != null) {
            return workoutPlanList.size();
        }
        return 0;
    }

    public class SavedWorkoutPlanAdapterViewHolder extends RecyclerView.ViewHolder {
        private CardView workoutPlanLayout;
        private TextView nameText;

        public SavedWorkoutPlanAdapterViewHolder(CardView view) {
            super(view);
            workoutPlanLayout = view;
            nameText = new TextView(context);
            workoutPlanLayout.addView(nameText);
        }
    }

}
