package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.maden.story.R
import com.maden.story.viewmodel.AddStoryModel
import com.maden.story.viewmodel.ShowStoryModel
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

    private lateinit var addViewModel: AddStoryModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewModel = ViewModelProvider(this).get(AddStoryModel::class.java)

        addFragmentShareButton.setOnClickListener {
            val title = addFragmentTitleText.text.toString()
            val story = addFragmentStoryText.text.toString()
            addViewModel.share(title, story)
        }
    }
}