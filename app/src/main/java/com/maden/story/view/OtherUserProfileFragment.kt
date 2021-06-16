package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.story.R
import com.maden.story.adapter.OtherUserProfileAdapter
import com.maden.story.adapter.UserProfileAdapter
import com.maden.story.viewmodel.OtherUserProfileModel
import com.maden.story.viewmodel.UserProfileModel
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.userProfileRecyclerView


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

    private lateinit var otherProfileModel: OtherUserProfileModel
    private val otherProfileAdapter = OtherUserProfileAdapter(arrayListOf())
    private var otherUserEmail: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            otherUserEmail = OtherUserProfileFragmentArgs.fromBundle(it).otherUserEmail
        }

        otherProfileModel = ViewModelProvider(this).get(OtherUserProfileModel::class.java)

        if (otherUserEmail != null && otherUserEmail != ""){
            otherProfileModel.getOtherUserProfile(otherUserEmail!!)
        }

        otherUserProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        otherUserProfileRecyclerView.adapter = otherProfileAdapter

        observeOtherProfileData()

        otherFollowButton.setOnClickListener {
            otherProfileModel.followUser(otherUserEmail!!)

            //Coroutines gelecek!!!!!!!!!!!!!!!!!
            //LifecycleCoroutineScope

            otherProfileModel.uFollowing.value?.let {

                if(otherProfileModel.uFollowing.value == "false"){
                    otherFollowButton.text = "Follow"
                    otherProfileModel.uFollowing.value = "false"
                } else {
                    otherFollowButton.text = "Unfollow"
                    otherProfileModel.uFollowing.value = "true"
                }
            }
        }
    }

    private fun observeOtherProfileData(){
        otherProfileModel.otherProfileDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserProfileNameSurname.text = it[0].userNameSurname
                otherUserProfileFollowedText.text = it[0].followed
                otherUserProfileFollowerText.text = it[0].follower
                otherUserProfileStoryText.text = it[0].storySize


                if (it[0].areUFollowing == "false"){
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
    }
}