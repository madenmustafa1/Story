package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.viewmodel.AddStoryViewModel
import kotlinx.android.synthetic.main.fragment_add_story.*


class AddStoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_story, container, false)
    }

    private lateinit var addViewModel: AddStoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Story"
        GLOBAL_CURRENT_FRAGMENT = "add_story"

        addViewModel = ViewModelProvider(this).get(AddStoryViewModel::class.java)

        addFragmentShareButton.setOnClickListener {
            val title = addFragmentTitleText.text.toString()
            val story = addFragmentStoryText.text.toString()
            addViewModel.share(title, story)

            val action = AddStoryFragmentDirections.actionAddStoryFragmentToShowStoryFragment()
            Navigation.findNavController(view).navigate(action)

        }
    }

}