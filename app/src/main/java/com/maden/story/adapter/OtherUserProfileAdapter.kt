package com.maden.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maden.story.R
import com.maden.story.databinding.ItemOtherProfileStoryBinding
import com.maden.story.model.DownloadPhotoUrl
import com.maden.story.model.OtherProfileAdapterData



class OtherUserProfileAdapter (private val otherProfileStoryList: ArrayList<OtherProfileAdapterData>,
                               private var downloadUrl: DownloadPhotoUrl)
: RecyclerView.Adapter<OtherUserProfileAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(val view: ItemOtherProfileStoryBinding):
        RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemOtherProfileStoryBinding>(inflater,
            R.layout.item_other_profile_story,
            parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.view.otherProfileData = otherProfileStoryList[position]
        holder.view.otherProfilePhoto = downloadUrl
    }

    override fun getItemCount(): Int {
        return otherProfileStoryList.size
    }

    fun updateOtherProfile(newList: List<OtherProfileAdapterData>){
        otherProfileStoryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun downloadPhoto(url: DownloadPhotoUrl){
        downloadUrl = url
        notifyDataSetChanged()
    }
}