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

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentsAdapter.EquipmentsViewHolder> {

    public static final String JSON_RESULTS = "results";
    public static final String JSON_NAME = "name";
    public static final String JSON_ID = "id";

    public Context context;
    private ArrayList<Pair<Integer, String>> equipmentsList;
    private ArrayList<Integer> selectedIdList;

    public EquipmentsAdapter(Context context, String json) {
        this.context = context;
        equipmentsList = new ArrayList<>();
        selectedIdList = new ArrayList<>();
        extractJson(json);
    }

    @NonNull
    @Override
    public EquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView equipmentsLayout = new CardView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(16, 16, 16, 16);
        equipmentsLayout.setLayoutParams(layoutParams);
        equipmentsLayout.setContentPadding(50, 150, 50, 150);
        equipmentsLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
        equipmentsLayout.setMaxCardElevation(15);
        equipmentsLayout.setRadius(40);
        equipmentsLayout.setCardElevation(8);

        return new EquipmentsViewHolder(equipmentsLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentsViewHolder holder, int i) {
        Pair<Integer, String> curPair = equipmentsList.get(i);

        if (curPair.first != null) {
            holder.equipmentsId = curPair.first;
        }

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        holder.equipmentsText.setLayoutParams(textParams);
        holder.equipmentsText.setText(curPair.second);
        holder.equipmentsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        holder.equipmentsText.setTextColor(context.getResources().getColor(R.color.colorButtonText));
        holder.equipmentsText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

        final EquipmentsViewHolder equipmentsViewHolder = holder;
        holder.textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentsViewHolder.onViewSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentsList.size();
    }

    private void extractJson(String json) {
        try {
            JSONArray equipmentsJson = new JSONObject(json).getJSONArray(JSON_RESULTS);
            for (int i = 0; i < equipmentsJson.length(); i++) {
                String equipmentsName = equipmentsJson
                        .getJSONObject(i)
                        .getString(JSON_NAME);

                int equipmentsId = equipmentsJson
                        .getJSONObject(i)
                        .getInt(JSON_ID);
                Pair<Integer, String> equipmentsPair = new Pair<>(equipmentsId, equipmentsName);
                equipmentsList.add(equipmentsPair);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pair<Integer, String>> getEquipmentsList() {
        return equipmentsList;
    }

    public void setEquipmentsList(ArrayList<Pair<Integer, String>> equipmentsList) {
        this.equipmentsList = equipmentsList;
    }

    public ArrayList<Integer> getSelectedIdList() {
        return selectedIdList;
    }

    public void setSelectedIdList(ArrayList<Integer> selectedIdList) {
        this.selectedIdList = selectedIdList;
    }

    public class EquipmentsViewHolder extends RecyclerView.ViewHolder {
        public CardView textHolder;
        public TextView equipmentsText;
        public int equipmentsId;
        public boolean selectedEquipments;

        public EquipmentsViewHolder(CardView view) {
            super(view);
            textHolder = view;
            equipmentsText = new TextView(context);
            textHolder.addView(equipmentsText);
            selectedEquipments = false;
        }

        public void onViewSelected() {
            if (selectedEquipments) {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorButtonHighlight));
                selectedEquipments = false;
                for (int i = 0; i < selectedIdList.size(); i++) {
                    if (selectedIdList.get(i) == equipmentsId) {
                        selectedIdList.remove(i);
                        break;
                    }
                }
            } else {
                textHolder.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                selectedEquipments = true;
                selectedIdList.add(equipmentsId);
            }
        }
    }
}
