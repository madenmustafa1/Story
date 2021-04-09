package com.ms.story.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.R
import com.ms.story.SEARCH.SearchFragmentDirections
import com.ms.story.STORY.GLOBAL_CONTEXT
import com.ms.story.STORY.GLOBAL_CURRENT_FRAGMENT
import kotlinx.android.synthetic.main.item_search_user.view.*


class RecyclerSearch
    ( val userName: ArrayList<String>,
      val followers: ArrayList<String>,
      val email: ArrayList<String>)
    : RecyclerView.Adapter<RecyclerSearch.Searcholder>() {
    class Searcholder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Searcholder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search_user, parent, false)
        return Searcholder(view)
    }

    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var otherUserEmail: String
    override fun onBindViewHolder(holder: Searcholder, position: Int) {
        holder.itemView.searchNameSurname.text = userName[position]
        holder.itemView.searchFollowers.text = followers[position]

        holder.itemView.searchUserProfile.setOnClickListener {
            println(email[position])

            val profileRef = db.collection("Profile")
            profileRef.whereEqualTo("email", email[position])
                    .get().addOnSuccessListener {
                        for (name in it){

                            println(name["email"].toString())
                            otherUserEmail = name["email"].toString()
                            nav()
                        }
                    }
        }

    }
    private fun nav(){
        if(GLOBAL_CURRENT_FRAGMENT == "search_story"){
            val action =
                SearchFragmentDirections.actionSearchFragmentToOtherUserProfile(otherUserEmail)
            Navigation.findNavController(GLOBAL_CONTEXT!! , R.id.story_Layout).navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }

    override fun getItemCount(): Int {
        return userName.size
    }
}