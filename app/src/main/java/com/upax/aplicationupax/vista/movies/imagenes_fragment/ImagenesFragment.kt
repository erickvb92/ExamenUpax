package com.upax.aplicationupax.vista.movies.imagenes_fragment

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.upax.aplicationupax.R
import com.upax.aplicationupax.vista.movies.base.BaseFragment
import kotlinx.android.synthetic.main.imagenes_fragment.*
import java.io.File
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.storage.UploadTask

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult











class ImagenesFragment : BaseFragment() {

    private val pickImage = 100
    private var imageUri: Uri? = null
    companion object {
        fun newInstance() = ImagenesFragment()
    }

    private lateinit var viewModel: imagenesViewModel
    private lateinit var viewFragment: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.imagenes_fragment, container, false)


        return viewFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[imagenesViewModel::class.java]

        setObservers()

        button_chose_file.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            try {
                imageUri = data?.data
                imageView.setImageURI(imageUri)
                editFilePath.setText(imageUri!!.path + "")

               /* val mAuth = FirebaseAuth.getInstance()
                val user = mAuth.currentUser
                if (user != null) {
                    // do your stuff
                } else {
                    mAuth.signInAnonymously().addOnSuccessListener(this, OnSuccessListener<AuthResult?> {
                        // do your stuff
                    })
                        .addOnFailureListener(this,
                            OnFailureListener { exception ->
                                Log.e(
                                    TAG,
                                    "signInAnonymously:FAILURE",
                                    exception
                                )
                            })
                }*/

                var path = getRealPathFromURI(imageUri!!)
                var storageRef = FirebaseStorage.getInstance().getReference()

                val file = Uri.fromFile(File(path))

                val riversRef: StorageReference = storageRef.child("images/"+imageUri!!.lastPathSegment)

                riversRef.putFile(file)
                    .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                        Toast.makeText(
                            activity,
                            "Imagen subida con exito a FirebaseStorage ",
                            Toast.LENGTH_SHORT
                        ).show()
                    // val downloadUrl: Uri = taskSnapshot.getDownloadUrl()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            activity,
                            "error",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Handle unsuccessful uploads
                        // ...
                    }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setObservers(){

    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireActivity(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }
}