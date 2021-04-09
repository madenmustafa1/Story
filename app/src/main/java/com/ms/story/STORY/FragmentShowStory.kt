package com.ms.story.STORY

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.ms.story.Adapter.RecyclerShowStory
import com.ms.story.R
import kotlinx.android.synthetic.main.fragment_show_story.*
import kotlin.collections.ArrayList


class FragmentShowStory : Fragment() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private var userNameList = ArrayList<String>()
    private var userTitleList = ArrayList<String>()
    private var userStoryList = ArrayList<String>()
    private var userLikeList = ArrayList<String>()
    private var uuidList = ArrayList<String>()
    lateinit var followed : List<String>
    private var userProfilePhoto = ArrayList<String>()

    private var emailList = ArrayList<String>()

    lateinit var storyAdapter: RecyclerShowStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        storyAdapter = RecyclerShowStory(userNameList, userTitleList, userStoryList, userLikeList, uuidList, userProfilePhoto)
        recyclerStoryView.layoutManager = LinearLayoutManager(context)

        recyclerStoryView.adapter = storyAdapter

        getStory()

        GLOBAL_CURRENT_FRAGMENT = "show_story"
        //(activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
    val storage = FirebaseStorage.getInstance()
    val reference = storage.reference
    var profileURL : String? = null
    var photoAccess : Boolean? = null
    //var nameSurname : String? = null
    private fun getStory(){
        try {
            if(activity != null){
                userNameList.clear()
                userTitleList.clear()
                userStoryList.clear()
                userLikeList.clear()
                uuidList.clear()
                userProfilePhoto.clear()



                val dbRef = db.collection("Profile")
                        .document(auth.currentUser.email.toString())

                dbRef.get().addOnSuccessListener {

                    if(it != null){
                        val str = it["followed"].toString()
                        var arr = str.dropWhile { !it.isLetter() }
                        arr = arr.dropLastWhile { !it.isLetter() }
                        followed = arr.split(",", " ")
                        var nameSurname : String? = null


                        for (followed in followed) {

                                    val nameRef = db.collection("Profile")
                                    nameRef.whereEqualTo("email", followed)
                                        .get().addOnSuccessListener {
                                            for(name in it){

                                                val dbbRef = db.collection("Story")
                                                dbbRef.orderBy("date", Query.Direction.DESCENDING)
                                                    .whereEqualTo("email", followed)
                                                    .get().addOnSuccessListener { it ->
                                                        for (document in it){
                                                            nameSurname = name["name"].toString()+ " " + name["surname"].toString()

                                                            if(nameSurname != null){
                                                                userNameList.add(nameSurname!!)
                                                            } else {
                                                                userNameList.add("Unknown")
                                                            }
                                                            var str = document["like"].toString()
                                                            var arr = str.split(",", " ")

                                                            userTitleList.add(document["title"].toString())
                                                            userStoryList.add(document["story"].toString())
                                                            userLikeList.add(""+arr.size + " Like")
                                                            uuidList.add(document["uuid"].toString())

                                                            emailList.add(document["email"].toString())



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
                                                        storyAdapter.notifyDataSetChanged()
                                                    }
                                            }
                                        }

                                }
                            }
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}
