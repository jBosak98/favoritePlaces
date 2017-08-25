package com.jbosak.favoriteplaces.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.jbosak.favoriteplaces.MapFragment;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;
import com.jbosak.favoriteplaces.views.NavDrawer;
import com.jbosak.favoriteplaces.R;

import java.util.ArrayList;

import static android.R.id.list;

public class MapsActivity extends BaseActivity implements MapFragment.OnCreateFavoriteListener {

    private Context context;

    private MapFragment mapFragment;
    private FragmentManager fragmentManager;
    MainNavDrawerMap drawerMap;


    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("<-Favorites");

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maps);


        drawerMap = new MainNavDrawerMap(this);
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        mapFragment = new MapFragment();

        super.createMapItem();
        if(drawerMap.size() == 0){
            drawerMap.addItem(mapItem);
        }
        setNavDrawer(drawerMap);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frame_layout,mapFragment).commit();

    }






    @Override
    public void onCreateFavorite(NavDrawer.ActivityNavDrawerItem item) {


        drawerMap.addItem(item);

        simplySetNavDrawer(drawerMap);
    }


}
