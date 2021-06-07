package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SignUpTabFragment : Fragment() {

    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var pass: EditText
    lateinit var confirmPass: EditText
    lateinit var signUp: Button

    var v = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.signup_tab_fragment, container, false) as ViewGroup

        email = root.findViewById(R.id.email1)
        mobile = root.findViewById(R.id.mobile1)
        pass = root.findViewById(R.id.pass1)
        confirmPass = root.findViewById(R.id.confirm_pass1)
        signUp = root.findViewById(R.id.signUpButton)

        email.translationX = 800f
        mobile.translationX = 800f
        pass.translationX = 800f
        confirmPass.translationX = 800f
        signUp.translationX = 800f

        email.alpha = v
        mobile.alpha = v
        pass.alpha = v
        confirmPass.alpha = v
        signUp.alpha = v

        email.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        pass.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        mobile.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        confirmPass.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        signUp.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()

        signUp.setOnClickListener{
            val intent = Intent(getActivity(), Homepage::class.java)
            getActivity()?.startActivity(intent)
        }

        return root

    }

}