package com.plantquiz.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login_button_login.setOnClickListener {

            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("Login", "Attempt login with email: $email and password: $password")


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {

                if(it.isSuccessful)
                {
                    Log.d("Main", "Successfully Logged in ")
                    Log.d("Main", "createUserWithEmail:success")

                }

                else{
                    Log.w("Main", "createUserWithEmail:failure", it.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }




            }

        }

        not_having_account_signup.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
