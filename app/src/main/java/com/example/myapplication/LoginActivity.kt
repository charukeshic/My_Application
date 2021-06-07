package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

//class LoginActivity : AppCompatActivity() {

    //override fun onCreate(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

    //}

//}

class LoginActivity : AppCompatActivity() {

    lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager

    var v = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Up"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = LoginAdapter(supportFragmentManager, this,tabLayout.tabCount)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.translationY = 300f

        tabLayout.alpha = v

        tabLayout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(100).start()




    }
}