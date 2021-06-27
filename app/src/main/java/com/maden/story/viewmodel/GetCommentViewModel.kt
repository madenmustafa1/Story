package com.maden.story.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.model.CommentData
import com.maden.story.model.FeedData
import com.maden.story.model.ProfileAdapterData
import java.util.*
import kotlin.collections.ArrayList

class GetCommentViewModel: ViewModel() {


    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val commentDataClass = MutableLiveData<List<CommentData>>()
    private var nameSurname: String? = null

    val profilePhoto = MutableLiveData<ArrayList<String>>()


    fun getComment(uuid: String){
        val commentRef = db.collection("Story")

        commentRef
            .whereEqualTo("uuid", uuid)
            .get().addOnSuccessListener {
                if (it != null){
                    for (i in it){

                        // buradan yorumlar çekilecek.
                        //i["comment"]




                    }
                }
            }
    }
}