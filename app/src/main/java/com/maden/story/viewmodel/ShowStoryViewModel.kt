package com.maden.story.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.model.FeedData

class ShowStoryViewModel : ViewModel() {


    private var db = Firebase.firestore
    private var auth = Firebase.auth

    lateinit var followed: List<String>
    val feedDataClass = MutableLiveData<List<FeedData>>()
    private var nameSurname: String? = null

    val profilePhoto = MutableLiveData<ArrayList<String>>()

    fun getStory() {
        val dbRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())


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
                                            var downloadUrl: String

                                            nameSurname =
                                                name["name"].toString() + " " + name["surname"].toString()
                                            downloadUrl = name["photoUrl"].toString()


                                            if (nameSurname == null) {
                                                nameSurname = "Unknown"
                                            }

                                            var like = document["like"] as List<String>
                                            val userTitle = document["title"].toString()
                                            val userStory = document["story"].toString()
                                            val userLike = "" + like.size + " Like"
                                            val uuid = document["uuid"].toString()
                                            val email_PNG_forPhoto =
                                                document["email"].toString()

                                            val userFeed = FeedData(
                                                nameSurname, userTitle,
                                                userStory, userLike, uuid,
                                                email_PNG_forPhoto, downloadUrl
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