package com.alexsav.stayfit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Workouts implements Parcelable {
    public static final Creator<Workouts> CREATOR = new Parcelable.Creator<Workouts>() {
        @Override
        public Workouts createFromParcel(Parcel parcel) {
            return new Workouts(parcel);
        }

        @Override
        public Workouts[] newArray(int size) {
            return new Workouts[size];
        }
    };
    private String name;
    private ArrayList<Exercises> exercisesList;

    public Workouts(String name, ArrayList<Exercises> exercisesList) {
        this.name = name;
        this.exercisesList = exercisesList;
    }

    private Workouts(Parcel parcel) {
        name = parcel.readString();
        if (parcel.readByte() == 0x01) {
            exercisesList = new ArrayList<Exercises>();
            parcel.readList(exercisesList, Exercises.class.getClassLoader());
        } else {
            exercisesList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        if (exercisesList == null) {
            parcel.writeByte((byte) (0x00));
        } else {
            parcel.writeByte((byte) (0x01));
            parcel.writeList(exercisesList);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercises> getExercisesList() {
        return exercisesList;
    }

    public void setExercisesList(ArrayList<Exercises> exercisesList) {
        this.exercisesList = exercisesList;
    }
}