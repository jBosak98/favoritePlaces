package com.jbosak.favoriteplaces;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class NavDrawer {
    private ArrayList<NavDrawerItem> items;
    private NavDrawerItem selectedItem;

    protected BaseActivity activity;
    protected ViewGroup navDrawerView;
    protected DrawerLayout drawerLayout;

    public NavDrawer(BaseActivity activity){
        this.activity = activity;
        items = new ArrayList<>();
       // drawerLayout =
       // drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        navDrawerView = (ViewGroup) activity.findViewById(R.id.nav_drawer);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);


        //if(drawerLayout == null || navDrawerView == null) {
       //     throw new RuntimeException(("To use this class, " +
      //              "you must have views with the ids of drawer_layout and nav_drawer"));

        //  }

        Toolbar toolbar = activity.getToolbar();


        toolbar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){
                    if(drawerLayout != null){
                        Log.e("NULL!","FEE");
                    }
                    setOpen(!isOpen());
                }
            });
        }


    public void addItem(NavDrawerItem item) {
        items.add(item);
        item.navDrawer = this;
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


    public static abstract class NavDrawerItem{
        protected NavDrawer navDrawer;

        public abstract void inflate(LayoutInflater inflater, ViewGroup container);
        public abstract void setSelected(boolean isSelected);
    }
    public static class BasicNavDrawerItem extends NavDrawerItem implements View.OnClickListener {
        private String text;
        private int containerId;
        private View view;
        private TextView textView;

        public BasicNavDrawerItem(String text, int containerId) {
            this.text = text;
            this.containerId = containerId;
        }

        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawerView){
            ViewGroup container = (ViewGroup) navDrawerView.findViewById(containerId);
            if(container == null){
                throw new RuntimeException("Nav drawer item" + text +
                        "could not be attached to ViewGroup, View not found.");
            }
            view = inflater.inflate(R.layout.list_item_nav_drawer, container,false);
            container.addView(view);

            view.setOnClickListener(this);

            textView = (TextView) view.findViewById(R.id.list_item_nav_drawer_text);
            textView.setText(text);
        }

        @Override
        public void setSelected(boolean isSelected) {

        }


        public void setText(String text){
            this.text = text;
            if(view != null){
                textView.setText(text);
            }

        }
        @Override
        public void onClick(View view) {
            navDrawer.setSelectedItem(this);

        }

    }

    public static class ActivityNavDrawerItem extends BasicNavDrawerItem{
        private final Class targetActivity;

        public ActivityNavDrawerItem( String text, int containerId) {
            super(text, containerId);
            targetActivity = NoteActivity.class;

        }
        @Override
        public void inflate(LayoutInflater inflater, ViewGroup navDrawer){
            super.inflate(inflater,navDrawer);

            if(this.navDrawer.activity.getClass() == targetActivity){
                this.navDrawer.setSelectedItem(this);
            }
        }



        @Override
        public void onClick(View view){
            navDrawer.setOpen(false);
            if(navDrawer.activity.getClass() == targetActivity){
                return;
            }
            super.onClick(view);


            navDrawer.activity.fadeOut(new BaseActivity.FadeOutListener() {
                @Override
                public void onFadeOutEnd() {
                    navDrawer.activity.startActivity(new Intent(navDrawer.activity,targetActivity));
                    navDrawer.activity.finish();

                }
            });

        }
    }

}
