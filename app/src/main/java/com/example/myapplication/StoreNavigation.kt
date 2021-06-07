package com.example.myapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.collections.ArrayList

class StoreNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    private lateinit var dbref: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var storeArrayList: ArrayList<Store>
    private lateinit var tempArrayList: ArrayList<Store>

    lateinit var search_toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_store_navigation)

        search_toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(findViewById(R.id.my_toolbar))


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        storeArrayList = arrayListOf<Store>()
        tempArrayList = arrayListOf<Store>()


        var store1 = Store("Grocery 1", "Promotion : buy 1 free 1", R.drawable.navbar_bg, "Welcome to visit us! 1")
        var store2 = Store("Grocery 2", "Promotion : buy 1 free 1", R.drawable.navbar_bg, "Welcome to visit us! 2")
        var store3 = Store("Grocery 3", "Promotion : buy 1 free 1", R.drawable.navbar_bg, "Welcome to visit us! 3")
        var store4 = Store("Grocery 4", "Promotion : buy 1 free 1", R.drawable.navbar_bg, "Welcome to visit us! 4")
        var store5 = Store("Grocery 5", "Promotion : buy 1 free 1", R.drawable.navbar_bg, "Welcome to visit us! 5")

        storeArrayList.add(store1)
        storeArrayList.add(store2)
        storeArrayList.add(store3)
        storeArrayList.add(store4)
        storeArrayList.add(store5)

        tempArrayList.addAll(storeArrayList)


        val adapter = RecyclerViewAdapter(tempArrayList)

        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(this@StoreNavigation, StoreDescription::class.java)
                intent.putExtra("title", storeArrayList[position].itemTitle)
                intent.putExtra("image", storeArrayList[position].itemImage)
                intent.putExtra("details", storeArrayList[position].itemDetails)
                intent.putExtra("description", storeArrayList[position].itemDescription)
                startActivity(intent)

            }


        })


        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)



        navigationDrawer()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.action_filter_search)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    storeArrayList.forEach {

                        if (it.itemTitle!!.toLowerCase(Locale.getDefault()).contains(searchText)) {

                            tempArrayList.add(it)

                        }

                    }

                    recyclerView.adapter!!.notifyDataSetChanged()

                }
                else {
                    tempArrayList.clear()
                    tempArrayList.addAll(storeArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        //navigationView.setCheckedItem(R.id.nav_home)

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
                val intent = Intent(this@StoreNavigation, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@StoreNavigation, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@StoreNavigation, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@StoreNavigation, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@StoreNavigation, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




}