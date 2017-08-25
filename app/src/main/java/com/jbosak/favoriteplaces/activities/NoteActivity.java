package com.jbosak.favoriteplaces.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;


public class NoteActivity extends BaseActivity/* implements MapFragment.OnCreateFavoriteListener*/ {
    MainNavDrawerMap drawerMap;

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("<-FavorXXXXites");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_note);
        drawerMap = new MainNavDrawerMap(this);
        Log.e(this.toString(),drawerMap.activity.toString());
        setNavDrawer(drawerMap);

        super.onCreate(savedInstanceState);


        navDrawer.refreshActivity(this);

    }


}
