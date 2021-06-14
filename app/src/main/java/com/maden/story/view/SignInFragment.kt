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
import com.google.firebase.ktx.Firebase
import com.maden.story.R
import com.maden.story.activity.StoryActivity
import kotlinx.android.synthetic.main.fragment_sign_in_.*


class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_in_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        signUpText.setOnClickListener { intentSignUp() }
        signInButton.setOnClickListener { signIn(view) }
    }

    fun intentSignUp(){
        val action = SignInFragmentDirections.actionSignInFragmentToFragmentSignUp()
        Navigation.findNavController(requireActivity(), R.id.loginContainer).navigate(action)
    }

    private var email: String? = null
    private var password: String? = null
    fun signIn(view: View){

        email = emailTextSignIn.text.toString()
        password = passwordTextSignIn.text.toString()


        if (email != null && password != null) {

            auth.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(auth.currentUser!!.isEmailVerified){
                            intentSignIn()
                        } else {
                            Toast.makeText(context,
                                "Mailinize gelen aktivasyonu onaylayın",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context,
                            it.exception?.localizedMessage.toString(),
                            Toast.LENGTH_LONG).show()
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

}