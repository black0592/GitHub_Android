package com.zpauly.githubapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zpauly.githubapp.R;
import com.zpauly.githubapp.listener.OnNavItemClickListener;

/**
 * Created by zpauly on 2016/10/15.
 */

public abstract class RightDrawerActivity extends ToolbarActivity {
    private final String TAG = getClass().getName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mRightNav;
    private AppCompatTextView mTitleTV;

    private ViewGroup mContentRoot;

    private Menu mNavMenu;

    private OnNavItemClickListener mOnNavItemClickListener;

    public void setOnNavItemClickListener(OnNavItemClickListener listener) {
        mOnNavItemClickListener = listener;
    }

    private void initNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        mRightNav = (NavigationView) findViewById(R.id.right_nav_view);

        View mNavHeader = mRightNav.getHeaderView(0);
        mTitleTV = (AppCompatTextView) mNavHeader.findViewById(R.id.right_nav_title);

        setupNavMenu();
    }

    private void setupNavMenu() {
        initNavMenu();
        mRightNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mOnNavItemClickListener != null) {
                    mOnMenuItemSelectedListener.onItemSelected(item);
                    closeDrawer();
                }
                return false;
            }
        });
    }

    @Override
    protected void setContent(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, mContentRoot, false);
        mContentRoot.addView(view);
    }

    protected void setNavHeaderTitle(int resId) {
        mTitleTV.setText(resId);
    }

    protected void setNavHeaderTitle(CharSequence text) {
        mTitleTV.setText(text);
    }

    public abstract void initNavMenu();

    protected void inflaterNavMenu(int menuResId) {
        mRightNav.inflateMenu(menuResId);
    }

    @Override
    public void initContent() {
        setContentView(R.layout.right_drawer_layout);
        mContentRoot = (ViewGroup) findViewById(R.id.nav_content);
        initNavDrawer();
    }

    protected void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    protected void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }
}
