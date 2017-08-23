package com.jbosak.favoriteplaces.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.jbosak.favoriteplaces.MapFragment;
import com.jbosak.favoriteplaces.views.NavDrawer;
import com.jbosak.favoriteplaces.R;

public class MapsActivity extends BaseActivity implements MapFragment.OnCreateFavoriteListener {

    private static final int PERMISSION_ALL = 101;
    private Context context;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};// List of permissions required
    private MapFragment mapFragment;
    private FragmentManager fragmentManager;
    MapFragment.MainNavDrawerMap drawerMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maps);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        super.onCreate(savedInstanceState);
        context = getApplicationContext();


        getSupportActionBar().setTitle("<-Favorites");


        mapFragment = new MapFragment();
        drawerMap = new MapFragment.MainNavDrawerMap(this);

        fragmentManager = getSupportFragmentManager();

        setNavDrawer(drawerMap);
        askPermission();

    }



    public void askPermission() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    return;
                }
            }
        }
        fragmentManager.beginTransaction().replace(R.id.frame_layout,mapFragment).commit();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragmentManager.beginTransaction().replace(R.id.frame_layout,mapFragment).commit();

                } else {
                    Toast.makeText(this, "Until you grant the permission, we cannot proceed further", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onCreateFavorite(NavDrawer.ActivityNavDrawerItem item) {
        drawerMap.deleteItems();

        drawerMap.addItem(item);

        simplySetNavDrawer(drawerMap);
    }
}
