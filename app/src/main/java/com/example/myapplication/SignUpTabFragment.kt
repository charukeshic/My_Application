package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignUpTabFragment : Fragment() {

    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var pass: EditText
    lateinit var username : EditText
    lateinit var signUp: Button

    var v = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.signup_tab_fragment, container, false) as ViewGroup

        email = root.findViewById(R.id.email1)
        mobile = root.findViewById(R.id.mobile1)
        pass = root.findViewById(R.id.pass1)
        username = root.findViewById(R.id.confirm_pass1)
        signUp = root.findViewById(R.id.signUpButton)

        email.translationX = 800f
        mobile.translationX = 800f
        pass.translationX = 800f
        username.translationX = 800f
        signUp.translationX = 800f

        email.alpha = v
        mobile.alpha = v
        pass.alpha = v
        username.alpha = v
        signUp.alpha = v

        email.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        pass.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        mobile.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        username.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        signUp.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()

        signUp.setOnClickListener{
            performRegistration()
        }

        return root

    }


    private fun performRegistration () {
        val emailAddr = email.text.toString()
        val passWord = pass.text.toString()
        val mobileNo = mobile.text.toString()
        val userName = username.text.toString()

        if (emailAddr.isEmpty() || passWord.isEmpty() || mobileNo.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this@SignUpTabFragment.context, "Please fill up required details", Toast.LENGTH_SHORT).show()
            return
        }

        //FirebaseAuthentication to create user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailAddr, passWord)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Succesful uid: ${it.result?.user?.uid}")
                saveUserToDatabase(it.result?.user?.uid.toString())



            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this@SignUpTabFragment.context, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveUserToDatabase(uid : String) {

        val emailAddr = email.text.toString()
        val passWord = pass.text.toString()
        val mobileNo = mobile.text.toString()
        val userName = username.text.toString()

        Log.d("Main", "email: $emailAddr")
        Log.d("Main", "pass: $passWord")
        Log.d("Main", "mob: $mobileNo")
        Log.d("Main", "username: $userName")

        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val address = "Not Updated".toString()
        val image = "Not Updated".toString()

        val user = User(emailAddr, userName, passWord, mobileNo, address, image)

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@SignUpTabFragment.context, "Account created succesfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpTabFragment.context, ProfileActivity::class.java)
                intent.putExtra("userId", uid)
                startActivity(intent)
            }

    }

}