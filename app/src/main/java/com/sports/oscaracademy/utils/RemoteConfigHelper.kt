package com.sports.oscaracademy.utils

import android.content.Context
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

abstract class RemoteConfigHelper {

    companion object {
        private val PRICE = "courtPrice"
        private val DEFAULTS: HashMap<String, Any> = hashMapOf(
            PRICE to 350
        )
        var INSTANCE: FirebaseRemoteConfig? = null
        fun getInstance(context: Context): FirebaseRemoteConfig {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Firebase.remoteConfig
                    val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 0
                    }
                    INSTANCE?.apply {
                        setConfigSettingsAsync(configSettings)
                        setDefaultsAsync(DEFAULTS)
                        fetchAndActivate().addOnCompleteListener {
                            Log.e("TAG", "getInstance: ${it.result}")
                        }
                    }
                }
            }
            return INSTANCE!!
        }
    }


}