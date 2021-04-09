package com.ms.story.STORY

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.story.R
import com.ms.story.SIGN.SignInActivity

var GLOBAL_CURRENT_FRAGMENT: String? = null
var GLOBAL_CONTEXT: Activity? = null

class StoryActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        GLOBAL_CURRENT_FRAGMENT = "show_story"

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        fragment()

        GLOBAL_CONTEXT = this

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun fragment(){
        val fragmentBottoms = supportFragmentManager
        val fragmentTransaction = fragmentBottoms.beginTransaction()
        val bottomButtons = BottomButtons()
        fragmentTransaction.replace(R.id.bottom_Buttons_Layout, bottomButtons).commit()






/*
        val fragmentShowStory = supportFragmentManager
        val fragmentTransactionStory = fragmentShowStory.beginTransaction()
        val story = FragmentShowStory()
        fragmentTransactionStory.replace(R.id.story_Layout, story).commit()
 */

    }
}