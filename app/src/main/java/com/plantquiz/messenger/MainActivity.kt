package com.plantquiz.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_textview.setOnClickListener {

            Log.d("MainActivity", "Try to show login Activity")

            //lanch login Activity

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {

            Log.d("Main", "Try to show photo")
            //To Open Gallery Intent
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

  var selectPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null ){

            //proceed and check what the selected image was...

            Log.d("Main", "Photo was selected ")

            val selectPhotoUri:Uri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)

            val bitmapdrawable = BitmapDrawable(bitmap)
            selectphoto_button_register.setBackgroundDrawable(bitmapdrawable)


        }
    }

    fun performRegister() {

        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()
        //val username = username_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "Please enter text in Email & Password", Toast.LENGTH_SHORT).show()
            // return@addOnCompleteListner

        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password is:  $password")

        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

              if(!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "successfully created user with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()

                }
                .addOnFailureListener {

                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to Create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }



    }


     fun uploadImageToFirebaseStorage(){

         if(selectPhotoUri == null) return

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectPhotoUri!!)
                .addOnSuccessListener {

                    Log.d("Main", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {

                        it.toString()
                        Log.d("Main", "File Loacation: $it")
                    }
                }




    }
}
