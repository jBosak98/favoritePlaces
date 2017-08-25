package com.jbosak.favoriteplaces.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;


public class NoteActivity extends BaseActivity/* implements MapFragment.OnCreateFavoriteListener*/ {
    MainNavDrawerMap drawerMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_note);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setTitle("<-FavorXXXXites");



         drawerMap = new MainNavDrawerMap(this);

        setNavDrawer(new MainNavDrawerMap(this));

        super.onCreate(savedInstanceState);
    }


}
