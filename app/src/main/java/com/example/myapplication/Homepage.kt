package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class Homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var storeNavigation : Button
    lateinit var onlineShopping : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_homepage)


        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.my_toolbar)

        storeNavigation = findViewById(R.id.store_navigation)
        onlineShopping = findViewById(R.id.online_shopping)

        /*------------Toolbar--------------*/
        setSupportActionBar(toolbar)

        /*------------Navigation Drawer Menu--------------*/
        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        storeNavigation.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this@Homepage, StoreNavigation::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        })

        onlineShopping.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this@Homepage, OnlineShopping::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        })


    }


    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)

        }
        else{
            super.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@Homepage, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@Homepage, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@Homepage, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}