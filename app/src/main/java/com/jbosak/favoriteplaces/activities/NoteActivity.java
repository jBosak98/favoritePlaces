package com.jbosak.favoriteplaces.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;
import com.jbosak.favoriteplaces.views.NavDrawer;


public class NoteActivity extends BaseActivity {
    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;
    private static final String BUNDLE_STATE = "BUNDLE_STATE";
    public static final String FAVORITE_PLACE = "FAVORITE_PLACE";


    private MainNavDrawerMap drawerMap;
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
        outState.putInt(BUNDLE_STATE,currentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_note);
        drawerMap = new MainNavDrawerMap(this);
        Log.e(this.toString(),drawerMap.activity.toString());
        setNavDrawer(drawerMap);
        super.onCreate(savedInstanceState);
        navDrawer.refreshActivity(this);
        favItem = getIntent().getParcelableExtra(FAVORITE_PLACE);

        noteText = (EditText) findViewById(R.id.activity_note_note);
        nameText = (EditText) findViewById(R.id.activity_note_name);
        
        
        if(savedInstanceState == null){
            getSupportActionBar().setTitle(favItem.getName());
            noteText.setText(favItem.getNote());
            changeState(STATE_VIEWING);
        } else
            changeState(savedInstanceState.getInt(BUNDLE_STATE));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.activity_note_menuEdit){
            changeState(STATE_EDITING);
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if(state == currentState) {
            return;
        }
        currentState = state;
        if (state == STATE_VIEWING){
            noteText.setEnabled(false);
            nameText.setEnabled(false);
            if(editProfileActionMode != null){
                editProfileActionMode.finish();
                editProfileActionMode = null;
            }
        } else if(state == STATE_EDITING){
            noteText.setEnabled(true);
            nameText.setEnabled(true);
            editProfileActionMode = toolbar.startActionMode( new EditProfileActionCallback());
        } else
            throw new IllegalArgumentException("Invalid state: " + state);
    }

    private class EditProfileActionCallback implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.activity_note_edit,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if(itemId == R.id.activity_note_edit_menuDone) {
                changeState(STATE_VIEWING);

                favItem.setName(nameText.getText().toString());
                toolbar.setTitle(nameText.getText().toString());
                favItem.setNote(noteText.getText().toString());

                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(currentState != STATE_VIEWING)
                changeState(STATE_VIEWING);
        }
    }


}
