package com.example.administrator.mapclient.activity.homepage;

import android.app.Application;

/**
 *初始化框架
 * Created by Administrator on 2017/2/20.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        //初始化框架
       // SDKInitializer.initialize(getApplicationContext());
        super.onCreate();
    }
}
