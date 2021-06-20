package com.maden.story.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.databinding.ItemSearchStoryBinding
import com.maden.story.model.FeedData
import com.maden.story.view.SearchFragmentDirections
import kotlinx.android.synthetic.main.item_search_story.view.*
import kotlinx.android.synthetic.main.item_show_story.view.likeButton

class SearchAdapter(private val searchStoryList: ArrayList<FeedData>) :
    RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var otherUserEmail: String

    class SearchAdapterViewHolder(var view: ItemSearchStoryBinding): RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_search_story, parent, false)
        val view = DataBindingUtil.inflate<ItemSearchStoryBinding>(inflater,
                                                       R.layout.item_search_story,
                                                       parent, false)

        return SearchAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
        holder.view.searchData = searchStoryList[position]




        val dbRef = db.collection("Story")
        dbRef
            .whereEqualTo("uuid", searchStoryList[position].uuid)
            .get().addOnSuccessListener {
                for (i in it) {
                    val newRef = db.collection("Story").document(i.id)
                    newRef.get().addOnSuccessListener {

                        var followed = it["like"] as List<String>

                        for (like in followed) {
                            if (like == auth.currentUser.email) {
                                holder.itemView.likeButton.setImageResource(R.drawable.ic_like_full)
                            }
                        }
                    }
                }
            }

        holder.itemView.imageViewSearchStory.setOnClickListener {
            val emailRef = db.collection("Story")

            emailRef
                .whereEqualTo("uuid", searchStoryList[position].uuid)
                .get().addOnSuccessListener {
                    for (email in it) {

                        val profileRef = db.collection("Profile")
                        profileRef.whereEqualTo("email", email["email"].toString())
                            .get().addOnSuccessListener {
                                for (name in it) {
                                    otherUserEmail = name["email"].toString()
                                    nav(holder.view.root)
                                }
                            }
                    }
                }
        }

        holder.view.likeButton.setOnClickListener {
            val dbRef = db.collection("Story")
            dbRef.whereEqualTo("uuid", searchStoryList[position].uuid)
                .get().addOnSuccessListener {
                    for (i in it) {
                        var followed = i["like"] as List<String>
                        if (followed.isEmpty()) {
                            followed = listOf("")
                        }

                        for (f in followed) {
                            println("asd")
                            if (f == auth.currentUser.email) {
                                val newRef = db.collection("Story").document(i.id)
                                newRef.update(
                                    "like",
                                    FieldValue.arrayRemove(auth.currentUser.email)
                                )

                                holder.itemView.likeButton.setImageResource(R.drawable.ic_like)
                            } else {
                                val newRef = db.collection("Story").document(i.id)
                                newRef.update("like", FieldValue.arrayUnion(auth.currentUser.email))
                                holder.itemView.likeButton.setImageResource(R.drawable.ic_like_full)
                            }
                        }
                    }
                }
        }

        /*
        holder.view.userStoryText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_SCROLL -> v!!.parent.requestDisallowInterceptTouchEvent(false);

                }
                //switch case


                return v?.onTouchEvent(event) ?: true
            }
        })

         */
    }

    override fun getItemCount(): Int {
        return searchStoryList.size
    }

    fun updateSearchList(newShowList: List<FeedData>) {
        //searchStoryList.clear()
        searchStoryList.addAll(newShowList)
        notifyDataSetChanged()
    }

    private fun nav(view: View){
        if(GLOBAL_CURRENT_FRAGMENT == "search_story"){
            val action =
                SearchFragmentDirections.actionSearchFragmentToOtherUserProfileFragment(otherUserEmail)
            view.findNavController().navigate(action)
            //Navigation.findNavController(view, R.id.story_Layout).navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }


}