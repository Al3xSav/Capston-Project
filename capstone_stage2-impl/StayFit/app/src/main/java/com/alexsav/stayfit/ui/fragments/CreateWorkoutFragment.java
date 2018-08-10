package com.alexsav.stayfit.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.adapters.EquipmentsAdapter;
import com.alexsav.stayfit.adapters.ExercisesAdapter;
import com.alexsav.stayfit.adapters.ExercisesCategoryAdapter;
import com.alexsav.stayfit.databinding.FragmentCreateWorkoutBinding;
import com.alexsav.stayfit.ui.activities.WorkoutDetailsActivity;
import com.alexsav.stayfit.utils.NetworkUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import static com.alexsav.stayfit.utils.ConnectionUtils.isNetworkAvailable;
import static com.alexsav.stayfit.utils.ConnectionUtils.makeSnackBar;

public class CreateWorkoutFragment extends Fragment {

    public Context mContext;
    public int mPosition;
    public FragmentCreateWorkoutBinding mFragmentCreateWorkoutBinding;

    public RecyclerView.LayoutManager mCategoriesLayout;
    public RecyclerView.LayoutManager mEquipmentsLayout;
    public RecyclerView.LayoutManager mExercisesLayout;

    public RecyclerView.Adapter mCategoriesAdapter;
    public RecyclerView.Adapter mEquipmentsAdapter;
    public RecyclerView.Adapter mExercisesAdapter;

    public String mCategoriesJson;
    //public ArrayList<Integer> mCategoriesSelectedItems;
    public String mEquipmentsJson;
    //public ArrayList<Integer> mEquipmentsSelectedItems;
    public String mExercisesJson;
    //public ArrayList<Integer> mExercisesSelectedItems;

    /*private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;`*/

