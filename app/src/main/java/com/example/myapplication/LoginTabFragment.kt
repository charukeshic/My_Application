package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment


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
            val intent = Intent(getActivity(), Homepage::class.java)
            getActivity()?.startActivity(intent)
        }

        return root
    }
}