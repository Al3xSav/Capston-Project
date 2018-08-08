package com.alexsav.stayfit.asynctasks;

import android.os.AsyncTask;

import com.alexsav.stayfit.ui.activities.SearchGymActivity;
import com.alexsav.stayfit.utils.MapsUtils;

import java.net.URL;

public class GymAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL nearbyRequestUrl = MapsUtils.buildNearbySearchUrl(strings[0]);
            return MapsUtils.getResponseFromHttpUrl(nearbyRequestUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SearchGymActivity.extractGymLocation(s);
    }
}
