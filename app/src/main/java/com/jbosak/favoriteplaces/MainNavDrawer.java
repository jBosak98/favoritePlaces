package com.jbosak.favoriteplaces;

/**
 * Created by root on 8/22/17.
 */

public class MainNavDrawer extends NavDrawer {


    public MainNavDrawer(BaseActivity activity) {
        super(activity);
        addItem( new ActivityNavDrawerItem("Favourite1",R.id.include_main_nav_drawer_topItems));
    }
}
