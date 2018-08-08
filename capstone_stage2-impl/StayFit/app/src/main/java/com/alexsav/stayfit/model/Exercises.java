package com.alexsav.stayfit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Exercises implements Parcelable {

    public static final Parcelable.Creator<Exercises> CREATOR = new Parcelable.Creator<Exercises>() {
        @Override
        public Exercises createFromParcel(Parcel parcel) {
            return new Exercises(parcel);
        }

        @Override
        public Exercises[] newArray(int size) {
            return new Exercises[size];
        }
    };
    private int id;
    private String name;
    private String description;
    private int exercisesCategory;
    private ArrayList<Integer> musclesList;
    private ArrayList<Integer> equipmentsList;
    private ArrayList<String> imagesUrlList;

    public Exercises(int id, String name, String description, int exercisesCategory,
                     ArrayList<Integer> musclesList, ArrayList<Integer> equipmentsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exercisesCategory = exercisesCategory;
        this.musclesList = musclesList;
        this.equipmentsList = equipmentsList;
        imagesUrlList = new ArrayList<>();
    }

    private Exercises(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        description = parcel.readString();
        exercisesCategory = parcel.readInt();
        if (parcel.readByte() == 0x01) {
            musclesList = new ArrayList<Integer>();
            parcel.readList(musclesList, Integer.class.getClassLoader());
        } else {
            musclesList = null;
        }
        if (parcel.readByte() == 0x01) {
            equipmentsList = new ArrayList<Integer>();
            parcel.readList(equipmentsList, Integer.class.getClassLoader());
        } else {
            equipmentsList = null;
        }
        if (parcel.readByte() == 0x01) {
            imagesUrlList = new ArrayList<String>();
            parcel.readList(imagesUrlList, String.class.getClassLoader());
        } else {
            imagesUrlList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(exercisesCategory);
        if (musclesList == null) {
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(musclesList);
        }
        if (equipmentsList == null) {
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(equipmentsList);
        }
        if (imagesUrlList == null) {
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(imagesUrlList);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExercisesCategory() {
        return exercisesCategory;
    }

    public void setExercisesCategory(int exercisesCategory) {
        this.exercisesCategory = exercisesCategory;
    }

    public ArrayList<Integer> getMusclesList() {
        return musclesList;
    }

    public void setMusclesList(ArrayList<Integer> musclesList) {
        this.musclesList = musclesList;
    }

    public ArrayList<Integer> getEquipmentsList() {
        return equipmentsList;
    }

    public void setEquipmentsList(ArrayList<Integer> equipmentsList) {
        this.equipmentsList = equipmentsList;
    }

    public ArrayList<String> getImagesUrlList() {
        return imagesUrlList;
    }

    public void setImagesUrlList(ArrayList<String> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
    }
}