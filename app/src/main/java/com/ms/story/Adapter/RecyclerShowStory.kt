package com.ms.story.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.PROFILE.UserProfileDirections
import com.ms.story.R
import com.ms.story.SEARCH.SearchFragmentDirections
import com.ms.story.STORY.FragmentAddStoryDirections
import com.ms.story.STORY.FragmentShowStoryDirections
import com.ms.story.STORY.GLOBAL_CONTEXT
import com.ms.story.STORY.GLOBAL_CURRENT_FRAGMENT
import kotlinx.android.synthetic.main.item_story.view.*


class RecyclerShowStory
(
        val userName: ArrayList<String>,
        val nameTitleText: ArrayList<String>,
        val userStoryText: ArrayList<String>,
        val userLikeText: ArrayList<String>,
        val userUUIDText: ArrayList<String>,
        val userProfilePhoto: ArrayList<String>,
)

    : RecyclerView.Adapter<RecyclerShowStory.StoryHolder>() {
    private var db = Firebase.firestore
    private var auth = Firebase.auth

    class StoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
    private lateinit var otherUserEmail: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_story, parent, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        return StoryHolder(itemView = view)
    }


    override fun onBindViewHolder(holder: StoryHolder, position: Int) {
        holder.itemView.usernameText.text = userName[position]
        holder.itemView.userTitleText.text = nameTitleText[position]
        holder.itemView.userStoryText.setText(userStoryText[position])
        holder.itemView.userLikeText.text = userLikeText[position]

        //Picasso.get().load(userProfilePhoto[position]).into(holder.itemView.imageViewShowStory)




        val dbRef = db.collection("Story")

        dbRef
                .whereEqualTo("uuid", userUUIDText[position])
                .get().addOnSuccessListener {
                    for (i in it){
                        val newRef = db.collection("Story").document(i.id)
                        newRef.get().addOnSuccessListener {
                            val str = it["like"].toString()
                            var arr = str.dropWhile { !it.isLetter() }
                            arr = arr.dropLastWhile { !it.isLetter() }
                            var followed = arr.split(",", " ")

                            for (f in followed){
                                if(f == auth.currentUser.email){
                                    holder.itemView.likeButton.setImageResource(R.drawable.ic_like_full)
                                }
                            }
                        }
                    }
                }

        holder.itemView.imageViewShowStory.setOnClickListener {
            val emailRef = db.collection("Story")

            emailRef
                .whereEqualTo("uuid", userUUIDText[position])
                .get().addOnSuccessListener {
                    for (email in it){
                        //println(email["email"].toString())
                        val profileRef = db.collection("Profile")
                        profileRef.whereEqualTo("email", email["email"].toString())
                            .get().addOnSuccessListener {
                                for (name in it){
                                    //println(name["name"].toString() + " " + name["surname"].toString())
                                    println(name["email"].toString())
                                    otherUserEmail = name["email"].toString()
                                    nav()
                                }
                            }
                    }
                }
        }

        holder.itemView.likeButton.setOnClickListener {
            val dbRef = db.collection("Story")

            dbRef.whereEqualTo("uuid", userUUIDText[position])
                    .get().addOnSuccessListener {
                for (i in it){
                    val str = i["like"].toString()
                    var arr = str.dropWhile { !it.isLetter() }
                    arr = arr.dropLastWhile { !it.isLetter() }
                    var followed = arr.split(",", " ")
                    for (f in followed){
                        if(f == auth.currentUser.email){
                            val newRef = db.collection("Story").document(i.id)
                            newRef.update("like", FieldValue.arrayRemove(auth.currentUser.email))
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
    }

    override fun getItemCount(): Int {
        return nameTitleText.size
    }

    private fun nav(){

        if(GLOBAL_CURRENT_FRAGMENT == "show_story"){

            val action =
                FragmentShowStoryDirections.actionFragmentShowStoryToOtherUserProfile(otherUserEmail)
            Navigation.findNavController(GLOBAL_CONTEXT!! , R.id.story_Layout).navigate(action)

            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"

        } else if(GLOBAL_CURRENT_FRAGMENT == "search_story"){
            val action = SearchFragmentDirections.actionSearchFragmentToOtherUserProfile(otherUserEmail)
            Navigation.findNavController(GLOBAL_CONTEXT!! , R.id.story_Layout).navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"

        }else if(GLOBAL_CURRENT_FRAGMENT == "add_story"){
            val action =
                FragmentAddStoryDirections.actionFragmentAddStoryToOtherUserProfile(otherUserEmail)
            Navigation.findNavController(GLOBAL_CONTEXT!! , R.id.story_Layout).navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"

        }else if(GLOBAL_CURRENT_FRAGMENT == "profile_story"){
            var action = UserProfileDirections.actionUserProfileToOtherUserProfile(otherUserEmail)
            Navigation.findNavController(GLOBAL_CONTEXT!! , R.id.story_Layout).navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }


}
