package com.jbosak.favoriteplaces.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.jbosak.favoriteplaces.MapFragment;
import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.views.MainNavDrawerMap;
import com.jbosak.favoriteplaces.views.NavDrawer;

public class MapsActivity extends BaseActivity implements MapFragment.OnCreateFavoriteListener {

    private Context context;

    private MapFragment mapFragment;
    private FragmentManager fragmentManager;
    private MainNavDrawerMap drawerMap;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};// List of permissions required
    private static final int PERMISSION_ALL = 101;


    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favorite Places");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maps);
        askPermission();

        drawerMap = new MainNavDrawerMap(this);
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        if (drawerMap.size() == 0) {
            drawerMap.addItem(mapItem());
        }
        setNavDrawer(drawerMap);
        mapFragment = new MapFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, mapFragment).commit();

    }


    @Override
    public void onCreateFavorite(NavDrawer.ActivityNavDrawerItem item) {
        drawerMap.addItem(item);
        simplySetNavDrawer(drawerMap);
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


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Until you grant the permission, we cannot proceed further", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
