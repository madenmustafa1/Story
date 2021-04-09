package com.ms.story.SEARCH

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.Adapter.RecyclerSearch
import com.ms.story.R
import com.ms.story.STORY.GLOBAL_CURRENT_FRAGMENT
import com.ms.story.Adapter.RecyclerShowStory
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private var userNameList = ArrayList<String>()
    private var userTitleList = ArrayList<String>()
    private var userStoryList = ArrayList<String>()
    private var userLikeList = ArrayList<String>()
    private var uuidList = ArrayList<String>()
    private var userProfilePhoto = ArrayList<String>()


    private lateinit var storyAdapter: RecyclerShowStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        GLOBAL_CURRENT_FRAGMENT = "search_story"

        storyAdapter = RecyclerShowStory(userNameList, userTitleList, userStoryList, userLikeList, uuidList, userProfilePhoto)
        recyclerSearchStoryView.layoutManager = LinearLayoutManager(context)
        recyclerSearchStoryView.adapter = storyAdapter

        getSearchStory()
        search()

        recyclerSearchStoryView.setOnClickListener{
            searchUserView.stopScroll()
        }
    }

    private var searchUserNameList = ArrayList<String>()
    private var searchFollowedList = ArrayList<String>()
    private var searchEmailList = ArrayList<String>()
    private lateinit var searchAdapter: RecyclerSearch
    private fun search(){
        val search = searchView
        searchAdapter = RecyclerSearch(searchUserNameList, searchFollowedList, searchEmailList)

        searchUserView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //searchUserView.layoutManager = LinearLayoutManager(context)
        searchUserView.adapter = searchAdapter


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                val dbbRef = db.collection("Profile")
                dbbRef
                        .whereEqualTo(query!!, "name")
                        .get()
                        .addOnSuccessListener {
                            for (document in it) {
                                //println(document["name"].toString())
                            }
                        }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null || newText != "") {
                    val text = newText
                    if (text != null && text != "") {

                        searchUserNameList.clear()
                        searchFollowedList.clear()
                        val dbbRef = db.collection("Profile")
                        dbbRef
                                .whereGreaterThanOrEqualTo("name", text)
                                .get()
                                .addOnSuccessListener {
                                    for (document in it) {

                                        val str = document["followed"].toString()
                                        var arr = str.dropWhile { !it.isLetter() }
                                        arr = arr.dropLastWhile { !it.isLetter() }
                                        val followedSize = arr.split(",", " ")

                                         recyclerSearchStoryView.layoutParams.height = 1135

                                        searchUserView.visibility = View.VISIBLE

                                        searchUserNameList.add(document["name"].toString() + " " + document["surname"].toString())
                                        searchFollowedList.add("" + (followedSize.size - 1))
                                        searchEmailList.add(document["email"].toString())
                                    }

                                    searchAdapter.notifyDataSetChanged()
                                }
                    } else {
                        recyclerSearchStoryView.layoutParams.height = 1630
                        searchUserView.visibility = View.INVISIBLE
                        searchUserNameList.clear()
                        searchFollowedList.clear()
                        searchAdapter.notifyDataSetChanged()

                    }
                }
                return false
            }
        })
    }

    private fun getSearchStory(){
        try {
            if(activity != null){
                userNameList.clear()
                userTitleList.clear()
                userStoryList.clear()
                userLikeList.clear()
                var nameSurname : String? = null

                val dbbRef = db.collection("Story")
                dbbRef
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(5)
                    .get().addOnSuccessListener {
                        for (document in it){
                            val dbRef = db.collection("Profile")
                            dbRef
                                .whereEqualTo("email", document["email"].toString())
                                .get().addOnSuccessListener {
                                    if (it != null){
                                        for (nameS in it){
                                            nameSurname = nameS["name"].toString()+ " " + nameS["surname"].toString()

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
                                        }
                                        storyAdapter.notifyDataSetChanged()
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