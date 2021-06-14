package com.maden.story.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.adapter.SearchAdapter
import com.maden.story.adapter.UserSearchAdapter
import com.maden.story.viewmodel.SearchModel
import com.maden.story.viewmodel.UserSearchModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.item_search_story.*


class SearchFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private lateinit var searchViewModel: SearchModel
    private lateinit var userSearchModel: UserSearchModel

    private val searchAdapter = SearchAdapter(arrayListOf())
    private val userSearchAdapter = UserSearchAdapter(arrayListOf())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GLOBAL_CURRENT_FRAGMENT = "search_story"

        searchViewModel = ViewModelProvider(this).get(SearchModel::class.java)
        searchViewModel.searchStory()

        userSearchModel = ViewModelProvider(this).get(UserSearchModel::class.java)

        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.adapter = searchAdapter

        userSearchRecyclerView.layoutManager = GridLayoutManager(context, 2)
        userSearchRecyclerView.adapter = userSearchAdapter
        userSearch()

        observeUserData()
        observeSearchData()
    }

    fun userSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var searchText : String? = null
                newText?.let {
                    searchText = newText.replace(" ", "").trim()
                }

                if(searchText != null && searchText != "") {
                    searchRecyclerView.visibility = View.GONE
                    userSearchRecyclerView.visibility = View.VISIBLE
                    newText.toString().lowercase()
                    userSearchModel.userSearch(searchText!!)
                } else {
                    userSearchRecyclerView.visibility = View.GONE
                    searchRecyclerView.visibility = View.VISIBLE
                }
                return false
            }
        })
    }

    private fun observeSearchData() {
        //Progressbar eklenecek
        searchViewModel.feedDataClass.observe(viewLifecycleOwner, Observer {
            it?.let { searchAdapter.updateSearchList(it) } }) }

    private fun observeUserData(){
        userSearchModel.searchDataClass.observe(viewLifecycleOwner, Observer {
            it?.let { userSearchAdapter.updateUserSearchList(it) } }) }
}

/*
        userStoryText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_SCROLL -> v!!.parent.requestDisallowInterceptTouchEvent(false);
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
 */