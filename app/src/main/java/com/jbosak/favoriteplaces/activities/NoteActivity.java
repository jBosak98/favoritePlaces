package com.jbosak.favoriteplaces.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;
import com.jbosak.favoriteplaces.views.NavDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class NoteActivity extends BaseActivity {
    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;
    private static final String BUNDLE_STATE = "BUNDLE_STATE";

    public static final String PLACE_LATITUDE = "PLACE_LATITUDE";
    public static final String PLACE_LONGITUDE = "PLACE_LONGITUDE";
    public static final String PLACE_NOTE = "PLACE_NOTE";
    public static final String PLACE_NAME = "PLACE_NAME";


    private EditText noteText;
    private EditText nameText;
    private NavDrawer.BasicNavDrawerItem favItem;
    private int currentState;
    private ActionMode editProfileActionMode;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_note, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STATE, currentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_note);
        setNavDrawer(new MainNavDrawerMap(this));
        super.onCreate(savedInstanceState);

        String note;

        if (getIntent().getStringExtra(PLACE_NOTE) != null) {
            note = getIntent().getStringExtra(PLACE_NOTE);
        } else note = null;

        favItem = new NavDrawer.BasicNavDrawerItem(
                getIntent().getStringExtra(PLACE_NAME),
                getIntent().getDoubleExtra(PLACE_LATITUDE, 0),
                getIntent().getDoubleExtra(PLACE_LONGITUDE, 0),
                note, NoteActivity.class);

        noteText = (EditText) findViewById(R.id.activity_note_note);
        nameText = (EditText) findViewById(R.id.activity_note_name);


        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(favItem.getName());
            nameText.setText(favItem.getName());
            if (note != null) {
                noteText.setText(note);
            }
            changeState(STATE_VIEWING);
        } else
            changeState(savedInstanceState.getInt(BUNDLE_STATE));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.activity_note_menuEdit) {
            changeState(STATE_EDITING);
            return true;
        }if (itemId == R.id.activity_note_map_view) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra(NoteActivity.PLACE_LATITUDE, favItem.getLatitude());
            intent.putExtra(NoteActivity.PLACE_LONGITUDE, favItem.getLongitude());
            intent.putExtra(NoteActivity.PLACE_NAME, favItem.getName());
            startActivity(intent);
            finish();
            return true;
        } if(itemId == R.id.activity_note_delete){
            favItem.setLongitude(Double.NaN);
            writeJSONArrayToFile(createJSONArray());
            startActivity(new Intent(this,MapsActivity.class));
        }
        return false;
    }

    private void changeState(int state) {
        if (state == currentState) {
            return;
        }
        currentState = state;
        if (state == STATE_VIEWING) {
            writeJSONArrayToFile(createJSONArray());

            noteText.setEnabled(false);
            nameText.setEnabled(false);
            if (editProfileActionMode != null) {
                editProfileActionMode.finish();
                editProfileActionMode = null;
            }
        } else if (state == STATE_EDITING) {
            noteText.setEnabled(true);
            nameText.setEnabled(true);
            editProfileActionMode = toolbar.startActionMode(new EditProfileActionCallback());
        } else
            throw new IllegalArgumentException("Invalid state: " + state);
    }


    private String createJSONArray() {
        navDrawer.getItems();
        JSONArray ja = new JSONArray();
        for (int i = 0; i < navDrawer.getItems().size(); i++) {
            try {
                JSONObject jo = new JSONObject();
                if (navDrawer.getItems().get(i).getLatitude() == favItem.getLatitude()
                        && navDrawer.getItems().get(i).getLongitude() == favItem.getLongitude()) {

                    navDrawer.getItems().set(i, favItem);
                }
                jo.put(PLACE_NAME, navDrawer.getItems().get(i).getName());
                if (navDrawer.getItems().get(i).getNote() != null) {
                    jo.put(PLACE_NOTE, navDrawer.getItems().get(i).getNote());
                }
                jo.put(PLACE_LATITUDE, navDrawer.getItems().get(i).getLatitude());
                jo.put(PLACE_LONGITUDE, navDrawer.getItems().get(i).getLongitude());

                if(!(navDrawer.getItems().get(i).getLatitude() == favItem.getLatitude() && favItem.getLongitude() == Double.NaN)) {
                    ja.put(jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ja.toString();
    }


    private class EditProfileActionCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.activity_note_edit, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.activity_note_edit_menuDone) {

                favItem.setName(nameText.getText().toString());
                toolbar.setTitle(nameText.getText().toString());
                favItem.setNote(noteText.getText().toString());
                changeState(STATE_VIEWING);

                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (currentState != STATE_VIEWING)
                changeState(STATE_VIEWING);
        }
    }

    private void writeJSONArrayToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("places.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
