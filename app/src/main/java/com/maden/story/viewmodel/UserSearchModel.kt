package com.maden.story.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.model.SearchData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserSearchModel : ViewModel() {

    private var db = Firebase.firestore

    private val searchList = arrayListOf<SearchData>()
    val searchDataClass = MutableLiveData<List<SearchData>>()

    fun userSearch(text: String) {
        viewModelScope.launch {
            delay(500)
            searchList.clear()

            val dbRef = db.collection("Profile")
            dbRef
                .orderBy("username")
                .startAt(text)
                .endAt("$text\uf8ff")
                .get()
                .addOnSuccessListener {
                    searchList.clear()

                    for (document in it) {

                        val followed = document["followed"] as List<*>
                        val nameSurname = document["name"].toString() +
                                " " + document["surname"].toString()
                        val email = document["email"].toString()

                        val searchData = SearchData(
                            userName = nameSurname,
                            followers = followed.size.toString(),
                            email = email
                        )

                        searchList.add(searchData)
                        //val searchList = arrayListOf<SearchData>(searchData)
                        searchDataClass.value = searchList
                    }
                }
        }
    }
}
