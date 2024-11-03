package com.example.flashcardapp.common

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthUtils {
    val currentId : String?
        get() = Firebase.auth.currentUser?.uid
}