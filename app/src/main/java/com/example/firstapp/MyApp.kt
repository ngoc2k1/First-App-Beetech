package com.example.firstapp

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.orhanobut.hawk.Hawk

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.setClientToken("4913316aff7640a63ba9a8aabe01f97f")
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        Hawk.init(applicationContext).build();
    }
}