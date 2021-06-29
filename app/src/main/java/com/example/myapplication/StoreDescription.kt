package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class StoreDescription : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    lateinit var callBtn: Button
    lateinit var bookBtn: Button
    lateinit var navigateBtn: Button
    lateinit var grabBtn: Button

    lateinit var layoutHeader : View
    lateinit var userImage : ImageView
    lateinit var userName : TextView
    lateinit var userEmail : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_store_description)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        val storeName : TextView = findViewById(R.id.item_title)
        val storePic : ImageView = findViewById(R.id.item_image)
        val storeDetails : TextView = findViewById(R.id.item_details)
        val storeDesc : TextView = findViewById(R.id.item_description)
        val storeOpHour : TextView = findViewById(R.id.item_opHour)
        val storeAddress : TextView = findViewById(R.id.item_address)
        val storeContact : TextView = findViewById(R.id.item_contact)

        val busDetails : TextView = findViewById(R.id.bus_details)
        val trainDetails : TextView = findViewById(R.id.train_details)

        callBtn = findViewById(R.id.call_btn)
        bookBtn = findViewById(R.id.book_btn)
        navigateBtn = findViewById(R.id.nav_btn)
        grabBtn = findViewById(R.id.grab_btn)


        val bundle : Bundle?= intent.extras
        val title = bundle!!.getString("title")
        val image = bundle.getInt("image")
        val details = bundle.getString("details")
        val description = bundle.getString("description")
        val opHour = bundle.getString("operation")
        val address = bundle.getString("address")
        val contact = bundle.getString("contact")
        val bus = bundle.getString("bus")
        val train = bundle.getString("train")


        storeName.text = title
        storePic.setImageResource(image)
        storeDetails.text = details
        storeDesc.text = description
        storeOpHour.text = opHour
        storeAddress.text = address
        storeContact.text = contact
        busDetails.text = bus
        trainDetails.text = train

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)


        navigationDrawer()

        updateNavHeader()

        callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(contact)))
            startActivity(intent)
        }

        bookBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(("http://www.google.com")))
            startActivity(intent)
        }

        navigateBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0, 0?q=$address"))
            startActivity(intent)
        }

        grabBtn.setOnClickListener {

            val installed  : Boolean = appInstalledOrNot("com.grabtaxi.passenger")

            if (installed) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0, 0?q=$address"))
                startActivity(intent)


            }
            else {
                Log.d("Start", "what happen to this function")
                Toast.makeText(this, "Grab app not installed", Toast.LENGTH_SHORT)
            }

        }


    }

    private fun appInstalledOrNot(url : String): Boolean {
        val packageManager = packageManager
        val appInstalled : Boolean
        appInstalled = try {
            Log.d("Start", "nvr reach this function")
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("Start", "done this function")
            false
        }
        return appInstalled

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

    private fun updateNavHeader() {

        layoutHeader = navigationView.getHeaderView(0)
        userName = layoutHeader.findViewById(R.id.username1)
        userImage = layoutHeader.findViewById(R.id.user_image)
        userEmail = layoutHeader.findViewById(R.id.email1)


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("Profile Activity", "username: $uid")
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userName.text = user?.username.toString()
                userEmail.text = user?.email.toString()
                Picasso.get().load(user?.image).into(userImage)

            }

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
                val intent = Intent(this@StoreDescription, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@StoreDescription, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@StoreDescription, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@StoreDescription, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@StoreDescription, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@StoreDescription, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@StoreDescription, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@StoreDescription, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@StoreDescription, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




}