package com.example.powercrew.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseAuthInstance {
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}