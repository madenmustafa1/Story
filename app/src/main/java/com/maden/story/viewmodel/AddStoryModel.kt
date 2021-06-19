package com.maden.story.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddStoryModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    fun share(title: String, story: String){
        var t: String
        var s: String

        t = title.replace(" ", "").trim()
        s = story.replace(" ", "").trim()

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
                    println("Story")
                    //val action = AddStoryFragmentDirections.actionAddStoryFragmentToShowStoryFragment()
                    //Navigation.findNavController(context, R.id.main_navigation).navigate(action)
                    //findNavController(R.id.main_navigation).navigate(action)

                }
                .addOnFailureListener{
                    println("Fail")
                }
        }



    }
}