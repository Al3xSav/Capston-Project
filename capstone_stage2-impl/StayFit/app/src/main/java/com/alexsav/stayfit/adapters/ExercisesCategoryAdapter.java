package com.alexsav.stayfit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexsav.stayfit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExercisesCategoryAdapter extends RecyclerView.Adapter<ExercisesCategoryAdapter.ExercisesCategoryViewHolder> {

    public static final String JSON_RESULTS = "results";
    public static final String JSON_NAME = "name";
    public static final String JSON_ID = "id";

    public Context context;
    private ArrayList<Pair<Integer, String>> categoriesList;
    private ArrayList<Integer> selectedIdList;

    public ExercisesCategoryAdapter(Context context, String json) {
        this.context = context;
        categoriesList = new ArrayList<>();
        selectedIdList = new ArrayList<>();
        extractJson(json);
    }

    @NonNull
    @Override
    public ExercisesCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView categoriesLayout = new CardView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);
        categoriesLayout.setLayoutParams(layoutParams);
        categoriesLayout.setContentPadding(50, 150, 50, 150);
        categoriesLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
        categoriesLayout.setRadius(40);
        categoriesLayout.setMaxCardElevation(15);
        categoriesLayout.setCardElevation(8);

        return new ExercisesCategoryViewHolder(categoriesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesCategoryViewHolder holder, int i) {
        Pair<Integer, String> curPair = categoriesList.get(i);
        if (curPair.first != null) {
            holder.categoriesId = curPair.first;
        }

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        holder.categoriesText.setLayoutParams(textParams);
        holder.categoriesText.setText(curPair.second);
        holder.categoriesText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.categoriesText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.categoriesText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

        final ExercisesCategoryViewHolder exercisesCategoryViewHolder = holder;
        holder.textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesCategoryViewHolder.onViewSelected();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    private void extractJson(String json) {
        try {
            JSONArray categoriesJson = new JSONObject(json).getJSONArray(JSON_RESULTS);
            for (int i = 0; i < categoriesJson.length(); i++) {
                String categoriesName = categoriesJson
                        .getJSONObject(i)
                        .getString(JSON_NAME);

                int categoriesId = categoriesJson
                        .getJSONObject(i)
                        .getInt(JSON_ID);

                Pair<Integer, String> categoriesPair = new Pair<>(categoriesId, categoriesName);
                categoriesList.add(categoriesPair);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pair<Integer, String>> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<Pair<Integer, String>> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<Integer> getSelectedIdList() {
        return selectedIdList;
    }

    public void setSelectedIdList(ArrayList<Integer> selectedIdList) {
        this.selectedIdList = selectedIdList;
    }

    public class ExercisesCategoryViewHolder extends RecyclerView.ViewHolder {
        public CardView textHolder;
        public TextView categoriesText;
        public int categoriesId;
        public boolean selectedCategories;

        public ExercisesCategoryViewHolder(CardView view) {
            super(view);
            textHolder = view;
            categoriesText = new TextView(context);
            textHolder.addView(categoriesText);
            selectedCategories = false;
        }

        public void onViewSelected() {
            if (selectedCategories) {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
                selectedCategories = false;
                for (int i = 0; i < selectedIdList.size(); i++) {
                    if (selectedIdList.get(i) == categoriesId) {
                        selectedIdList.remove(i);
                        break;
                    }
                    /*notifyItemChanged(selectedIdList.get(i));
                    selectedIdList = getLayoutPosition();
                    notifyItemChanged(selectedIdList.get(i));*/
                }
            } else {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                selectedCategories = true;
                selectedIdList.add(categoriesId);
            }
        }
    }
}
