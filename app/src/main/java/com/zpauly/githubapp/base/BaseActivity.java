package com.zpauly.githubapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.zpauly.githubapp.db.UserDao;
import com.zpauly.githubapp.db.UserModel;
import com.zpauly.githubapp.view.profile.ProfileActivity;

/**
 * Created by zpauly on 16-6-8.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected UserModel userInfo;
    protected String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userInfo = UserDao.queryUser();
        username = getIntent().getStringExtra(ProfileActivity.USERNAME);

        initContent();

        initViews();
    }

    public abstract void initViews();

    public abstract void initContent();
}
