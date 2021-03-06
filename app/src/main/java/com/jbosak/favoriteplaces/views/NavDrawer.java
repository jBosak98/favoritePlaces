package com.jbosak.favoriteplaces.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.jbosak.favoriteplaces.R;
import com.jbosak.favoriteplaces.activities.BaseActivity;
import com.jbosak.favoriteplaces.activities.MapsActivity;
import com.jbosak.favoriteplaces.activities.NoteActivity;

import java.util.ArrayList;


public class NavDrawer {
    public static ArrayList<BasicNavDrawerItem> items = new ArrayList<>();

    public static ArrayList<BasicNavDrawerItem> getItems() {
        return items;
    }

    private NavDrawerItem selectedItem;

    public static BaseActivity activity;
    protected ViewGroup navDrawerView;
    protected DrawerLayout drawerLayout;

    public NavDrawer(BaseActivity activity) {

        this.activity = activity;
        navDrawerView = (ViewGroup) activity.findViewById(R.id.nav_drawer);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        Toolbar toolbar = activity.getToolbar();
        toolbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (drawerLayout != null) {
                }
                setOpen(!isOpen());
            }
        });
    }


    public void addItem(BasicNavDrawerItem item) {
        items.add(item);
        item.navDrawer = this;
    }

    public void deleteItems() {
        items.removeAll(items);

    }

    public int size() {
        return items.size();
    }

    public boolean isOpen() {
        return drawerLayout.isDrawerOpen(Gravity.START);
    }

    public void setOpen(boolean isOpen) {
        if (isOpen) {
            drawerLayout.openDrawer(Gravity.START);
        } else {
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    public void setSelectedItem(NavDrawerItem item) {
        if (selectedItem != null) {
            selectedItem.setSelected(false);
        }
        selectedItem = item;
        selectedItem.setSelected(true);

    }

    public void create() {
        LayoutInflater inflater = activity.getLayoutInflater();

        for (NavDrawerItem item : items) {
            item.inflate(inflater, navDrawerView);
        }


    }

    public void addOnce() {
        LayoutInflater inflater = activity.getLayoutInflater();

        items.get(items.size() - 1).inflate(inflater, navDrawerView);

    }


    public static abstract class NavDrawerItem {
        protected NavDrawer navDrawer;

        public abstract void inflate(LayoutInflater inflater, ViewGroup container);

        public abstract void setSelected(boolean isSelected);


    }

    public static class BasicNavDrawerItem extends NavDrawerItem implements View.OnClickListener {
        private String name;
        private View view;
        private TextView textView;
        private String note;
        private double latitude;
        private double longitude;
        private Bundle bundle;
        private Class targetActivity;

        public double getLatitude() {
            return latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLongitude() {
            return longitude;
        }


        public Class getTargetActivity() {
            return targetActivity;
        }


        public BasicNavDrawerItem(String name, Double latitude, Double longitude, @Nullable String note, Class targetActivity) {
            this.name = name;
            this.note = note;
            this.latitude = latitude;
            this.longitude = longitude;
            this.targetActivity = targetActivity;

        }


        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView) {
            ViewGroup container = (ViewGroup) navDrawerView.findViewById(R.id.include_main_nav_drawer_topItems);

            if (container == null) {
                throw new RuntimeException("Nav drawer item" + name +
                        "could not be attached to ViewGroup, View not found.");
            }

            view = inflater.inflate(R.layout.list_item_nav_drawer, container, false);
            container.addView(view);


            view.setOnClickListener(this);

            textView = (TextView) view.findViewById(R.id.list_item_nav_drawer_text);
            textView.setText(name);
        }

        @Override
        public void setSelected(boolean isSelected) {
        }

        public void setName(String text) {
            this.name = text;
            if (view != null) {
                textView.setText(text);
            }
        }

        @Override
        public void onClick(View view) {
            bundle = new Bundle();
            bundle.putString(NoteActivity.PLACE_NAME, getName());
            bundle.putDouble(NoteActivity.PLACE_LATITUDE, getLatitude());
            bundle.putDouble(NoteActivity.PLACE_LONGITUDE, getLongitude());
            if (getNote() != null)
                bundle.putString(NoteActivity.PLACE_NOTE, getNote());
            navDrawer.setSelectedItem(this);

        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getName() {
            return name;
        }

        public String getNote() {
            return note;
        }

    }

    public static class ActivityNavDrawerItem extends BasicNavDrawerItem {


        public ActivityNavDrawerItem(String name, LatLng latLng, @Nullable String note, Class targetActivity) {
            super(name, latLng.latitude, latLng.longitude, note, targetActivity);

        }

        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawer) {
            super.inflate(inflater, navDrawer);

            if (this.navDrawer.activity.getClass() == getTargetActivity()) {
                this.navDrawer.setSelectedItem(this);
            }

        }


        @Override
        public void onClick(final View view) {
            navDrawer.setOpen(false);
            if (navDrawer.activity.getClass() == getTargetActivity() && getTargetActivity() == MapsActivity.class) {
                return;
            }
            view.setTag(super.getName());
            super.onClick(view);

            navDrawer.activity.fadeOut(new BaseActivity.FadeOutListener() {
                @Override
                public void onFadeOutEnd() {
                    Intent intent = new Intent(
                            navDrawer.activity.getApplicationContext(),
                            ActivityNavDrawerItem.super.targetActivity);
                    intent.putExtras(ActivityNavDrawerItem.super.bundle);

                    navDrawer.activity.startActivity(intent);
                    navDrawer.activity.finish();
                }
            });

        }
    }

}
