package com.maden.story.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class GoCommentViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    fun goComment(uuid: String, comment: String) {
        var co: String = comment.replace(" ", "").trim()

        val myUUID = UUID.randomUUID()
        var username: String
        var parentID: String


        if (co != null && co != "") {
            if (auth.currentUser != null) {
                db.collection("Profile")
                    .document(auth.currentUser!!.email!!.toString())
                    .get().addOnSuccessListener {
                        if (it != null) {
                            username = it["username"].toString()

                            //Postu UUID'sine göre getiriyorum ve
                            // sonrasında parentID'sinden güncelliyorum.
                            val commentRef = db.collection("Story")
                            commentRef
                                .whereEqualTo("uuid", uuid)
                                .get().addOnSuccessListener {
                                    for (i in it) {
                                        parentID = i.id



                                        val commentData = hashMapOf(
                                            "username" to username,
                                            "comment" to comment,
                                            "email" to auth.currentUser?.email.toString(),
                                            "like" to listOf<String>(),
                                            "date" to Timestamp.now(),
                                            "uuid" to myUUID.toString()
                                        )

                                        val newRef = db.collection("Story").document(i.id)
                                        newRef.update("comment", FieldValue.arrayUnion(commentData))

                                    }
                                }
                        }
                    }
            }
        }
    }
}