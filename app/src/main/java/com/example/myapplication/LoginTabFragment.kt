package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginTabFragment : Fragment() {
    lateinit var email: EditText
    lateinit var pass: EditText
    lateinit var forgetPass: TextView
    lateinit var login: Button

    var v = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.login_tab_fragment, container, false) as ViewGroup

        email = root.findViewById(R.id.email)
        pass = root.findViewById(R.id.pass)
        forgetPass = root.findViewById(R.id.forgetPass)
        login = root.findViewById(R.id.loginButton)

        email.translationX = 800f
        pass.translationX = 800f
        forgetPass.translationX = 800f
        login.translationX = 800f

        email.alpha = v
        pass.alpha = v
        forgetPass.alpha = v
        login.alpha = v

        email.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        pass.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        forgetPass.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        login.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()

        login.setOnClickListener{
            val emailAddr = email.text.toString()
            val passWord = pass.text.toString()

            if (emailAddr.isEmpty() || passWord.isEmpty()) {
                Toast.makeText(this@LoginTabFragment.context, "Please fill up required details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailAddr,passWord)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    val intent = Intent(getActivity(), Homepage::class.java)
                    intent.putExtra("userId", it.result?.user?.uid.toString())
                    Log.d("Login", "User uid: ${it.result?.user?.uid}")
                    getActivity()?.startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this@LoginTabFragment.context, "Invalid combination of email & password", Toast.LENGTH_SHORT).show()
                }

        }


        forgetPass.setOnClickListener {
            val intent = Intent(getActivity(), ForgotPasswordActivity::class.java)
            getActivity()?.startActivity(intent)

        }


        return root
    }


}