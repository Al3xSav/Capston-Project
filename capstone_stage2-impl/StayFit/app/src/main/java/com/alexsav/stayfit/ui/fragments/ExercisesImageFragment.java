package com.alexsav.stayfit.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.databinding.FragmentExercisesImageBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExercisesImageFragment extends Fragment {

    public ArrayList<String> mExercisesImageList;
    public FragmentExercisesImageBinding mFragmentExercisesImageBinding;

    public ExercisesImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentExercisesImageBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_exercises_image, container, false);

        mExercisesImageList = getArguments().getStringArrayList(getString(R.string.images_list_extra));
        mFragmentExercisesImageBinding.exercisesImageHolder.removeAllViews();
        for (String s : mExercisesImageList) {
            ImageView exercisesImage = new ImageView(getContext());
            Picasso.get().load(s).into(exercisesImage);
            exercisesImage.setContentDescription(s);
            mFragmentExercisesImageBinding.exercisesImageHolder.addView(exercisesImage);
        }
        return mFragmentExercisesImageBinding.getRoot();
    }
}
