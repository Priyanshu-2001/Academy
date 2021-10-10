package com.sports.oscaracademy.Application

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sports.oscaracademy.utils.RemoteConfigHelper

class MyApplication : Application() {
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        remoteConfig = RemoteConfigHelper.getInstance(applicationContext)
    }
}