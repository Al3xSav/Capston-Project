package com.alexsav.stayfit.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexsav.stayfit.BuildConfig;
import com.alexsav.stayfit.R;
import com.alexsav.stayfit.adapters.WorkoutDetailsAdapter;
import com.alexsav.stayfit.databinding.FragmentExercisesListBinding;
import com.alexsav.stayfit.model.Exercises;
import com.alexsav.stayfit.utils.NetworkUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class ExercisesListFragment extends Fragment {

    public static final String JSON_RESULTS = "results";
    public static final String JSON_EXERCISE = "exercise";
    public static final String JSON_IMAGE = "image";
    public FragmentExercisesListBinding mFragmentExercisesListBinding;
    public LinearLayoutManager mWorkoutsLayout;
    public WorkoutDetailsAdapter mWorkoutDetailsAdapter;
    public ArrayList<Exercises> mExercisesList;

    public ExercisesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentExercisesListBinding = DataBindingUtil.
                inflate(inflater, R.layout.fragment_exercises_list, container, false);

        mWorkoutsLayout = new LinearLayoutManager(getActivity());
        mExercisesList = getArguments().getParcelableArrayList(getString(R.string.exercises_list_extra));

        mFragmentExercisesListBinding.recyclerViewWorkoutDetails.setLayoutManager(mWorkoutsLayout);

        if (savedInstanceState == null) {
            mFragmentExercisesListBinding.progressBar.setVisibility(View.VISIBLE);
            for (Exercises e : mExercisesList) {
                new FetchImageUrlsTask().execute(e.getId());
            }
        } else {
            mExercisesList = savedInstanceState.getParcelableArrayList(getString(R.string.exercises_list_extra));
            setDetailsAdapter();
        }

        MobileAds.initialize(getContext(), BuildConfig.ADMOB_APP_ID_KEY);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mFragmentExercisesListBinding.adViewExercisesList.loadAd(adRequest);

        return mFragmentExercisesListBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.exercises_list_extra), mExercisesList);
    }

    public void setDetailsAdapter() {
        mWorkoutDetailsAdapter = new WorkoutDetailsAdapter(getActivity(), mExercisesList);
        mFragmentExercisesListBinding.recyclerViewWorkoutDetails.setAdapter(mWorkoutDetailsAdapter);
    }

    class FetchImageUrlsTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                URL imageRequestUrl = NetworkUtils.imagesUrl(integers[0]);
                return NetworkUtils.getResponseFromHttpUrl(imageRequestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mFragmentExercisesListBinding.progressBar.setVisibility(View.GONE);

            try {
                JSONArray imageUrlArray = new JSONObject(s).getJSONArray(JSON_RESULTS);
                if (imageUrlArray.length() != 0) {
                    int currentExercise =
                            imageUrlArray.getJSONObject(0).getInt(JSON_EXERCISE);
                    for (Exercises e : mExercisesList) {
                        if (e.getId() == currentExercise) {
                            for (int i = 0; i < imageUrlArray.length(); i++) {
                                e.getImagesUrlList().add(imageUrlArray.getJSONObject(i).getString(JSON_IMAGE));
                            }
                        }
                    }
                }
                setDetailsAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
