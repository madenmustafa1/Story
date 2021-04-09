package com.ms.story.SIGN

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ms.story.STORY.StoryActivity
import com.ms.story.R

class SignInActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        val fragment = supportFragmentManager
        val fragmentTransaction = fragment.beginTransaction()
        val signIn = FragmentSignIn()
        fragmentTransaction.replace(R.id.signLayout, signIn).commit()


        auth = Firebase.auth
        val currentUser = auth.currentUser

        supportActionBar?.hide()


        if (currentUser != null) {

            val intent = Intent(applicationContext, StoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
