package com.maden.story.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.adapter.UserProfileAdapter
import com.maden.story.viewmodel.UserProfileModel
import kotlinx.android.synthetic.main.fragment_user_profile.*


class UserProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    private lateinit var profileModel: UserProfileModel
    private val profileAdapter = UserProfileAdapter(arrayListOf())

    private var db = Firebase.firestore
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileModel = ViewModelProvider(this).get(UserProfileModel::class.java)
        profileModel.getMyUserProfile()

        userProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        userProfileRecyclerView.adapter = profileAdapter





        observeMyProfileData()
    }

    private fun observeMyProfileData(){
        profileModel.profileDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                userProfileNameSurname.text = it[0].userNameSurname
                userProfileFollowedText.text = it[0].followed
                userProfileFollowerText.text = it[0].follower
                userProfileStoryText.text = it[0].storySize
            }
        })

        profileModel.profileAdapterDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                profileAdapter.updateMyProfile(it)
            }
        })
    }
}