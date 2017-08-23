package com.jbosak.favoriteplaces;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import static com.jbosak.favoriteplaces.R.id.drawer_layout;


public class BaseActivity extends AppCompatActivity {
    protected NavDrawer navDrawer;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }
    protected void simplySetNavDrawer(NavDrawer drawer){
        this.navDrawer = drawer;
        this.navDrawer.create();
    }

    protected void setNavDrawer(NavDrawer drawer){
        this.navDrawer = drawer;
        this.navDrawer.create();

        overridePendingTransition(0, 0);
        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);
        rootView.animate().alpha(1).setDuration(450).start();
    }

    @Override
    public void  setContentView(@LayoutRes int layoutResId){
        super.setContentView(layoutResId);
        toolbar = (Toolbar) findViewById(R.id.include_toolbar);



    }
    public Toolbar getToolbar() {
        return toolbar;
    }

    public void fadeOut(final FadeOutListener listener){
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
    public interface FadeOutListener{
        void onFadeOutEnd();

    }

}
