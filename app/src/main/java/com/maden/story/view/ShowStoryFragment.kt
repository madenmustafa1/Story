package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.adapter.ShowStoryAdapter
import com.maden.story.viewmodel.ShowStoryModel
import kotlinx.android.synthetic.main.fragment_show_story.*


class ShowStoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_story, container, false)
    }


    private lateinit var showViewModel: ShowStoryModel
    private val showAdapter = ShowStoryAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "show_story"

        showViewModel = ViewModelProvider(this).get(ShowStoryModel::class.java)
        showViewModel.getStory()

        showStoryRecyclerView.layoutManager = LinearLayoutManager(context)
        showStoryRecyclerView.adapter = showAdapter

        observeData()
    }

    private fun observeData() {
        //Progressbar eklenecek
        showViewModel.feedDataClass.observe(viewLifecycleOwner, Observer {

            it?.let {
                showAdapter.updateShowList(it)
                println(it)
            }
        })
    }
}