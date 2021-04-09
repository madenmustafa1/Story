package com.ms.story.SIGN

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.STORY.StoryActivity
import com.ms.story.R
import kotlinx.android.synthetic.main.fragment__sign_up.*


class FragmentSignUp : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private var gender : String? = null
    private var name : String? = null
    private var surname : String? = null
    private var email : String? = null
    private var password : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()


        signUpText.setOnClickListener {
            intentSignIn()
        }

        signUp.setOnClickListener {
            signUp(it)
        }

        maleBox.setOnClickListener {

            if(femaleBox.isChecked){
                femaleBox.isChecked = false
            }
        }
        femaleBox.setOnClickListener {
            if(maleBox.isChecked){
                maleBox.isChecked = false
            }
        }
    }

    fun signUp(view: View){
        name = nameText.text.toString()
        surname = surnameText.text.toString()
        email =  storyText.text.toString()
        password = passwordText.text.toString()

        gender = when {
            maleBox.isChecked -> { "male" }
            femaleBox.isChecked -> { "female" }
            else -> { null }
        }

        if(name != null && surname != null && email != null && password != null && gender != null){

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){

                    profileData()
                    intentLogin()
                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun intentSignIn(){

        val signIn = FragmentSignIn()
        val fragmentManager : FragmentManager? = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.signLayout, signIn).commit()

    }
    fun intentLogin(){
        val intent = Intent(context, StoryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }
    fun profileData(){

        val profile = hashMapOf(
                "name" to name,
                "surname" to surname,
                "email" to email,
                "gender" to gender
        )

        db.collection("Profile").document(email!!)
                .set(profile)
                .addOnSuccessListener { println("Welcome to Story") }
                .addOnFailureListener{ println("Fail$it") }

    }


}