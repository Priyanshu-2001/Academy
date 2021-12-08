package com.sports.oscaracademy.Application

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sports.oscaracademy.utils.RemoteConfigHelper

class MyApplication : Application() {
    var remoteConfig: FirebaseRemoteConfig? = null

    override fun onCreate() {
        super.onCreate()
        if (remoteConfig == null) {
            remoteConfig = RemoteConfigHelper.getInstance()
        }
    }

    @JvmName("getRemoteConfig1")
    fun getRemoteConfigInstance(): FirebaseRemoteConfig {
        if (remoteConfig == null) {
            remoteConfig = RemoteConfigHelper.getInstance()
        }
        return remoteConfig as FirebaseRemoteConfig
    }
}