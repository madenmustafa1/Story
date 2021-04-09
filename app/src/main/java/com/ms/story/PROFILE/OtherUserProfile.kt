package com.ms.story.PROFILE


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.R
import com.ms.story.STORY.GLOBAL_CURRENT_FRAGMENT
import com.ms.story.Adapter.RecyclerShowStory
import kotlinx.android.synthetic.main.fragment_other_user_profile.*


class OtherUserProfile : Fragment() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private lateinit var profileAdapter: RecyclerShowStory

    private var userNameList = ArrayList<String>()
    private var userTitleList = ArrayList<String>()
    private var userStoryList = ArrayList<String>()
    private var userLikeList = ArrayList<String>()
    private var uuidList = ArrayList<String>()
    private var userProfilePhoto = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_user_profile, container, false)
    }
    lateinit var otherUserEmail: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        GLOBAL_CURRENT_FRAGMENT = "other_profile_story"

        arguments?.let {
            otherUserEmail = OtherUserProfileArgs.fromBundle(it).userEmail
        }

        profileAdapter = RecyclerShowStory(userNameList, userTitleList, userStoryList, userLikeList, uuidList, userProfilePhoto)

        otherUserProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        otherUserProfileRecyclerView.adapter = profileAdapter


        userFollowButton.setOnClickListener {
            otherUserFollow(it)
        }


        getUserProfile()
    }

    private fun getUserProfile() {
        try {
            if (activity != null) {

                otherUserFollowedControl()

                userNameList.clear()
                userTitleList.clear()
                userStoryList.clear()
                userLikeList.clear()
                uuidList.clear()
                //Followed
                val dbRef = db.collection("Profile")
                        .document(otherUserEmail)
                dbRef.get().addOnSuccessListener {
                    if (it != null) {

                        var nameSurname: String? = null

                        val nameRef = db.collection("Profile")
                        nameRef.whereEqualTo("email", otherUserEmail)
                                .get().addOnSuccessListener {
                                    for (name in it) {
                                        var profileNS = name["name"].toString() + " " + name["surname"].toString()
                                        otherProfileNameSurname.text = profileNS

                                        val str = name["followed"].toString()
                                        var arr = str.dropWhile { !it.isLetter() }
                                        arr = arr.dropLastWhile { !it.isLetter() }
                                        var profileFollowedArr = arr.split(",", " ")

                                        otherProfileFollowed.text = profileFollowedArr.size.toString()

                                        val dbbRef = db.collection("Story")
                                        dbbRef.whereEqualTo("email", otherUserEmail)
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

    var visible: Boolean = true
    private fun otherUserFollowedControl(){
        val followedRef = db.collection("Profile")
                .document(auth.currentUser.email.toString())
        followedRef.get().addOnSuccessListener {
            val str = it["followed"].toString()
            var arr = str.dropWhile { !it.isLetter() }
            arr = arr.dropLastWhile { !it.isLetter() }
            var followed = arr.split(",", " ")


            for (followed in followed){
                if (otherUserEmail == followed){
                    visible = false
                    userFollowButton.text = "Unfollow"
                }
            }
            if(visible){
                //userFollowButton.visibility = View.VISIBLE
                userFollowButton.text = "Follow"
            }
        }
    }

    fun otherUserFollow(view: View){

        val followRef = db.collection("Profile")
                .document(auth.currentUser?.email.toString())

        followRef.get().addOnSuccessListener {
            if(visible){
                followRef.update("followed", FieldValue.arrayUnion(otherUserEmail))
                userFollowButton.text = "Unfollow"
                visible = false
            } else {
                followRef.update("followed", FieldValue.arrayRemove(otherUserEmail))
                userFollowButton.text = "Follow"
                visible = true
            }
        }
    }
}