package com.alexsav.stayfit.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.databinding.ActivityMainBinding;
import com.alexsav.stayfit.ui.fragments.CreateWorkoutFragment;
import com.alexsav.stayfit.ui.fragments.SavedWorkoutPlanFragment;

import static com.alexsav.stayfit.utils.ConnectionUtils.isNetworkAvailable;
import static com.alexsav.stayfit.utils.ConnectionUtils.makeSnackBar;

public class MainActivity extends AppCompatActivity {

    public static final String CREATE_WORKOUT_FRAGMENT_TAG = "fragment_create_workout";
    public static final String SAVED_WORKOUT_PLAN_FRAGMENT_TAG = "fragment_saved_workout_plan";

    public ActivityMainBinding mActivityMainBinding;
    public CreateWorkoutFragment mCreateWorkoutFragment;
    public SavedWorkoutPlanFragment mSavedWorkoutPlanFragment;
    public MenuItem menuRefresh;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search_gym:
                    Intent intent = new Intent(MainActivity.this, SearchGymActivity.class);
                    startActivity(intent);
                    return false;
                case R.id.navigation_create_workout:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container,
                                    mCreateWorkoutFragment, CREATE_WORKOUT_FRAGMENT_TAG)
                            .commit();
                    return true;
                case R.id.navigation_saved_workouts:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container,
                                    mSavedWorkoutPlanFragment, SAVED_WORKOUT_PLAN_FRAGMENT_TAG)
                            .commit();
                    return true;
            }
            return false;
        }
    };
    private long lastPress;
    private Snackbar onBackPress;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (isNetworkAvailable(this)) {
            onItemLoaded();
            if (savedInstanceState == null) {
                mCreateWorkoutFragment = new CreateWorkoutFragment();
                mSavedWorkoutPlanFragment = new SavedWorkoutPlanFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mCreateWorkoutFragment, CREATE_WORKOUT_FRAGMENT_TAG)
                        .commit();
                mActivityMainBinding.navigation.setSelectedItemId(R.id.navigation_create_workout);
            } else {
                mCreateWorkoutFragment = (CreateWorkoutFragment) getSupportFragmentManager()
                        .findFragmentByTag(CREATE_WORKOUT_FRAGMENT_TAG);
                mSavedWorkoutPlanFragment = (SavedWorkoutPlanFragment) getSupportFragmentManager()
                        .findFragmentByTag(SAVED_WORKOUT_PLAN_FRAGMENT_TAG);
                if (mCreateWorkoutFragment == null) {
                    mCreateWorkoutFragment = new CreateWorkoutFragment();
                }
                if (mSavedWorkoutPlanFragment == null) {
                    mSavedWorkoutPlanFragment = new SavedWorkoutPlanFragment();
                }
            }

        } else if (!isNetworkAvailable(this)) {
            makeSnackBar(mActivityMainBinding.getRoot(), getResources().getString(R.string.swipe_message));
            mActivityMainBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshItems(savedInstanceState);
                }
            });

        }
        mActivityMainBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    void refreshItems(Bundle savedInstanceState) {

        if (!isNetworkAvailable(MainActivity.this)) {
            makeSnackBar(mActivityMainBinding.getRoot(), getResources().getString(R.string.swipe_message));
            mActivityMainBinding.swipeRefreshLayout.setRefreshing(false);
        } else {
            if (savedInstanceState == null) {
                mCreateWorkoutFragment = new CreateWorkoutFragment();
                mSavedWorkoutPlanFragment = new SavedWorkoutPlanFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mCreateWorkoutFragment, CREATE_WORKOUT_FRAGMENT_TAG)
                        .commit();
                mActivityMainBinding.navigation.setSelectedItemId(R.id.navigation_create_workout);
            } else {
                mCreateWorkoutFragment = (CreateWorkoutFragment) getSupportFragmentManager()
                        .findFragmentByTag(CREATE_WORKOUT_FRAGMENT_TAG);
                mSavedWorkoutPlanFragment = (SavedWorkoutPlanFragment) getSupportFragmentManager()
                        .findFragmentByTag(SAVED_WORKOUT_PLAN_FRAGMENT_TAG);
                if (mCreateWorkoutFragment == null) {
                    mCreateWorkoutFragment = new CreateWorkoutFragment();
                }
                if (mSavedWorkoutPlanFragment == null) {
                    mSavedWorkoutPlanFragment = new SavedWorkoutPlanFragment();
                }
            }
            onItemLoaded();
        }
    }

    void onItemLoaded() {
        if (isNetworkAvailable(this)) {
            mActivityMainBinding.swipeRefreshLayout.setEnabled(false);
            mActivityMainBinding.swipeRefreshLayout.setRefreshing(false);
        } else {
            mActivityMainBinding.swipeRefreshLayout.setEnabled(true);
            mActivityMainBinding.swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 2000) {
            onBackPress = Snackbar.make(
                    mActivityMainBinding.getRoot(),
                    getResources().getString(R.string.click_back),
                    Snackbar.LENGTH_SHORT).setAction(getResources().getString(R.string.action),
                    null);
            onBackPress.show();
            lastPress = currentTime;
        } else {
            if (onBackPress != null) {
                onBackPress.dismiss();
                onBackPress = null;
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        menuRefresh = menu.findItem(R.id.settings_refresh);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_refresh:
                mCreateWorkoutFragment = new CreateWorkoutFragment();
                mSavedWorkoutPlanFragment = new SavedWorkoutPlanFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mCreateWorkoutFragment, CREATE_WORKOUT_FRAGMENT_TAG)
                        .commit();
                mActivityMainBinding.navigation.setSelectedItemId(R.id.navigation_create_workout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
