package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CartItemAdapter2
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class OrderActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
    lateinit var taxPrice : TextView
    lateinit var deliveryPrice : TextView
    lateinit var subTotal : TextView

    lateinit var edit : TextView
    lateinit var selectPaymentMethod : TextView
    lateinit var selectMerchant : TextView
    lateinit var orderBtn : MaterialButton
    lateinit var paymentMethod : TextView
    lateinit var merchantName : TextView

    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var dbrefUser : DatabaseReference
    private lateinit var productArrayList: ArrayList<CartItem>
    private lateinit var productRecyclerView: RecyclerView

    private lateinit var dbrefOrder : DatabaseReference
    private lateinit var dbrefOrderItems : DatabaseReference
    private lateinit var dbrefActiveOrder : DatabaseReference
    private lateinit var dbrefActiveOrderItems : DatabaseReference

    private lateinit var dbrefPendingOrders : DatabaseReference
    private lateinit var dbrefCompletedOrders : DatabaseReference


    var selectedIndex = 0
    var selected = 0
    val paymentMethods = arrayOf(
        "Cash On Delivery", "FPX / Online Banking"
    )

    val merchantNames = arrayOf(
        "Public Bank", "Maybank2u", "CIMB Bank"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_order)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)

        orUsername = findViewById(R.id.username)
        orUserPhone = findViewById(R.id.user_phone)
        orUserAddr = findViewById(R.id.user_address)

        edit = findViewById(R.id.edit_details)
        selectPaymentMethod = findViewById(R.id.select_payment_method)
        selectMerchant = findViewById(R.id.select_merchant)
        orderBtn = findViewById(R.id.order_btn)
        paymentMethod = findViewById(R.id.payment_method)
        merchantName = findViewById(R.id.merchant_name)


        productRecyclerView = findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = LinearLayoutManager(productRecyclerView.context)
        productArrayList = arrayListOf<CartItem>()

        totalPrice = findViewById(R.id.total_price)
        deliveryPrice = findViewById(R.id.delivery_price)
        taxPrice = findViewById(R.id.tax_price)
        subTotal = findViewById(R.id.sub_total_price)

        navigationDrawer()

        updateNavHeader()

        val totalCost = intent.getStringExtra("Total")

        subTotal.text = String.format("%.2f", totalCost!!.toDouble())
        deliveryPrice.text = String.format("%.2f", 5.00)
        taxPrice.text = String.format("%.2f", (totalCost!!.toDouble() * 0.06))
        val sum = (totalCost.toDouble().plus(5.00).plus((totalCost!!.toDouble() * 0.06.toDouble())))
        totalPrice.text = String.format("%.2f", sum)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        dbrefUser = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")
        dbrefUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                orUsername.text = user?.username.toString()
                orUserPhone.text = user?.mobileNo.toString()
                orUserAddr.text = user?.address.toString()

            }

        })


        Log.d("Profile Activity", "username: $uid")
        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Cart")

        dbrefProducts.addValueEventListener (object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                productArrayList.clear()

                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(CartItem::class.java)
                        productArrayList.add(product!!)

                        var adapter = CartItemAdapter2(productArrayList)
                        productRecyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object: CartItemAdapter2.onItemClickListener {

                            override fun onItemClick(position: Int) {

                                val productName = productArrayList[position].itemName

                                Toast.makeText(this@OrderActivity, "You clicked on $productName", Toast.LENGTH_SHORT).show()



                            }

                        })


                    }

                }

            }


        })

        edit.setOnClickListener {

            val intent = Intent(this@OrderActivity, ProfileActivity::class.java)
            startActivity(intent)

        }

        selectPaymentMethod.setOnClickListener {

            selectedPaymentMethod(it)

        }

        selectMerchant.setOnClickListener {

            selectedMerchant(it)

        }

        orderBtn.setOnClickListener {



            if(orUserAddr.text.equals("Not Updated") or orUserAddr.text.isEmpty()) {

                Toast.makeText(this@OrderActivity, "Please use a valid address", Toast.LENGTH_LONG).show()

            }
            else {

                createOrder()
                finish()
                Toast.makeText(this@OrderActivity, "Your order will be processed", Toast.LENGTH_LONG).show()
            }


        }



    }

    private fun createOrder() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        dbrefOrder = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Purchase History")
        dbrefActiveOrder = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Active Orders")
        dbrefPendingOrders = FirebaseDatabase.getInstance().getReference("/Orders").child("Pending Orders")
        dbrefCompletedOrders = FirebaseDatabase.getInstance().getReference("/Orders").child("Completed Orders")

        val orderId = UUID.randomUUID().toString().substring(0,8)
        val orderUser = orUsername.text.toString()
        val orderPhone = orUserPhone.text.toString()
        val orderAddr = orUserAddr.text.toString()
        val orderPaymentMethod = paymentMethod.text.toString()
        val orderPaymentMerchantName = merchantName.text.toString()
        val orderPayment = totalPrice.text.toString().toDouble()
        val currentDateTime = LocalDateTime.now()
        val orderStatus = "Processing"
        val orderTracking = "https://www.ninjavan.co/en-my/tracking"
        val paymentDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val createOrder = Order(orderId, orderUser, orderPhone, orderAddr, orderPaymentMethod, orderPaymentMerchantName, orderPayment, paymentDate, orderStatus, orderTracking)

        dbrefOrder.child("$orderId").setValue(createOrder)
        dbrefActiveOrder.child("$orderId").setValue(createOrder)

        dbrefPendingOrders.child("$orderId").setValue(createOrder)
        dbrefCompletedOrders.child("$orderId").setValue(createOrder)

        dbrefOrderItems = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Purchase History")
        dbrefActiveOrderItems = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Active Orders")



        for(items in productArrayList) {

            val nameOfItem = items.itemName.plus("(").plus(items.store).plus(")")

            dbrefOrderItems.child("$orderId").child("Items").child("$nameOfItem").setValue(items)
            dbrefActiveOrderItems.child("$orderId").child("Items").child("$nameOfItem").setValue(items)

            dbrefPendingOrders.child("$orderId").child("Items").child("$nameOfItem").setValue(items)
            dbrefCompletedOrders.child("$orderId").child("Items").child("$nameOfItem").setValue(items)

            dbrefProducts = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Cart")
            dbrefProducts.child("$nameOfItem").removeValue()

        }


    }


    fun selectedPaymentMethod(view: View) {
        var selectItem = paymentMethods[selectedIndex]
        val langDialog =  MaterialAlertDialogBuilder(this)
        langDialog.setTitle("Select Payment Method")
        langDialog.setSingleChoiceItems(paymentMethods,selectedIndex) {
            dialog, which ->
            selectedIndex = which
            selectItem = paymentMethods[which]

        }
        langDialog.setPositiveButton("Ok") {
                dialog, which ->
                paymentMethod.text = selectItem
            merchantName.text = merchantNames[0]
            if(selectItem.equals("Cash On Delivery")) {
                merchantName.text = "-"
            }
                dialog.dismiss()
        }
        langDialog.setNeutralButton("Cancel") {
                dialog, which ->
                dialog.dismiss()
        }
        langDialog.show()


    }


    fun selectedMerchant(view: View) {

        if (paymentMethod.text.equals("Cash On Delivery")) {
            merchantName.text = "-"
            Toast.makeText(this@OrderActivity, "No merchant required for cash on delivery", Toast.LENGTH_SHORT).show()
        }
        else {

            var selectItem = merchantNames[selected]
            val langDialog =  MaterialAlertDialogBuilder(this)
            langDialog.setTitle("Select Payment Method")
            langDialog.setSingleChoiceItems(merchantNames,selected) {
                    dialog, which ->
                selected = which
                selectItem = merchantNames[which]
            }
            langDialog.setPositiveButton("Ok") {
                    dialog, which ->
                merchantName.text = selectItem
                dialog.dismiss()
            }
            langDialog.setNeutralButton("Cancel") {
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
                val intent = Intent(this@OrderActivity, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@OrderActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@OrderActivity, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@OrderActivity, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@OrderActivity, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@OrderActivity, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@OrderActivity, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@OrderActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@OrderActivity, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }





}


