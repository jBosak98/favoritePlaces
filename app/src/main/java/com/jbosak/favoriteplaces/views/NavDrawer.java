package com.jbosak.favoriteplaces.views;

import android.app.Activity;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.ArrayList;



public class NavDrawer {
    public static ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();

    private NavDrawerItem selectedItem;

    public static BaseActivity activity;
    protected ViewGroup navDrawerView;
    protected DrawerLayout drawerLayout;

    public NavDrawer(BaseActivity activity){

        this.activity = activity;
        Log.e(activity.toString(),"CLASS");
        navDrawerView = (ViewGroup) activity.findViewById(R.id.nav_drawer);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);


        Toolbar toolbar = activity.getToolbar();


        toolbar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    if(drawerLayout != null){
                    }
                    setOpen(!isOpen());
                }
            });
        }


    public void addItem(NavDrawerItem item) {
        items.add(item);
        item.navDrawer = this;
    }
    public void deleteItems(){
        items.removeAll(items);

    }
    public int size(){
        return items.size();
    }
    public boolean isOpen(){
        return drawerLayout.isDrawerOpen(Gravity.START);
    }
    public void setOpen(boolean isOpen){
        if(isOpen){
            drawerLayout.openDrawer(Gravity.START);
        }else{
            drawerLayout.closeDrawer(Gravity.START);
        }
    }
    public void setSelectedItem(NavDrawerItem item){
        if(selectedItem != null){
            selectedItem.setSelected(false);
        }
        selectedItem = item;
        selectedItem.setSelected(true);

    }
    public void create(){
        LayoutInflater inflater = activity.getLayoutInflater();

        for(NavDrawerItem item : items){
            item.inflate(inflater, navDrawerView);
        }


    }

    public void addOnce() {
        LayoutInflater inflater = activity.getLayoutInflater();

            items.get(items.size()-1).inflate(inflater, navDrawerView);

    }

    public void refreshActivity(BaseActivity activity) {
        this.activity = activity;
    }


    public static abstract class NavDrawerItem implements Parcelable {
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
        public double longitude;

        private Class targetActivity;

        @Override
        public int describeContents() {
            return 0;
        }


        public Class getTargetActivity() {
            return targetActivity;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(getName());
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
            dest.writeString(getNote());


        }

        public BasicNavDrawerItem(Parcel in){
            this.name = in.readString();
            this.note = in.readString();
            this.latitude = in.readDouble();
            this.longitude = in.readDouble();



        }
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public BasicNavDrawerItem createFromParcel(Parcel in) {
                return new BasicNavDrawerItem(in);
            }

            public BasicNavDrawerItem[] newArray(int size) {
                return new BasicNavDrawerItem[size];
            }
        };




        public BasicNavDrawerItem(String name, Double latitude, Double longitude,@Nullable String note, Class targetActivity) {
            this.name = name;
            this.note = note;
            this.latitude = latitude;
            this.longitude = longitude;
            this.targetActivity = targetActivity;

        }


        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView){

            ViewGroup container = (ViewGroup) navDrawerView.findViewById(R.id.include_main_nav_drawer_topItems);
            if(container == null){
                throw new RuntimeException("Nav drawer item" + name +
                        "could not be attached to ViewGroup, View not found.");
            }

            view = inflater.inflate(R.layout.list_item_nav_drawer, container,false);
            container.addView(view);


            view.setOnClickListener(this);

            textView = (TextView) view.findViewById(R.id.list_item_nav_drawer_text);
            textView.setText(name);
        }

        @Override
        public void setSelected(boolean isSelected) {

        }




        public void setName(String text){
            this.name = text;
            if(view != null){
                textView.setText(text);
            }

        }
        @Override
        public void onClick(View view) {
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

    public static class ActivityNavDrawerItem extends BasicNavDrawerItem implements Serializable{
        private Class target;


        //public ActivityNavDrawerItem(String name, LatLng latLng,) {
     //       super(name, latLng.latitude,latLng.longitude, note);
      //      targetActivity = NoteActivity.class;

       // }

        public ActivityNavDrawerItem(String name,LatLng latLng, @Nullable String note, Class targetActivity){
            super(name,latLng.latitude,latLng.longitude,note,targetActivity);
            target = targetActivity;

        }
        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawer){
            super.inflate(inflater,navDrawer);

            if(this.navDrawer.activity.getClass() == target){
                this.navDrawer.setSelectedItem(this);
            }

        }



        @Override
        public void onClick(final View view){
            //Log.e(activity.toString(),"HERE");
            navDrawer.setOpen(false);
            Log.e("TARGET",target.toString());
            Log.e("TARGET2",ActivityNavDrawerItem.super.targetActivity.toString());

            Log.e("THIS:",navDrawer.activity.getClass().toString());
            if(navDrawer.activity.getClass() == target){
                return;
            }
            view.setTag(super.getName());
            super.onClick(view);



            navDrawer.activity.fadeOut(new BaseActivity.FadeOutListener() {
                @Override
                public void onFadeOutEnd() {

                    Intent intent = new Intent(navDrawer.activity.getApplicationContext(),ActivityNavDrawerItem.super.targetActivity);
                    intent.putExtra("NAME", ActivityNavDrawerItem.super.targetActivity.toString());



                    navDrawer.activity.startActivity(intent);
                    navDrawer.activity.finish();

                }
            });

        }
    }

}
