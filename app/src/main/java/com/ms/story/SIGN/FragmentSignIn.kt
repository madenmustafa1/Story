package com.ms.story.SIGN

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ms.story.STORY.StoryActivity
import com.ms.story.R
import kotlinx.android.synthetic.main.fragment__sign_in.*


class FragmentSignIn : Fragment() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        signUp.setOnClickListener {
            signIn(view)
        }
        signUpText.setOnClickListener {
            intentSignUp()
        }

    }
    fun signIn(view: View){

        val email = storyText.text.toString()
        val password = passwordText.text.toString()

        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
                        intentSignIn()

                    } else {
                        Toast.makeText(context, it.exception?.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    fun intentSignIn(){
        val intent = Intent(context, StoryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }
    fun intentSignUp(){

        val signUp = FragmentSignUp()
        val fragmentManager : FragmentManager? = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.signLayout, signUp).commit()

    }
}