package com.maden.story.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.model.FeedData

class SearchViewModel : ViewModel() {

    private var db = Firebase.firestore

    lateinit var followed: List<String>
    val feedDataClass = MutableLiveData<List<FeedData>>()
    private var nameSurname: String? = null

    fun searchStory() {
        val dbbRef = db.collection("Story")
        dbbRef
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(5)
            .get().addOnSuccessListener {
                for (document in it) {
                    val dbRef = db.collection("Profile")
                    dbRef
                        .whereEqualTo("email", document["email"].toString())
                        .get().addOnSuccessListener {
                            if (it != null) {
                                for (nameS in it) {
                                    var downloadUrl: String
                                    nameSurname = nameS["name"].toString() +
                                            " " + nameS["surname"].toString()
                                    downloadUrl = nameS["photoUrl"].toString()

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
                                        email_PNG_forPhoto, downloadUrl
                                    )

                                    val feedList = arrayListOf<FeedData>(userFeed)
                                    feedDataClass.value = feedList
                                }
                            }
                        }
                }
            }
    }


}