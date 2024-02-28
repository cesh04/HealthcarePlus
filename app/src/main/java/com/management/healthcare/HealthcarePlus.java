package com.management.healthcare;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class HealthcarePlus extends Application {
        @Override
        public void onCreate(){
            super.onCreate();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
}
