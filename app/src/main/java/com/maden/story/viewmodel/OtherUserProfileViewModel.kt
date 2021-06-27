package com.maden.story.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.model.OtherProfileAdapterData
import com.maden.story.model.OtherProfileData


class OtherUserProfileViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private val storage = FirebaseStorage.getInstance()
    //val reference = storage.reference

    val otherProfileDataClass = MutableLiveData<List<OtherProfileData>>()
    val otherProfileAdapterDataClass = MutableLiveData<List<OtherProfileAdapterData>>()
    val uFollowing = MutableLiveData<String>()
    val otherProfilePhotoDownloadUrl = MutableLiveData<String>()

    fun getOtherUserProfile(email: String) {
        uFollowing.value = "false"
        val ref = storage.reference

        val profileRef = db.collection("Profile")
            .document(email)

        profileRef.get().addOnSuccessListener {
            if (it != null) {

                val nameSurname = it["name"].toString() + " " +
                        it["surname"].toString()
                val email = it["email"].toString()
                val username = it["username"].toString()

                var followed = it["followed"] as List<String>
                var follower = it["followed"] as List<*>

                val followedRef = db.collection("Profile")
                    .document(auth.currentUser?.email.toString())

                followedRef.get().addOnSuccessListener {
                    val followed = it["followed"] as List<*>

                    for (f in followed) {
                        if (email == f) {
                            uFollowing.value = "true"
                        }
                    }
                }.addOnCompleteListener {
                    val myProfileData = OtherProfileData(
                        nameSurname, email, username,
                        followed.size.toString(),
                        follower.size.toString(), "5",
                        uFollowing.value
                    )
                    otherProfileDataClass.value = listOf(myProfileData)

                    ref.child(otherProfileDataClass.value?.get(0)!!.userEmail!!)
                        .child("profilePhoto")
                        .downloadUrl.addOnSuccessListener {
                            if (it != null) {
                                otherProfilePhotoDownloadUrl.value = it.toString()
                            }
                        }
                    getOtherUserPost()
                }
            }
        }
    }

    private fun getOtherUserPost() {
        val postRef = db.collection("Story")
        postRef//.whereEqualTo("email", auth.currentUser!!.email)
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for (document in it) {
                    println(otherProfileDataClass.value?.get(0)?.userEmail)
                    if (document["email"] == otherProfileDataClass.value?.get(0)?.userEmail) {
                        val nameSurname = otherProfileDataClass.value?.get(0)?.userNameSurname
                        var like = document["like"] as List<*>
                        val userTitle = document["title"].toString()
                        val userStory = document["story"].toString()
                        val userLike = "" + like.size + " Like"
                        val uuid = document["uuid"].toString()
                        val email_PNG_forPhoto =
                            document["email"].toString() + ".PNG"

                        val myPost = OtherProfileAdapterData(
                            nameSurname, userTitle,
                            userStory, userLike, uuid,
                            email_PNG_forPhoto
                        )

                        val postList = arrayListOf<OtherProfileAdapterData>(myPost)
                        otherProfileAdapterDataClass.value = postList
                    }

                }
            }
    }

    fun followUser(email: String) {
        val followRef = db.collection("Profile")
            .document(auth.currentUser?.email.toString())

        followRef.get().addOnSuccessListener {
            if (uFollowing.value == "false") {
                followRef.update("followed", FieldValue.arrayUnion(email))
                uFollowing.value = "true"

            } else {
                followRef.update("followed", FieldValue.arrayRemove(email))
                uFollowing.value = "false"
            }
        }
    }
}