package com.jbosak.favoriteplaces.views;


import android.util.Log;

import com.jbosak.favoriteplaces.MapFragment;
import com.jbosak.favoriteplaces.activities.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class MainNavDrawerMap extends NavDrawer implements MapFragment.OnCreateFavoriteListener{


        public MainNavDrawerMap(BaseActivity activity) {
            super(activity);


    }

    @Override
    public void onCreateFavorite(ActivityNavDrawerItem item) {
        this.deleteItems();

        this.addItem(item);
    }


}