package com.maden.story.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.maden.story.R
import com.maden.story.view.*
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*



class BottomButtons : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//"comment_story"
        homeButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "show_story"){
                when (GLOBAL_CURRENT_FRAGMENT) {
                    "search_story" -> {
                        val action = SearchFragmentDirections.actionSearchFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "add_story" -> {
                        val action = AddStoryFragmentDirections.actionAddStoryFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "profile_story" -> {
                        val action = UserProfileFragmentDirections.actionUserProfileFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "other_profile_story" -> {
                        val action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "comment_story" -> {
                        val action = CommentFragmentDirections.actionCommentFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                }
            }
        }
        searchButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "search_story"){
                when (GLOBAL_CURRENT_FRAGMENT) {
                    "show_story" -> {
                        val action = ShowStoryFragmentDirections.actionShowStoryFragmentToSearchFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "search_story"

                    }
                    "add_story" -> {
                        val action = AddStoryFragmentDirections.actionAddStoryFragmentToSearchFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "search_story"

                    }
                    "profile_story" -> {
                        val action = UserProfileFragmentDirections.actionUserProfileFragmentToSearchFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "search_story"
                    }
                    "other_profile_story" -> {
                        val action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToSearchFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "search_story"
                    }
                    "comment_story" -> {
                        val action = CommentFragmentDirections.actionCommentFragmentToSearchFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "search_story"
                    }
                }
            }
        }
        addButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "add_story"){
                when (GLOBAL_CURRENT_FRAGMENT) {
                    "show_story" -> {
                        val action = ShowStoryFragmentDirections.actionShowStoryFragmentToAddStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "add_story"

                    }
                    "search_story" -> {
                        val action = SearchFragmentDirections.actionSearchFragmentToAddStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "add_story"

                    }
                    "profile_story" -> {
                        val action = UserProfileFragmentDirections.actionUserProfileFragmentToAddStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "add_story"

                    }
                    "other_profile_story" -> {
                        val action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToAddStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "add_story"
                    }
                    "comment_story" -> {
                        val action = CommentFragmentDirections.actionCommentFragmentToAddStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "add_story"
                    }
                }
            }
        }
        userProfileButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "profile_story"){
                when (GLOBAL_CURRENT_FRAGMENT) {
                    "show_story" -> {
                        val action = ShowStoryFragmentDirections.actionShowStoryFragmentToUserProfileFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "profile_story"

                    }
                    "search_story" -> {
                        val action = SearchFragmentDirections.actionSearchFragmentToUserProfileFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "profile_story"

                    }
                    "add_story" -> {
                        val action = AddStoryFragmentDirections.actionAddStoryFragmentToUserProfileFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "profile_story"

                    }
                    "other_profile_story" -> {
                        val action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToUserProfileFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "profile_story"
                    }
                    "comment_story" -> {
                        val action = CommentFragmentDirections.actionCommentFragmentToUserProfileFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "profile_story"
                    }
                }
            }
        }
    }
}