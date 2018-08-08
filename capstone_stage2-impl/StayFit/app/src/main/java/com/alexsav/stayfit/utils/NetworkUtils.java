package com.alexsav.stayfit.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.alexsav.stayfit.BuildConfig.BASE_URL;

public class NetworkUtils {

    private static final String WM_BASE_URL = BASE_URL;
    private static final String EQUIPMENT = "equipment";
    private static final String EXERCISE_CATEGORY = "exercisecategory";
    private static final String EXERCISE = "exercise";
    private static final String EXERCISE_IMAGE = "exerciseimage";
    private static final String LANGUAGE = "language";

    // Build Categories Url
    public static URL categoriesUrl() {
        Uri uri = Uri
                .parse(WM_BASE_URL)
                .buildUpon()
                .appendPath(EXERCISE_CATEGORY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Build Equipments Url
    public static URL equipmentsUrl() {
        Uri uri = Uri
                .parse(WM_BASE_URL)
                .buildUpon()
                .appendPath(EQUIPMENT)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Build Exercises Url
    public static URL exercisesUrl(ArrayList<Integer> categoriesList, ArrayList<Integer> equipmentsList) {
        Uri uri = Uri
                .parse(WM_BASE_URL)
                .buildUpon()
                .appendPath(EXERCISE)
                .appendQueryParameter(LANGUAGE, "2")
                .build();
        for (int i = 0; i < categoriesList.size(); i++) {
            uri = uri
                    .buildUpon()
                    .appendQueryParameter(EXERCISE_CATEGORY, Integer.toString(i))
                    .build();
        }
        for (int i = 0; i < equipmentsList.size(); i++) {
            uri = uri
                    .buildUpon()
                    .appendQueryParameter(EQUIPMENT, Integer.toString(i))
                    .build();
        }
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Build Images Url
    public static URL imagesUrl(int exercisesId) {
        Uri uri = Uri
                .parse(WM_BASE_URL)
                .buildUpon()
                .appendPath(EXERCISE_IMAGE)
                .appendQueryParameter(LANGUAGE, "2")
                .build();
        uri = uri
                .buildUpon()
                .appendQueryParameter(EXERCISE, Integer.toString(exercisesId))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // By Udacity
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}