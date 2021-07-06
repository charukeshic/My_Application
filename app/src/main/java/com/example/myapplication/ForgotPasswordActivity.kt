package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {


    lateinit var submitButton : Button
    lateinit var email : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        submitButton = findViewById(R.id.btn_submit)
        email = findViewById(R.id.email)

        submitButton.setOnClickListener {

            val userEmail : String = email.text.toString().trim { it <= ' ' }

            if(userEmail.isEmpty()) {

                Toast.makeText(this@ForgotPasswordActivity, "Please enter email address", Toast.LENGTH_SHORT).show()
            }
            else {

                FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this@ForgotPasswordActivity, "Email sent successfully to reset your password", Toast.LENGTH_LONG).show()
                            finish()

                        }
                        else {
                            Toast.makeText(this@ForgotPasswordActivity, task.exception!!.message, Toast.LENGTH_LONG).show()
                        }

                    }

            }


        }



    }

}