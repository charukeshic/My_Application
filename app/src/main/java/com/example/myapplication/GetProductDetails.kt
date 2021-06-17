package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductDetailsAdapter
import com.example.myapplication.fragments.ProductFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class GetProductDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    //val storeName : TextView = findViewById(R.id.mall_name)
    //val productImage : ImageView = findViewById(R.id.mall_image)
    //val itemName : TextView = findViewById(R.id.product_name)
    //val productPrice : TextView = findViewById(R.id.product_price)
    //val productDetails : TextView = findViewById(R.id.item_details)

    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var productArrayList: ArrayList<ProductDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_get_product_details)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)

        productArrayList = arrayListOf<ProductDetails>()

        val mall = intent.getStringExtra("MALL").toString()
        val category = intent.getStringExtra("CATEGORY").toString()
        val product = intent.getStringExtra("PRODUCT").toString()

        Log.d("GetProductDetails", "$mall")
        Log.d("GetProductDetails", "$category")
        Log.d("GetProductDetails", "$product")

        navigationDrawer()

        getProductDetails(mall, category, product)


    }

    private fun getProductDetails(mallName : String, categoryName : String, productName : String) {

        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").
        child("categories").child("$categoryName").child("$productName")


        dbrefProducts.addListenerForSingleValueEvent (object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val storeName : TextView = findViewById(R.id.mall_name)
                val productImage : ImageView = findViewById(R.id.mall_image)
                val itemName : TextView = findViewById(R.id.product_name)
                val productPrice : TextView = findViewById(R.id.product_price)
                val productDetails : TextView = findViewById(R.id.item_details)

                val addEvent : LinearLayout = findViewById(R.id.create_event)
                val addToFav : LinearLayout = findViewById(R.id.add_to_fav)
                val addToCart : LinearLayout = findViewById(R.id.add_to_cart)


                if(snapshot.exists()) {
                    val product = snapshot.getValue(ProductDetails::class.java)
                    product?.store = mallName.toString()

                    itemName.text = product?.itemName.toString()
                    productPrice.text = product?.price.toString()
                    productDetails.text = product?.itemDetails.toString()
                    storeName.text = product?.store.toString()
                    Picasso.get().load(product?.image).into(productImage)


                    addEvent.setOnClickListener {
                        Toast.makeText(this@GetProductDetails, "$productName event added", Toast.LENGTH_SHORT).show()
                    }

                    addToFav.setOnClickListener {
                        Toast.makeText(this@GetProductDetails, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                    }

                    addToCart.setOnClickListener {
                        Toast.makeText(this@GetProductDetails, "$productName added to Cart", Toast.LENGTH_SHORT).show()
                    }


                }

            }


        })

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
                val intent = Intent(this@GetProductDetails, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@GetProductDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@GetProductDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@GetProductDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@GetProductDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@GetProductDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@GetProductDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@GetProductDetails, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@GetProductDetails, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}