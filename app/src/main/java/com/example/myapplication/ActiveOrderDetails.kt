package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CartItemAdapter2
import com.example.myapplication.adapters.OrderHistoryAdapter
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductDetailsAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ActiveOrderDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    lateinit var layoutHeader : View
    lateinit var userImage : ImageView
    lateinit var userName : TextView
    lateinit var userEmail : TextView

    lateinit var orUsername : TextView
    lateinit var orUserPhone : TextView
    lateinit var orUserAddr : TextView
    lateinit var totalPrice : TextView
    lateinit var purchaseId : TextView
    lateinit var orderDate: TextView
    lateinit var taxPrice : TextView
    lateinit var deliveryPrice : TextView
    lateinit var subTotal : TextView
    lateinit var orderStatus : TextView
    lateinit var orderTracker : TextView

    lateinit var paymentMethod : TextView
    lateinit var merchantName : TextView
    lateinit var buyAgain : MaterialButton

    private lateinit var orderItemArrayList : ArrayList<CartItem>
    private lateinit var totalCostArrayList: ArrayList<Double>
    private lateinit var productRecyclerView: RecyclerView

    private lateinit var dbrefOrder : DatabaseReference
    private lateinit var dbrefOrderItems : DatabaseReference
    private lateinit var dbrefPendingOrders : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.rv_order_history)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)

        orUsername = findViewById(R.id.username)
        orUserPhone = findViewById(R.id.user_phone)
        orUserAddr = findViewById(R.id.user_address)
        purchaseId = findViewById(R.id.order_id)
        orderDate = findViewById(R.id.order_date)

        paymentMethod = findViewById(R.id.payment_method)
        merchantName = findViewById(R.id.merchant_name)
        buyAgain = findViewById(R.id.order_btn)


        productRecyclerView = findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = LinearLayoutManager(productRecyclerView.context)
        orderItemArrayList = arrayListOf<CartItem>()
        totalCostArrayList = arrayListOf<Double>()

        totalPrice = findViewById(R.id.total_price)
        deliveryPrice = findViewById(R.id.delivery_price)
        taxPrice = findViewById(R.id.tax_price)
        subTotal = findViewById(R.id.sub_total_price)
        orderStatus = findViewById(R.id.order_status)
        orderTracker = findViewById(R.id.order_tracker)

        buyAgain.text = "Cancel / Refund Order"

        navigationDrawer()

        updateNavHeader()

        val orderId = intent.getStringExtra("OrderID")

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        dbrefOrder = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")
            .child("Active Orders").child("$orderId")

        dbrefOrder.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Order::class.java)

                purchaseId.text = order?.orderId.toString()
                orderDate.text = order?.paymentDate.toString()
                orderStatus.text = order?.orderStatus.toString()
                orderTracker.text= order?.orderTracking.toString()

                orUsername.text = order?.username.toString()
                orUserPhone.text = order?.mobile.toString()
                orUserAddr.text = order?.address.toString()

                paymentMethod.text = order?.paymentMethod.toString()
                merchantName.text = order?.paymentMerchant.toString()

                totalPrice.text = String.format("%.2f", order?.orderPayment)
                deliveryPrice.text = String.format("%.2f", 5.00)


                dbrefOrderItems = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")
                    .child("Active Orders").child("$orderId").child("Items")
                dbrefOrderItems.addValueEventListener( object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()) {
                            for (productSnapshot in snapshot.children) {
                                val product = productSnapshot.getValue(CartItem::class.java)
                                orderItemArrayList.add(product!!)
                                totalCostArrayList.add(product.total)

                                var adapter = CartItemAdapter2(orderItemArrayList)
                                productRecyclerView.adapter = adapter
                                adapter.setOnItemClickListener(object : CartItemAdapter2.onItemClickListener{

                                    override fun onItemClick(position: Int) {

                                        val productName = orderItemArrayList[position].itemName
                                        Toast.makeText(this@ActiveOrderDetails, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                                    }

                                })


                            }

                            subTotal.text = String.format("%.2f", totalCostArrayList.sum())
                            taxPrice.text = String.format("%.2f", (totalCostArrayList.sum() * 0.06))


                        }


                    }


                })



            }



        })




        buyAgain.setOnClickListener {

            val langDialog =  MaterialAlertDialogBuilder(this)
            langDialog.setMessage("Are you sure of cancelling this order?")
            langDialog.setPositiveButton("Yes") {
                    dialog, which ->
                Toast.makeText(this@ActiveOrderDetails, "Your cancellation will be processed, our team shall contact you shortly.", Toast.LENGTH_LONG).show()
                dialog.dismiss()

                dbrefPendingOrders = FirebaseDatabase.getInstance().getReference("/Orders").child("Pending Orders")
                    .child("$orderId")
                dbrefPendingOrders.child("orderStatus").setValue("Requested for Cancellation")

                dbrefOrder.child("orderStatus").setValue("Requested for Cancellation")

                val intent = Intent(this@ActiveOrderDetails, ActiveOrders::class.java)
                startActivity(intent)


            }
            langDialog.setNeutralButton("No") {
                    dialog, which ->
                dialog.dismiss()
            }
            langDialog.show()


        }



    }


    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        //navigationView.setCheckedItem(R.id.nav_favourites)

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
                val intent = Intent(this@ActiveOrderDetails, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@ActiveOrderDetails, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@ActiveOrderDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@ActiveOrderDetails, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@ActiveOrderDetails, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@ActiveOrderDetails, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@ActiveOrderDetails, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@ActiveOrderDetails, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@ActiveOrderDetails, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }





}