package com.jbosak.favoriteplaces;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ALL = 101;
    private Context context;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};// List of permissions required
    private MapFragment mapFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maps);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        super.onCreate(savedInstanceState);
        setNavDrawer(new MainNavDrawer(this));
        context = getApplicationContext();


        getSupportActionBar().setTitle("<-Favorites");


         mapFragment = new MapFragment();
         fragmentManager = getSupportFragmentManager();

        askPermission();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

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
}
