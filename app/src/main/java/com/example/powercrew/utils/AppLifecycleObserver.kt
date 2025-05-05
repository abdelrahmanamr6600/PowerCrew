package com.example.powercrew.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppLifecycleObserver(private val userId: String) : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        changeUserState(userId, false)
    }

    override fun onStart(owner: LifecycleOwner) {
        changeUserState(userId, true)
    }

    private fun changeUserState(userId: String, state: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            FirestoreInstance.db
                .collection(FirestoreCollections.USER)
                .document(userId)
                .update(FirestoreFieldNames.STATE, state)
        }
    }
}
