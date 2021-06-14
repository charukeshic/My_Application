package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.adapters.ViewPagerAdapter2
import com.example.myapplication.fragments.ComparePriceFragment
import com.example.myapplication.fragments.ProductFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class StoreProducts2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewPager : ViewPager
    private lateinit var tabs : TabLayout

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    private lateinit var categoryArrayList: ArrayList<Category>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_store_products2)

        tabs = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)



        navigationDrawer()

        setUpTabs()


    }



    private fun setUpTabs() {

        val mall = intent.getStringExtra("MALL").toString()
        val category = intent.getStringExtra("CATEGORY").toString()
        val product = intent.getStringExtra("PRODUCT").toString()

        val adapter = ViewPagerAdapter2(supportFragmentManager)
        adapter.addFragment(ProductFragment(mall,category,product), "Product")
        adapter.addFragment(ComparePriceFragment(), "Compare Price")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)



    }

    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

        menuIcon.setOnClickListener(View.OnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else
                drawerLayout.openDrawer(GravityCompat.START)
        })

    }

    override fun onBackPressed() {


        /*------------Navigation Drawer Menu--------------*/
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)

        }
        else {
            super.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this@StoreProducts2, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@StoreProducts2, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@StoreProducts2, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@StoreProducts2, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@StoreProducts2, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@StoreProducts2, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@StoreProducts2, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@StoreProducts2, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@StoreProducts2, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




}