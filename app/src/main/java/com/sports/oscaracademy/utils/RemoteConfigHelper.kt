package com.sports.oscaracademy.utils

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

abstract class RemoteConfigHelper {


    companion object {
        private var INSTANCE: FirebaseRemoteConfig? = null

        private val PRICE = "courtPrice"
        private val ACADEMY_CONTACT = "academyContactNumber"
        private val ACADEMY_EMAIL = "academyContactEmail"
        private val DEFAULTS: HashMap<String, Any> = hashMapOf(
            PRICE to 350,
            ACADEMY_CONTACT to 123456789,
            ACADEMY_EMAIL to "academy.oscarbansal@gmail.com"
        )

        fun getInstance(): FirebaseRemoteConfig {
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
                            Log.e("TAG", "Remote getInstance : ${it.result}")
                        }
                    }
                }
            }
            return INSTANCE!!
        }
    }


}