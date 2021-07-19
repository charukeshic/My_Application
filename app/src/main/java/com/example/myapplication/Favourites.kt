package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductDetailsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class Favourites : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView
    lateinit var cartIcon: ImageView

    lateinit var layoutHeader : View
    lateinit var userImage : ImageView
    lateinit var userName : TextView
    lateinit var userEmail : TextView
    lateinit var chatbot: FloatingActionButton

    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var productArrayList: ArrayList<ProductDetails>
    private lateinit var productRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_favourites)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)
        cartIcon = findViewById(R.id.cart_icon)
        chatbot = findViewById(R.id.chatbot)

        productRecyclerView = findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = LinearLayoutManager(productRecyclerView.context)
        productArrayList = arrayListOf<ProductDetails>()

        navigationDrawer()

        updateNavHeader()

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        Log.d("Profile Activity", "username: $uid")
        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites")

        dbrefProducts.addValueEventListener (object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                productArrayList.clear()

                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ProductDetails::class.java)
                        productArrayList.add(product!!)

                        var adapter = ProductDetailsAdapter(productArrayList)
                        productRecyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object: ProductDetailsAdapter.onItemClickListener {

                            override fun onItemClick(position: Int) {

                                val productName = productArrayList[position].itemName

                                Toast.makeText(this@Favourites, "You clicked on $productName", Toast.LENGTH_SHORT).show()



                            }

                        })
                    }

                }

            }


        })


        cartIcon.setOnClickListener {
            getCartItems()
        }

        chatbot.setOnClickListener {

            val chat = "https://dialogflow.cloud.google.com/#/agent/shoppingchatbot-alpv/integrations"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(("$chat")))
            startActivity(intent)


        }


    }

    private fun getCartItems() {

        val intent = Intent(this@Favourites, CartActivity::class.java)
        startActivity(intent)

    }


    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_favourites)

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
                val intent = Intent(this@Favourites, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@Favourites, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@Favourites, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@Favourites, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@Favourites, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val calendarUri: Uri = CalendarContract.CONTENT_URI
                    .buildUpon()
                    .appendPath("time")
                    .build()
                startActivity(Intent(Intent.ACTION_VIEW, calendarUri))
                navigationView.setCheckedItem(R.id.nav_events)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@Favourites, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                val intent = Intent(this@Favourites, ShareActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_contact -> {
                val intent = Intent(this@Favourites, ContactInformation::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}