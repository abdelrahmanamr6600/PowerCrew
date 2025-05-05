package com.example.powercrew.utils

import DataStoreManager
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()

        val dataStore = DataStoreManager(this)
        CoroutineScope(Dispatchers.Main).launch {
            val user = dataStore.userData.firstOrNull()
            user?.let {
                val observer = AppLifecycleObserver(it.userId)
                ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
            }
        }
    }
}