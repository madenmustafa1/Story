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
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.databinding.ItemShowStoryBinding
import com.maden.story.model.DPU
import com.maden.story.model.DownloadPhotoUrl
import com.maden.story.model.FeedData
import com.maden.story.model.ProfileData
import com.maden.story.util.downloadPhoto
import com.maden.story.view.SearchFragmentDirections
import com.maden.story.view.ShowStoryFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_show_story.view.*
import kotlinx.android.synthetic.main.item_show_story.view.likeButton


//Yorum kısmı entegre edilecek.
class ShowStoryAdapter(private val showStoryList: ArrayList<FeedData>) :
    RecyclerView.Adapter<ShowStoryAdapter.ShowViewHolder>() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var otherUserEmail: String

    class ShowViewHolder(var view: ItemShowStoryBinding) :
        RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_show_story, parent, false)
        val view = DataBindingUtil.inflate<ItemShowStoryBinding>(
            inflater,
            R.layout.item_show_story,
            parent, false
        )
        return ShowViewHolder(view)
    }

    private val dbRef = db.collection("Story")

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.view.showData = showStoryList[position]



        holder.view.commentButtonShowStory.setOnClickListener {
            getComments(showStoryList[position].uuid!!, it)
        }


        //Like Control
        dbRef.whereEqualTo("uuid", showStoryList[position].uuid)
            .get().addOnSuccessListener {
                for (i in it) {
                    val newRef = db.collection("Story").document(i.id)
                    newRef.get().addOnSuccessListener {

                        var followed = it["like"] as List<String>
                        for (f in followed) {
                            if (f == auth.currentUser.email) {
                                holder.itemView.likeButton.setImageResource(R.drawable.ic_like_full)
                            }
                        }
                    }
                }
            }

        //GET Other story
        holder.itemView.imageViewShowStory.setOnClickListener {
            val emailRef = db.collection("Story")
            emailRef
                .whereEqualTo("uuid", showStoryList[position].uuid)
                .get().addOnSuccessListener {
                    for (email in it) {

                        val profileRef = db.collection("Profile")
                        profileRef.whereEqualTo("email", email["email"].toString())
                            .get().addOnSuccessListener {
                                for (name in it) {
                                    otherUserEmail = name["email"].toString()
                                    navToOtherStory(holder.view.root)
                                }
                            }
                    }
                }
        }

        //LIKE
        holder.view.likeButton.setOnClickListener {
            val dbRef = db.collection("Story")
            dbRef.whereEqualTo("uuid", showStoryList[position].uuid)
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

        //Go to other user profile
        holder.itemView.imageViewShowStory.setOnClickListener {
            val emailRef = db.collection("Story")
            emailRef
                .whereEqualTo("uuid", showStoryList[position].uuid)
                .get().addOnSuccessListener {
                    for (email in it) {

                        val profileRef = db.collection("Profile")
                        profileRef.whereEqualTo("email", email["email"].toString())
                            .get().addOnSuccessListener {
                                for (name in it) {
                                    otherUserEmail = name["email"].toString()
                                }
                            }.addOnCompleteListener {
                                navToOtherStory(holder.view.root)
                            }
                    }
                }
        }


    }

    override fun getItemCount(): Int {
        return showStoryList.size
    }

    fun updateShowList(newShowList: List<FeedData>) {
        //showStoryList.clear()
        showStoryList.addAll(newShowList)
        notifyDataSetChanged()
    }

    private fun navToOtherStory(view: View) {
        if (GLOBAL_CURRENT_FRAGMENT == "show_story") {
            val action =
                ShowStoryFragmentDirections.actionShowStoryFragmentToOtherUserProfileFragment(
                    otherUserEmail
                )
            view.findNavController().navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        } else if (GLOBAL_CURRENT_FRAGMENT == "search_story") {
            val action =
                SearchFragmentDirections.actionSearchFragmentToOtherUserProfileFragment(
                    otherUserEmail
                )
            view.findNavController().navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }

    private fun getComments(postUUID: String?, view: View) {



        val action =
            ShowStoryFragmentDirections.actionShowStoryFragmentToCommentFragment(postUUID!!)
        view.findNavController().navigate(action)
        GLOBAL_CURRENT_FRAGMENT = "comment_story"
    }
}