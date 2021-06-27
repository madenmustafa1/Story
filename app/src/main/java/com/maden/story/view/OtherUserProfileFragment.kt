package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.adapter.OtherUserProfileAdapter
import com.maden.story.model.DownloadPhotoUrl
import com.maden.story.util.downloadPhoto
import com.maden.story.viewmodel.OtherUserProfileViewModel
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OtherUserProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other_user_profile, container, false)
    }

    private lateinit var otherProfileModel: OtherUserProfileViewModel
    private val otherProfileAdapter = OtherUserProfileAdapter(arrayListOf(), DownloadPhotoUrl(""))
    private var otherUserEmail: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        GLOBAL_CURRENT_FRAGMENT = "other_profile_story"

        arguments?.let {
            otherUserEmail = OtherUserProfileFragmentArgs.fromBundle(it).otherUserEmail
        }

        otherProfileModel = ViewModelProvider(this).get(OtherUserProfileViewModel::class.java)

        if (otherUserEmail != null && otherUserEmail != "") {
            otherProfileModel.getOtherUserProfile(otherUserEmail!!)
        }

        otherUserProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        otherUserProfileRecyclerView.adapter = otherProfileAdapter

        observeOtherProfileData()

        otherFollowButton.setOnClickListener { followControl() }
    }

    private fun observeOtherProfileData() {
        otherProfileModel.otherProfileDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserProfileNameSurname.text = it[0].userNameSurname

                otherUserProfileFollowedText.text = it[0].followed
                otherUserProfileFollowerText.text = it[0].follower
                otherUserProfileStoryText.text = it[0].storySize
                (activity as AppCompatActivity)
                    .supportActionBar?.title = "@" + it[0].username

                if (it[0].areUFollowing == "false") {
                    otherFollowButton.text = "Follow"
                } else {
                    otherFollowButton.text = "Unfollow"
                }
            }
        })

        otherProfileModel.otherProfileAdapterDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherProfileAdapter.updateOtherProfile(it)
            }
        })

        otherProfileModel.otherProfilePhotoDownloadUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserProfilePhoto.downloadPhoto(it)
                otherProfileAdapter.downloadPhoto(DownloadPhotoUrl(it))
            }
        })
    }

    private fun followControl() {
        otherProfileModel.followUser(otherUserEmail!!)
        viewLifecycleOwner.lifecycleScope.launch {
            otherProfileModel.uFollowing.value?.let {
                delay(500)
                if (otherProfileModel.uFollowing.value == "false") {
                    otherFollowButton.text = "Follow"
                    otherProfileModel.uFollowing.value = "false"
                } else {

                    otherFollowButton.text = "Unfollow"
                    otherProfileModel.uFollowing.value = "true"
                }
            }
        }
    }
}