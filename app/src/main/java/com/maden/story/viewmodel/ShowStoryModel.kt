package com.maden.story.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.model.FeedData

class ShowStoryModel : ViewModel() {

    private val storage = FirebaseStorage.getInstance()
    val reference = storage.reference

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    lateinit var followed: List<String>
    val feedDataClass = MutableLiveData<List<FeedData>>()
    private var nameSurname: String? = null


    fun getStory() {
        val dbRef = db.collection("Profile")
            .document(auth.currentUser.email.toString())
        dbRef.get().addOnSuccessListener {

            if (it["followed"] != null) {
                followed = it["followed"] as List<String>
                nameSurname = null

                for (followed in followed) {
                    val nameRef = db.collection("Profile")

                    nameRef.whereEqualTo("email", followed)
                        .get().addOnSuccessListener {
                            for (name in it) {
                                val dbbRef = db.collection("Story")
                                dbbRef
                                    //.orderBy("date", Query.Direction.DESCENDING)
                                    .whereEqualTo("email", followed)
                                    .get().addOnSuccessListener { it ->

                                        for (document in it) {
                                            nameSurname =
                                                name["name"].toString() + " " + name["surname"].toString()

                                            if (nameSurname == null) {
                                                nameSurname = "Unknown"
                                            }

                                            var like = document["like"] as List<String>
                                            val userTitle = document["title"].toString()
                                            val userStory = document["story"].toString()
                                            val userLike = "" + like.size + " Like"
                                            val uuid = document["uuid"].toString()
                                            val email_PNG_forPhoto =
                                                document["email"].toString() + ".PNG"

                                            val userFeed = FeedData(
                                                nameSurname, userTitle,
                                                userStory, userLike, uuid,
                                                email_PNG_forPhoto
                                            )

                                            val feedList = arrayListOf<FeedData>(userFeed)
                                            feedDataClass.value = feedList

                                            /*
                                            val userProfileReference = reference.child("userProfile").child("$document.PNG")
                                            userProfileReference.downloadUrl.addOnSuccessListener {
                                                profileURL = it.toString()
                                                if(profileURL != null){
                                                    userProfilePhoto.add(profileURL!!)
                                                    println(profileURL)
                                                }
                                            }
                                             */


                                        }
                                    }
                            }
                        }
                }
            }
        }
    }


}