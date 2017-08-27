package com.jbosak.favoriteplaces.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.jbosak.favoriteplaces.views.NavDrawer;
import com.jbosak.favoriteplaces.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {
    protected NavDrawer navDrawer;
    protected Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    protected void simplySetNavDrawer(NavDrawer drawer) {
        this.navDrawer = drawer;
        this.navDrawer.addOnce();

    }

    public NavDrawer.ActivityNavDrawerItem mapItem() {
        NavDrawer.ActivityNavDrawerItem mapItem = new NavDrawer.ActivityNavDrawerItem(
                "Map", new LatLng(30, 30), null, MapsActivity.class);
        return mapItem;
    }


    protected void setNavDrawer(NavDrawer drawer) {
        this.navDrawer = drawer;
        this.navDrawer.create();

        overridePendingTransition(0, 0);
        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        toolbar = (Toolbar) findViewById(R.id.include_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void fadeOut(final FadeOutListener listener) {
        View rootView = findViewById(android.R.id.content);
        rootView.animate()
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onFadeOutEnd();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .setDuration(300)
                .start();
    }

    public interface FadeOutListener {
        void onFadeOutEnd();

    }

}
