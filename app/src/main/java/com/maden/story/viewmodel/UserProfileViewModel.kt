package com.maden.story.viewmodel

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.model.ProfileAdapterData
import com.maden.story.model.ProfileData

class UserProfileViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private val storage = FirebaseStorage.getInstance()

    val profileDataClass = MutableLiveData<List<ProfileData>>()
    val profileAdapterDataClass = MutableLiveData<List<ProfileAdapterData>>()
    val uProfilePhoto = MutableLiveData<String>()

    fun getMyUserProfile() {
        val ref = storage.reference

        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(auth.currentUser!!.email!!.toString())

            profileRef.get().addOnSuccessListener {
                if (it != null) {

                    val nameSurname = it["name"].toString() + " " +
                            it["surname"].toString()
                    val email = it["email"].toString()
                    val username = it["username"].toString()

                    var followed = it["followed"] as List<String>
                    var follower = it["followed"] as List<*>

                    val myProfileData = ProfileData(
                        nameSurname, email, username,
                        followed.size.toString(),
                        follower.size.toString(), "5", ""
                    )

                    profileDataClass.value = listOf(myProfileData)
                }
            }.addOnCompleteListener {
                getMyPost()
                ref.child(profileDataClass.value?.get(0)!!.userEmail!!)
                    .child("profilePhoto")
                    .downloadUrl.addOnSuccessListener {
                        if (it != null){
                            uProfilePhoto.value = it.toString()
                        }
                    }
            }
        }
    }


    private fun getMyPost() {

        val postRef = db.collection("Story")
        postRef//.whereEqualTo("email", auth.currentUser!!.email)
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for (document in it) {
                    if (document["email"] == auth.currentUser!!.email) {
                        val nameSurname = profileDataClass.value?.get(0)?.userNameSurname
                        var like = document["like"] as List<*>
                        val userTitle = document["title"].toString()
                        val userStory = document["story"].toString()
                        val userLike = "" + like.size + " Like"
                        val uuid = document["uuid"].toString()
                        val email_PNG_forPhoto =
                            document["email"].toString() + ".PNG"

                        val myPost = ProfileAdapterData(
                            nameSurname, userTitle,
                            userStory, userLike, uuid,
                            email_PNG_forPhoto
                        )

                        val postList = arrayListOf<ProfileAdapterData>(myPost)
                        profileAdapterDataClass.value = postList
                    }
                }
            }
    }


}