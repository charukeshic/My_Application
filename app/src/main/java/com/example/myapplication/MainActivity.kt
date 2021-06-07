package com.example.myapplication


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout



class MainActivity : AppCompatActivity() {
    //override fun onCreate(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)


   // }

    lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager
    var v = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

    }

}