package com.maden.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maden.story.R
import com.maden.story.databinding.ItemProfileStoryBinding
import com.maden.story.model.DownloadPhotoUrl
import com.maden.story.model.ProfileAdapterData

class UserProfileAdapter(private val profileStoryList: ArrayList<ProfileAdapterData>,
                         private var downloadUrl: DownloadPhotoUrl)
    : RecyclerView.Adapter<UserProfileAdapter.ProfileViewHolder>(){


    class ProfileViewHolder(val view: ItemProfileStoryBinding):
        RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemProfileStoryBinding>(inflater,
            R.layout.item_profile_story,
            parent, false)
        return ProfileViewHolder(view)

    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        //holder.view.
        holder.view.profileData = profileStoryList[position]
        holder.view.profilePhoto = downloadUrl
    }

    override fun getItemCount(): Int {
        return profileStoryList.size
    }

    fun updateMyProfile(newList: List<ProfileAdapterData>){
        profileStoryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun downloadPhoto(url: DownloadPhotoUrl){
        downloadUrl = url
        notifyDataSetChanged()
    }
}