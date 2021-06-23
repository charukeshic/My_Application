package com.example.myapplication

import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class GetProductDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var productArrayList: ArrayList<ProductDetails>

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

        updateNavHeader()

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

                    val title = "Reminder to buy ".plus(product?.itemName.toString()).plus(" from ").plus(product?.store.toString())

                    addEvent.setOnClickListener {
                        val intent = Intent(Intent.ACTION_INSERT)
                        intent.data = CalendarContract.Events.CONTENT_URI
                        intent.putExtra(CalendarContract.Events.TITLE, title)

                        if(intent.resolveActivity(getPackageManager()) != null){
                            addTheEvent(product!!)
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this@GetProductDetails, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                        }

                    }

                    addToFav.setOnClickListener {
                        addToFavourites(product!!)
                        //Toast.makeText(this@GetProductDetails, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                    }

                    addToCart.setOnClickListener {
                        Toast.makeText(this@GetProductDetails, "$productName added to Cart", Toast.LENGTH_SHORT).show()
                    }


                }

            }


        })

    }


    private fun addToFavourites(product : ProductDetails) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("Profile Activity", "username: $uid")
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites")

        val productName = product.itemName.toString().plus("(").plus(product.store).plus(")")

        ref.child("$productName").setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this@GetProductDetails, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
            }

    }

    private fun addTheEvent(product : ProductDetails) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Events")

        val productName = product.itemName.toString().plus("(").plus(product.store).plus(")")
        ref.child("$productName").setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this@GetProductDetails, "Creating event for $productName", Toast.LENGTH_SHORT).show()
            }

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
                val intent = Intent(this@GetProductDetails, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@GetProductDetails, ProfileActivity::class.java)
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