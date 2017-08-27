package com.jbosak.favoriteplaces.views;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jbosak.favoriteplaces.activities.BaseActivity;
import com.jbosak.favoriteplaces.activities.NoteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainNavDrawerMap extends NavDrawer {

    public MainNavDrawerMap(BaseActivity activity) {
        super(activity);
        reload();
    }

    public void reload() {

        if (items.size() != 0)
            writeJSONArrayToFile(createJSONArray());

        JSONArray array = readData();
        if (array != null) {
            if (array.length() != 0) {
                JSONObject fav;
                super.deleteItems();
                addItem(activity.mapItem());
                for (int i = 1; i < array.length(); i++) {

                    try {
                        fav = array.getJSONObject(i);
                        String note = null;
                        if (!fav.isNull(NoteActivity.PLACE_NOTE)) {
                            note = fav.getString(NoteActivity.PLACE_NOTE);
                        }
                        LatLng latLng = new LatLng(
                                fav.getDouble(NoteActivity.PLACE_LATITUDE),
                                fav.getDouble(NoteActivity.PLACE_LONGITUDE));

                        addItem(new ActivityNavDrawerItem(
                                fav.getString(NoteActivity.PLACE_NAME), latLng, note, NoteActivity.class));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }
    }


    private JSONArray readData() {
        String ret = "";

        try {
            InputStream inputStream = activity.openFileInput("places.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }
        } catch (FileNotFoundException e) {
            Log.e("MainNavDrawerMap", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("MainNavDrawerMap", "Can not read file: " + e.toString());
        }


        try {
            return new JSONArray(ret);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createJSONArray() {
        JSONArray ja = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            try {
                JSONObject jo = new JSONObject();
                jo.put(NoteActivity.PLACE_NAME, items.get(i).getName());
                if (items.get(i).getNote() != null)
                    jo.put(NoteActivity.PLACE_NOTE, items.get(i).getNote());
                jo.put(NoteActivity.PLACE_LATITUDE, items.get(i).getLatitude());
                jo.put(NoteActivity.PLACE_LONGITUDE, items.get(i).getLongitude());
                ja.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }

    private void writeJSONArrayToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(activity.getBaseContext().openFileOutput("places.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}