    private AlertDialog.Builder mSaveInputBuilder;
    private EditText mSaveInputField;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentCreateWorkoutBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_create_workout, container, false);

        mContext = container.getContext();

        mCategoriesLayout = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mEquipmentsLayout = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mExercisesLayout = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);

        mFragmentCreateWorkoutBinding.recyclerViewMuscles.setLayoutManager(mCategoriesLayout);
        mFragmentCreateWorkoutBinding.recyclerViewMuscles.setHasFixedSize(true);

        mFragmentCreateWorkoutBinding.recyclerViewEquipments.setLayoutManager(mEquipmentsLayout);
        mFragmentCreateWorkoutBinding.recyclerViewEquipments.setHasFixedSize(true);

        mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setLayoutManager(mExercisesLayout);
        mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setHasFixedSize(true);

        mFragmentCreateWorkoutBinding.fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition == 2) {
                    createSaveDialog("");
                } else {
                    mPosition++;
                    onPositionView();
                }
            }
        });

        mFragmentCreateWorkoutBinding.fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition != 0) {
                    mPosition--;
                    onPositionView();
                }
            }
        });

        if (getActivity() != null) {
            getActivity().setTitle(getString(R.string.body_parts_title));
        }

        return mFragmentCreateWorkoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(getString(R.string.create_position_extra));
            mCategoriesJson = savedInstanceState.getString(getString(R.string.categories_json_extra));
            //mCategoriesSelectedItems = savedInstanceState.getIntegerArrayList(getString(R.string.create_selected_categories_id_extra));
            mEquipmentsJson = savedInstanceState.getString(getString(R.string.equipments_json_extra));
            //mEquipmentsSelectedItems = savedInstanceState.getIntegerArrayList(getString(R.string.create_selected_equipments_id_extra));
            mExercisesJson = savedInstanceState.getString(getString(R.string.exercises_json_extra));
            //mExercisesSelectedItems = savedInstanceState.getIntegerArrayList(getString(R.string.create_selected_exercises_id_extra));

            String saveString = savedInstanceState.getString(getString(R.string.input_string_extra));

            if (!TextUtils.isEmpty(mCategoriesJson)) {
                setCategoriesAdapter(mCategoriesJson);
            }
            if (!TextUtils.isEmpty(mEquipmentsJson)) {
                setEquipmentsAdapter(mEquipmentsJson);
            }
            if (!TextUtils.isEmpty(mExercisesJson)) {
                setExercisesAdapter(mExercisesJson);
            }

            if (saveString != null) {
                createSaveDialog(saveString);
            }
            onPositionView();
        } else {
            resetFragment();
        }
    }

    /*@Override
    public void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = mFragmentCreateWorkoutBinding.recyclerViewMuscles.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.create_position_extra), mPosition);
        outState.putString(getString(R.string.categories_json_extra), mCategoriesJson);
        outState.putString(getString(R.string.equipments_json_extra), mEquipmentsJson);
        outState.putString(getString(R.string.exercises_json_extra), mExercisesJson);


        if (mSaveInputField != null) {
            outState.putString(getString(R.string.input_string_extra), mSaveInputField.getText().toString());
        }
    }

    public void onPositionView() {
        switch (mPosition) {
            case 0:
                mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewMuscles.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.recyclerViewEquipments.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.fabPrevious.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.fabNext.setImageResource(R.drawable.ic_next);
                if (getActivity() != null) {
                    getActivity().setTitle(getString(R.string.body_parts_title));
                }
                break;
            case 1:
                mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewMuscles.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewEquipments.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.fabPrevious.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.fabPrevious.setImageResource(R.drawable.ic_previous);
                mFragmentCreateWorkoutBinding.fabNext.setImageResource(R.drawable.ic_next);
                if (getActivity() != null) {
                    getActivity().setTitle(getString(R.string.equipments_title));
                }
                break;
            case 2:
                mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.recyclerViewMuscles.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewEquipments.setVisibility(View.GONE);
                mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.fabPrevious.setVisibility(View.VISIBLE);
                mFragmentCreateWorkoutBinding.fabPrevious.setImageResource(R.drawable.ic_previous);
                mFragmentCreateWorkoutBinding.fabNext.setImageResource(R.drawable.ic_heart_circle);

                if (mCategoriesAdapter != null && mEquipmentsAdapter != null && TextUtils.isEmpty(mExercisesJson)) {
                    new FetchExercisesTask(this).execute();
                }
                if (getActivity() != null) {
                    getActivity().setTitle(getString(R.string.exercises_title));
                }
                break;
            default:
                resetFragment();
                break;
        }
    }

    public void resetFragment() {
        mPosition = 0;
        mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.VISIBLE);
        mSaveInputBuilder = null;
        mSaveInputField = null;
        new FetchCategoriesTask(this).execute();
        new FetchEquipmentsTask(this).execute();
        onPositionView();
    }

    public void createSaveDialog(String previousInput) {
        if (getActivity() != null) {
            mSaveInputBuilder = new AlertDialog.Builder(getActivity());
        }

        mSaveInputBuilder.setMessage(getString(R.string.save_input_field));
        mSaveInputField = new EditText(getActivity());
        mSaveInputField.setInputType(InputType.TYPE_CLASS_TEXT);
        mSaveInputBuilder.setView(mSaveInputField);
        if (!TextUtils.isEmpty(previousInput)) {
            mSaveInputField.setText(previousInput);
        }
        mSaveInputBuilder.setPositiveButton(getString(R.string.save_button_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent workoutDetailsIntent = new Intent(getActivity(), WorkoutDetailsActivity.class);
                        workoutDetailsIntent.putExtra(getString(R.string.workouts_name_extra),
                                mSaveInputField.getText().toString());
                        workoutDetailsIntent.putParcelableArrayListExtra(getString(R.string.exercises_list_extra),
                                ((ExercisesAdapter) mExercisesAdapter).getSelectedExercisesList());
                        getActivity().startActivity(workoutDetailsIntent);
                    }
                });
        mSaveInputBuilder.setNegativeButton(getString(R.string.cancel_button_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        mSaveInputBuilder.show();
    }

    public void setCategoriesAdapter(String json) {
        mCategoriesAdapter = new ExercisesCategoryAdapter(mContext, json);
        mFragmentCreateWorkoutBinding.recyclerViewMuscles.setAdapter(mCategoriesAdapter);
    }

    public void setEquipmentsAdapter(String json) {
        mEquipmentsAdapter = new EquipmentsAdapter(mContext, json);
        mFragmentCreateWorkoutBinding.recyclerViewEquipments.setAdapter(mEquipmentsAdapter);
    }

    public void setExercisesAdapter(String json) {
        mExercisesAdapter = new ExercisesAdapter(mContext, json);
        mFragmentCreateWorkoutBinding.recyclerViewExercisesSelection.setAdapter(mExercisesAdapter);
    }

    //AsyncTask for Categories
    private static class FetchCategoriesTask extends AsyncTask<Void, Void, String> {
        private WeakReference<CreateWorkoutFragment> fragmentWeakReference;

        private FetchCategoriesTask(CreateWorkoutFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isNetworkAvailable(fragmentWeakReference.get().mContext)) {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.VISIBLE);
                makeSnackBar(fragmentWeakReference.get().mFragmentCreateWorkoutBinding.getRoot(), "Connection Lost.\nPlease Try Again!");
            } else {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
            }
        }
            @Override
            protected String doInBackground (Void...voids){
                try {
                    URL categoriesRequestUrl = NetworkUtils.categoriesUrl();
                    return NetworkUtils.getResponseFromHttpUrl(categoriesRequestUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);

                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
                fragmentWeakReference.get().mCategoriesJson = s;

                if (fragmentWeakReference.get() != null && s != null)
                    fragmentWeakReference.get().setCategoriesAdapter(fragmentWeakReference.get().mCategoriesJson);
            }
    }

    //AsyncTask for Equipments
    private static class FetchEquipmentsTask extends AsyncTask<Void, Void, String> {
        private WeakReference<CreateWorkoutFragment> fragmentWeakReference;

        private FetchEquipmentsTask(CreateWorkoutFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isNetworkAvailable(fragmentWeakReference.get().mContext)) {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.VISIBLE);
                makeSnackBar(fragmentWeakReference.get().mFragmentCreateWorkoutBinding.getRoot(), "Connection Lost.\nPlease Try Again!");
            } else {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
            }
        }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL equipmentRequestUrl = NetworkUtils.equipmentsUrl();
                    return NetworkUtils.getResponseFromHttpUrl(equipmentRequestUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
                fragmentWeakReference.get().mEquipmentsJson = s;

                if (fragmentWeakReference.get() != null && s != null)
                    fragmentWeakReference.get().setEquipmentsAdapter(fragmentWeakReference.get().mEquipmentsJson);
        }
    }

    // AsyncTask for Exercises
    private static class FetchExercisesTask extends AsyncTask<Void, Void, String> {
        private WeakReference<CreateWorkoutFragment> fragmentWeakReference;

        private FetchExercisesTask(CreateWorkoutFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isNetworkAvailable(fragmentWeakReference.get().mContext)) {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.VISIBLE);
                makeSnackBar(fragmentWeakReference.get().mFragmentCreateWorkoutBinding.getRoot(),
                        "Connection Lost.\nPlease Try Again!");
            } else {
                fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL exerciseRequestUrl = NetworkUtils.exercisesUrl
                        (((ExercisesCategoryAdapter) fragmentWeakReference.get().mCategoriesAdapter).getSelectedIdList(),
                                ((EquipmentsAdapter) fragmentWeakReference.get().mEquipmentsAdapter).getSelectedIdList());
                return NetworkUtils.getResponseFromHttpUrl(exerciseRequestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            fragmentWeakReference.get().mFragmentCreateWorkoutBinding.progressBar.setVisibility(View.GONE);
            fragmentWeakReference.get().mExercisesJson = s;

            if (fragmentWeakReference.get() != null)
                fragmentWeakReference.get().setExercisesAdapter(fragmentWeakReference.get().mExercisesJson);

        }
    }
}