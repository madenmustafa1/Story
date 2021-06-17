package com.maden.story.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.story.R
import com.maden.story.adapter.UserProfileAdapter
import com.maden.story.viewmodel.UserProfileModel
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.jar.Manifest


class UserProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    private lateinit var profileModel: UserProfileModel
    private val profileAdapter = UserProfileAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = ""

        profileModel = ViewModelProvider(this).get(UserProfileModel::class.java)
        profileModel.getMyUserProfile()

        userProfileRecyclerView.layoutManager = LinearLayoutManager(context)
        userProfileRecyclerView.adapter = profileAdapter

        observeMyProfileData()

        userProfilePhoto.setOnClickListener {
            askForPermissions()
        }

    }

    private fun observeMyProfileData() {
        profileModel.profileDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                userProfileNameSurname.text = it[0].userNameSurname
                userProfileFollowedText.text = it[0].followed
                userProfileFollowerText.text = it[0].follower
                userProfileStoryText.text = it[0].storySize
                (activity as AppCompatActivity)
                    .supportActionBar?.title = "@" + it[0].username
            }
        })

        profileModel.profileAdapterDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                profileAdapter.updateMyProfile(it)
            }
        })
    }

    //####### ####### ####### ####### #######
    //#######  HEPSİ Viewmodel'e geçecek #######
    //####### ####### ####### ####### #######
    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireView().context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            val intentGallery = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intentGallery, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intentGallery = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intentGallery, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var selectedPicture: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    val reference = storage.reference
    private var auth = Firebase.auth
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data
            try {
                if (selectedPicture != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver, selectedPicture!!
                        )
                        var bitmap = ImageDecoder.decodeBitmap(source)
                        userProfilePhoto.setImageBitmap(bitmap)



                        val ref = storage.reference
                        val bit = bitmap
                        val baos = ByteArrayOutputStream()
                        bit.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                        val data = baos.toByteArray()
                        ref.child(auth.currentUser!!.email!!).child("profilePhoto").putBytes(data).addOnSuccessListener {
                            println("Başarılı")
                        }.addOnFailureListener{
                            println(it.localizedMessage)
                        }

                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver, selectedPicture
                        )
                        userProfilePhoto.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}