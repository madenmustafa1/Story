package com.maden.story.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.activity.StoryActivity
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private var gender : String? = null
    private var name : String? = null
    private var surname : String? = null
    private var email : String? = null
    private var password : String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        maleBox.setOnClickListener { if(femaleBox.isChecked){ femaleBox.isChecked = false } }
        femaleBox.setOnClickListener { if(maleBox.isChecked){ maleBox.isChecked = false } }


        signInText.setOnClickListener { intentSignIn() }
        signUpButton.setOnClickListener { signUpF(it)  }

    }

    fun intentSignIn(){
        val action = SignUpFragmentDirections.actionFragmentSignUpToSignInFragment()
        Navigation.findNavController(requireActivity(), R.id.loginContainer).navigate(action)
    }

    fun signUpF(view: View){
        name = nameTextSignUp.text.toString()
        surname = surnameTextSignUp.text.toString()
        username = usernameTextSignUp.text.toString()
        email =  emailTextSignUp.text.toString()
        password = passwordTextSignUp.text.toString()

        gender = when {
            maleBox.isChecked -> { "male" }
            femaleBox.isChecked -> { "female" }
            else -> { null }
        }

        if( name != null && surname != null &&
            email != null && password != null &&
            gender != null && username != null ){

            auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        auth.currentUser!!.sendEmailVerification()
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    Toast.makeText(context,
                                        "Mailinize gelen aktivasyonu onaylayın",
                                        Toast.LENGTH_LONG).show()

                                    profileData()
                                    intentSignIn()
                                } else {
                                    Toast.makeText(context,
                                        it.exception!!.message!!, Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, it.exception!!.message!!, Toast.LENGTH_LONG).show()
                    }
                }
        }
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
            "username" to username,
            "email" to email,
            "gender" to gender,
            "user" to "user",
            "followed" to listOf<String>()
        )

        //intentLogin()
        db.collection("Profile").document(email!!)
            .set(profile)
            .addOnSuccessListener {  }
            .addOnFailureListener{ println("Fail$it") }
    }
}