package com.maden.story.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.view.AddStoryFragmentDirections
import java.util.*

class AddStoryViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    fun share(title: String, story: String){

        var t: String = title.replace(" ", "").trim()
        var s: String = story.replace(" ", "").trim()


        if (s != "" && s != null && t != "" && t != null) {
            val uuid = UUID.randomUUID()

            val data = hashMapOf(
                "title" to title,
                "story" to story,
                "email" to auth.currentUser?.email.toString(),
                "like" to listOf<String>(),
                "comment" to listOf<String>(),
                "date" to Timestamp.now(),
                "uuid" to uuid.toString()
            )



            db.collection("Story")
                .add(data)
                .addOnSuccessListener {




                }
                .addOnFailureListener{
                    println("Fail")
                }
        }



    }
}