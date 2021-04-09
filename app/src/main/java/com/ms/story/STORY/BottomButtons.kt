package com.ms.story.STORY

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ms.story.PROFILE.OtherUserProfileDirections
import com.ms.story.PROFILE.UserProfileDirections
import com.ms.story.R
import com.ms.story.SEARCH.SearchFragmentDirections
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*


class BottomButtons : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homeButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "show_story"){
                if(GLOBAL_CURRENT_FRAGMENT == "search_story") {
                    val action = SearchFragmentDirections.actionSearchFragmentToFragmentShowStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "show_story"
                } else if (GLOBAL_CURRENT_FRAGMENT == "add_story"){
                    val action = FragmentAddStoryDirections.actionFragmentAddStoryToFragmentShowStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "show_story"
                } else if (GLOBAL_CURRENT_FRAGMENT == "profile_story"){
                    val action = UserProfileDirections.actionUserProfileToFragmentShowStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "show_story"
                } else if (GLOBAL_CURRENT_FRAGMENT == "other_profile_story"){
                    val action = OtherUserProfileDirections.actionOtherUserProfileToFragmentShowStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "show_story"
                }
            }
        }
        searchButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "search_story"){
                if(GLOBAL_CURRENT_FRAGMENT == "show_story") {
                    val action = FragmentShowStoryDirections.actionFragmentShowStoryToSearchFragment()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "search_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "add_story"){
                    val action = FragmentAddStoryDirections.actionFragmentAddStoryToSearchFragment()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "search_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "profile_story"){
                    val action = UserProfileDirections.actionUserProfileToSearchFragment2()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "search_story"
                } else if (GLOBAL_CURRENT_FRAGMENT == "other_profile_story"){
                    val action = OtherUserProfileDirections.actionOtherUserProfileToSearchFragment()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "search_story"
                }
            }
        }
        addButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "add_story"){
                if(GLOBAL_CURRENT_FRAGMENT == "show_story") {
                    val action = FragmentShowStoryDirections.actionFragmentShowStoryToFragmentAddStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "add_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "search_story"){
                    val action = SearchFragmentDirections.actionSearchFragmentToFragmentAddStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "add_story"

                }else if (GLOBAL_CURRENT_FRAGMENT == "profile_story"){
                    val action = UserProfileDirections.actionUserProfileToFragmentAddStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "add_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "other_profile_story"){
                    val action = OtherUserProfileDirections.actionOtherUserProfileToFragmentAddStory()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "add_story"
                }
            }
        }
        userProfileButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "profile_story"){
                if(GLOBAL_CURRENT_FRAGMENT == "show_story") {
                    val action = FragmentShowStoryDirections.actionFragmentShowStoryToUserProfile()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "profile_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "search_story"){
                    val action = SearchFragmentDirections.actionSearchFragmentToUserProfile2()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "profile_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "add_story"){
                    val action = FragmentAddStoryDirections.actionFragmentAddStoryToUserProfile()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "profile_story"

                } else if (GLOBAL_CURRENT_FRAGMENT == "other_profile_story"){
                    val action = OtherUserProfileDirections.actionOtherUserProfileToUserProfile()
                    Navigation.findNavController(requireActivity(), R.id.story_Layout).navigate(action)
                    GLOBAL_CURRENT_FRAGMENT = "profile_story"
                }
            }
        }
    }
}