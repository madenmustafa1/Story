package com.ms.story.PROFILE

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.R
import com.ms.story.STORY.GLOBAL_CURRENT_FRAGMENT
import com.ms.story.Adapter.RecyclerShowStory
import kotlinx.android.synthetic.main.fragment_user_profile.*


class UserProfile : Fragment() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private lateinit var profileAdapter: RecyclerShowStory

    private var userNameList = ArrayList<String>()
    private var userTitleList = ArrayList<String>()
    private var userStoryList = ArrayList<String>()
    private var userLikeList = ArrayList<String>()
    private var uuidList = ArrayList<String>()
    lateinit var followed: List<String>
    private var userProfilePhoto = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        GLOBAL_CURRENT_FRAGMENT = "profile_story"



        profileAdapter = RecyclerShowStory(userNameList, userTitleList, userStoryList, userLikeList, uuidList, userProfilePhoto)
        userProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        userProfileRecyclerView.adapter = profileAdapter

        getUserProfile()
    }

    private fun getUserProfile() {
        try {
            if (activity != null) {
                userNameList.clear()
                userTitleList.clear()
                userStoryList.clear()
                userLikeList.clear()
                uuidList.clear()
                //Followed
                val dbRef = db.collection("Profile")
                        .document(auth.currentUser.email.toString())
                dbRef.get().addOnSuccessListener {
                    if (it != null) {
                        /*
                        val str = it["followed"].toString()
                        var arr = str.dropWhile { !it.isLetter() }
                        arr = arr.dropLastWhile { !it.isLetter() }
                        followed = arr.split(",", " ")
                         */

                        var nameSurname: String? = null

                        val nameRef = db.collection("Profile")
                        nameRef.whereEqualTo("email", auth.currentUser.email)
                                .get().addOnSuccessListener {
                                    for (name in it) {
                                        var profileNS = name["name"].toString() + " " + name["surname"].toString()
                                        profileNameSurname.text = profileNS

                                        val str = name["followed"].toString()
                                        var arr = str.dropWhile { !it.isLetter() }
                                        arr = arr.dropLastWhile { !it.isLetter() }
                                        var profileFollowedArr = arr.split(",", " ")

                                        profileFollowed.text = profileFollowedArr.size.toString()

                                        val dbbRef = db.collection("Story")
                                        dbbRef.whereEqualTo("email", auth.currentUser.email)
                                                .orderBy("date", Query.Direction.DESCENDING)
                                                .get().addOnSuccessListener {
                                                    for (document in it) {
                                                        nameSurname = name["name"].toString() + " " + name["surname"].toString()

                                                        if (nameSurname != null) {
                                                            userNameList.add(nameSurname!!)
                                                        } else {
                                                            userNameList.add("Unknown")
                                                        }
                                                        var str = document["like"].toString()
                                                        var arr = str.split(",", " ")

                                                        userTitleList.add(document["title"].toString())
                                                        userStoryList.add(document["story"].toString())
                                                        userLikeList.add("" + arr.size + " Like")
                                                        uuidList.add(document["uuid"].toString())
                                                    }
                                                    profileAdapter.notifyDataSetChanged()
                                                }
                                    }
                                }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}