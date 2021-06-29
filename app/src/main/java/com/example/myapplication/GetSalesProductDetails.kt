package com.example.myapplication

import android.content.Intent
import android.graphics.Paint
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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductDetailsAdapter
import com.example.myapplication.fragments.ProductFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class GetSalesProductDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView
    lateinit var cartIcon: ImageView

    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var productArrayList: ArrayList<SalesProduct>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_get_sales_product_details)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)
        cartIcon = findViewById(R.id.cart_icon)


        productArrayList = arrayListOf<SalesProduct>()

        val mall = intent.getStringExtra("SALE_MALL").toString()
        val product = intent.getStringExtra("SALE_PRODUCT").toString()

        Log.d("GetSalesProductDetails", "$mall")
        Log.d("GetSalesProductDetails", "$product")

        navigationDrawer()

        getProductDetails(mall, product)


        cartIcon.setOnClickListener {
            getCartItems()
        }


    }

    private fun getCartItems() {

        val intent = Intent(this@GetSalesProductDetails, CartActivity::class.java)
        startActivity(intent)

    }

    private fun getProductDetails(mallName : String, productName : String) {

        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").
        child("sales").child("$productName")


        dbrefProducts.addListenerForSingleValueEvent (object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val storeName : TextView = findViewById(R.id.mall_name)
                val productImage : ImageView = findViewById(R.id.mall_image)
                val itemName : TextView = findViewById(R.id.product_name)
                val productOriPrice : TextView = findViewById(R.id.product_original_price)
                val productDiscPrice : TextView = findViewById(R.id.product_discounted_price)
                val productDetails : TextView = findViewById(R.id.item_details)
                val textPrice : TextView = findViewById(R.id.text_price)

                productOriPrice.setPaintFlags(productOriPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                textPrice.setPaintFlags(textPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

                val addEvent : LinearLayout = findViewById(R.id.create_event)
                val addToFav : LinearLayout = findViewById(R.id.add_to_fav)
                val addToCart : LinearLayout = findViewById(R.id.add_to_cart)


                if(snapshot.exists()) {
                    val product = snapshot.getValue(SalesProduct::class.java)
                    product?.store = mallName.toString()

                    itemName.text = product?.itemName.toString()
                    productOriPrice.text = product?.originalPrice.toString()
                    productDiscPrice.text = product?.discountedPrice.toString()
                    productDetails.text = product?.itemDetails.toString()
                    storeName.text = product?.store.toString()
                    Picasso.get().load(product?.image).into(productImage)

                    val title = "Reminder to buy ".plus(product?.itemName.toString()).plus(" from ").plus(product?.store.toString())

                    addEvent.setOnClickListener {
                        val intent = Intent(Intent.ACTION_INSERT)
                        intent.data = CalendarContract.Events.CONTENT_URI
                        intent.putExtra(CalendarContract.Events.TITLE, title)

                        if(intent.resolveActivity(getPackageManager()) != null){

                            val name = product?.itemName.toString()
                            val price = product?.discountedPrice.toString()
                            val details = product?.itemDetails.toString()
                            val store = product?.store.toString()
                            val image = product?.image.toString()

                            val productDetail = ProductDetails(name, store, image, price, details)

                            addTheEvent(productDetail)
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this@GetSalesProductDetails, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(this@GetSalesProductDetails, "$productName event added", Toast.LENGTH_SHORT).show()
                    }

                    addToFav.setOnClickListener {

                        val name = product?.itemName.toString()
                        val price = product?.discountedPrice.toString()
                        val details = product?.itemDetails.toString()
                        val store = product?.store.toString()
                        val image = product?.image.toString()

                        val productDetail = ProductDetails(name, store, image, price, details)

                        addToFavourites(productDetail)
                        //Toast.makeText(this@GetSalesProductDetails, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                    }

                    addToCart.setOnClickListener {

                        val cartItemName = product?.itemName.toString()
                        val cartItemStore = product?.store.toString()
                        val cartItemImage = product?.image.toString()
                        val cartItemDetails = product?.itemDetails.toString()
                        val cartItemQuantity = 1
                        val cartItemPrice = String.format("%.2f", product?.discountedPrice?.toDouble()).toDouble()
                        val cartItemTotal = String.format("%.2f", (cartItemQuantity * cartItemPrice)).toDouble()

                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        Log.d("Profile Activity", "username: $uid")
                        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Cart")

                        val productName = product?.itemName.toString().plus("(").plus(product?.store).plus(")")

                        val cartItem = CartItem(cartItemName, cartItemStore, cartItemImage, cartItemPrice, cartItemDetails, cartItemQuantity, cartItemTotal)

                        ref.child("$productName").setValue(cartItem)
                            .addOnSuccessListener {
                                Toast.makeText(this@GetSalesProductDetails, "$productName added to cart", Toast.LENGTH_SHORT).show()
                            }

                        //Toast.makeText(this@GetSalesProductDetails, "$productName added to Cart", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@GetSalesProductDetails, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
            }

    }


    private fun addTheEvent(product : ProductDetails) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Events")

        val productName = product.itemName.toString().plus("(").plus(product.store).plus(")")
        ref.child("$productName").setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this@GetSalesProductDetails, "Creating event for $productName", Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this@GetSalesProductDetails, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@GetSalesProductDetails, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@GetSalesProductDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@GetSalesProductDetails, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@GetSalesProductDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@GetSalesProductDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@GetSalesProductDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@GetSalesProductDetails, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@GetSalesProductDetails, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}