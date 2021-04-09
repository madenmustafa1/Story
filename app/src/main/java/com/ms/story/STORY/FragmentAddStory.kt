package com.ms.story.STORY

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.R
import kotlinx.android.synthetic.main.fragment__addstory.*
import java.util.*


class FragmentAddStory : Fragment() {


    private var db = Firebase.firestore
    private var auth = Firebase.auth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__addstory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        GLOBAL_CURRENT_FRAGMENT = "add_story"
        shareButton.setOnClickListener {
            share(it)
        }
    }

    fun share(view: View){
        val uuid = UUID.randomUUID()
        val data = hashMapOf(
                "title" to titleText.text.toString(),
                "story" to storyText.text.toString(),
                "email" to auth.currentUser?.email.toString(),
                "like" to listOf<String>(),
                "comment" to listOf<String>(),
                "date" to Timestamp.now(),
                "uuid" to uuid.toString()
        )

        db.collection("Story")
            .add(data)
            .addOnSuccessListener {
                println("Story")
            }
            .addOnFailureListener{
                println("Fail")
            }

        val fragment2 = FragmentShowStory()
        val fragmentManager: FragmentManager? = fragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.story_Layout, fragment2)
        fragmentTransaction.commit()
        Toast.makeText(activity, "Shared", Toast.LENGTH_LONG).show()

    }
}