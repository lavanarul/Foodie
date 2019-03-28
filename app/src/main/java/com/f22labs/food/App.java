package com.f22labs.food;

import android.app.Application;
import android.databinding.DataBindingUtil;

import com.f22labs.food.databinding.AppDataBindingComponent;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataBindingUtil.setDefaultComponent(new AppDataBindingComponent());
    }
}
