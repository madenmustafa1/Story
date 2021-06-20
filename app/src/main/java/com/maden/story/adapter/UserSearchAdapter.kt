package com.maden.story.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.databinding.ItemSearchStoryBinding
import com.maden.story.databinding.ItemSearchUserBinding
import com.maden.story.model.SearchData
import com.maden.story.view.SearchFragmentDirections
import kotlinx.android.synthetic.main.item_search_user.view.*

class UserSearchAdapter(private val userSearchList: ArrayList<SearchData>) :
    RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
    class UserSearchViewHolder(var view: ItemSearchUserBinding)
        : RecyclerView.ViewHolder(view.root) {

    }

    private var db = Firebase.firestore
    private lateinit var otherUserEmail: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_search_user, parent, false)
        val view = DataBindingUtil.inflate<ItemSearchUserBinding>(inflater,
            R.layout.item_search_user,
            parent, false)

        return UserSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {

        holder.view.userSearchData = userSearchList[position]




        holder.itemView.searchUserProfile.setOnClickListener {
            val profileRef = db.collection("Profile")
            profileRef.whereEqualTo("email", userSearchList[position].email)
                .get().addOnSuccessListener {
                    for (name in it) {
                        otherUserEmail = name["email"].toString()
                        nav(holder.view.root)
                    }
                }
        }
    }

    override fun getItemCount(): Int {
        return userSearchList.size
    }

    fun updateUserSearchList(newUserSearchList: List<SearchData>) {
        userSearchList.clear()
        userSearchList.addAll(newUserSearchList)
        notifyDataSetChanged()
    }
    private fun nav(view: View){
        if(GLOBAL_CURRENT_FRAGMENT == "search_story"){
            val action =
                SearchFragmentDirections.actionSearchFragmentToOtherUserProfileFragment(otherUserEmail)
            view.findNavController().navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }
}