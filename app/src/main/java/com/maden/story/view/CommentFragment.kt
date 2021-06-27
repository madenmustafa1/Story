package com.maden.story.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.maden.story.R
import com.maden.story.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.story.viewmodel.GetCommentViewModel
import com.maden.story.viewmodel.GoCommentViewModel
import kotlinx.android.synthetic.main.fragment_comment.*


class CommentFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }


    private lateinit var goCommentViewModel: GoCommentViewModel
    private lateinit var getCommentViewModel: GetCommentViewModel

    var commentUUID: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GLOBAL_CURRENT_FRAGMENT = "comment_story"

        arguments?.let {
            commentUUID = CommentFragmentArgs.fromBundle(it).postUUID
        }

        goCommentViewModel = ViewModelProvider(this)
            .get(GoCommentViewModel::class.java)
        getCommentViewModel = ViewModelProvider(this)
            .get(GetCommentViewModel::class.java)


        val callback = requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                val action =
                    CommentFragmentDirections.actionCommentFragmentToShowStoryFragment()
                view.findNavController().navigate(action)
            }

        commentFragmentShareButton.setOnClickListener {
            val comment: String = commentText.text.toString()
            getCommentViewModel.getComment(commentUUID!!)

            if (commentUUID != null && comment != ""){
                goCommentViewModel.goComment(commentUUID!!, comment)
            }
        }
    }

}