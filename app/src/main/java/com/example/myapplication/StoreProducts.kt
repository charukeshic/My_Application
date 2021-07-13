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
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CategoryAdapter
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.adapters.ProductDetailsAdapter
import com.example.myapplication.adapters.RecommendedProductAdapter
import com.example.myapplication.fragments.CategoryFragment
import com.example.myapplication.fragments.ProductFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class StoreProducts : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView
    lateinit var cartIcon: ImageView

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var recommendedRecyclerView : RecyclerView

    private lateinit var dbrefCategories : DatabaseReference
    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var dbrefgetProducts : DatabaseReference
    private lateinit var dbrefOrder : DatabaseReference
    private lateinit var dbrefRecommendedProducts : DatabaseReference
    private lateinit var dbrefcatStore : DatabaseReference
    private lateinit var categoryArrayList: ArrayList<Category>
    private lateinit var productArrayList: ArrayList<ProductDetails>
    private lateinit var recommendedArrayList: ArrayList<CartItem>

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
        setContentView(R.layout.activity_store_products)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)
        cartIcon = findViewById(R.id.cart_icon)

        navigationDrawer()

        //fetchCategory()
        updateNavHeader()


        categoryRecyclerView = findViewById(R.id.category_recyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.setHasFixedSize(true)

        categoryArrayList = arrayListOf<Category>()
        getCategory()

        productRecyclerView = findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = GridLayoutManager(this, 2)

        productArrayList = arrayListOf<ProductDetails>()
        getProducts()


        recommendedRecyclerView = findViewById(R.id.recommended_recyclerView)
        recommendedRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedRecyclerView.setHasFixedSize(true)

        recommendedArrayList = arrayListOf<CartItem>()
        getProducts2()

        cartIcon.setOnClickListener {
            getCartItems()
        }


    }

    private fun getProducts2() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        dbrefOrder = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Purchase History")
        dbrefOrder.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    for(orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        val orderID = order?.orderId

                        dbrefRecommendedProducts = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")
                            .child("Purchase History").child("$orderID").child("Items")
                        dbrefRecommendedProducts.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(productSnapshot in snapshot.children) {
                                    val product = productSnapshot.getValue(CartItem::class.java)
                                    recommendedArrayList.add(product!!)

                                }

                                var adapter = RecommendedProductAdapter(recommendedArrayList)
                                recommendedRecyclerView.adapter = adapter
                                adapter.setOnItemClickListener(object : RecommendedProductAdapter.onItemClickListener{
                                    override fun onItemClick(position: Int) {

                                        val productName = recommendedArrayList[position].itemName
                                        val mallName = recommendedArrayList[position].store
                                        val price = recommendedArrayList[position].price.toString()
                                        val details = recommendedArrayList[position].itemDetails
                                        val image = recommendedArrayList[position].image

                                        Toast.makeText(this@StoreProducts, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                                        //productArrayList.clear()

                                        val intent = Intent(this@StoreProducts, GetRecommendedProductDetails::class.java)
                                        intent.putExtra("MALL", mallName)
                                        intent.putExtra("PRODUCT", productName)
                                        intent.putExtra("IMAGE", image)
                                        intent.putExtra("PRICE", price)
                                        intent.putExtra("DETAILS", details)
                                        startActivity(intent)


                                    }


                                })


                            }


                        })

                    }

                }

            }


        })



    }

    private fun getCartItems() {

        val intent = Intent(this@StoreProducts, CartActivity::class.java)
        startActivity(intent)

    }

    private fun getProducts() {
        val mallName = intent.getStringExtra(OnlineShopping.STORE_KEY)
        val categoryName = ("Canned Foods").toString()

        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").
        child("categories").child("Canned Foods")

        dbrefProducts.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ProductDetails::class.java)
                        product?.store = mallName.toString()

                        productArrayList.add(product!!)

                    }

                    val adapter = ProductAdapter(productArrayList)
                    productRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val productName = productArrayList[position].itemName
                            Toast.makeText(this@StoreProducts, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                            //productArrayList.clear()


                            val intent = Intent(this@StoreProducts, StoreProducts2::class.java)
                            intent.putExtra("MALL", mallName)
                            intent.putExtra("CATEGORY", categoryName)
                            intent.putExtra("PRODUCT", productName)

                            Log.d("Store Products", "$mallName")
                            Log.d("Store Products", "$categoryName")
                            Log.d("Store Products", "$productName")


                            startActivity(intent)



                        }


                    })


                }

            }


        })

    }




    private fun getCategory() {

        val mallName = intent.getStringExtra(OnlineShopping.STORE_KEY)

        dbrefCategories = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").child("categoriesList")


        dbrefCategories.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        val category = categorySnapshot.getValue(Category::class.java)
                        categoryArrayList.add(category!!)
                    }

                    var adapter = CategoryAdapter(categoryArrayList)
                    categoryRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val categoryName = categoryArrayList[position].title
                            Toast.makeText(this@StoreProducts, "You clicked on $categoryName", Toast.LENGTH_SHORT).show()

                            productArrayList.clear()

                            dbrefgetProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").
                            child("categories").child("$categoryName")

                            dbrefgetProducts.addValueEventListener(object: ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {

                                    if(snapshot.exists()) {
                                        for (productSnapshot in snapshot.children) {
                                            val product = productSnapshot.getValue(ProductDetails::class.java)
                                            product?.store = mallName.toString()

                                            productArrayList.add(product!!)
                                        }

                                        var adapter = ProductAdapter(productArrayList)
                                        productRecyclerView.adapter = adapter
                                        adapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
                                            override fun onItemClick(position: Int) {

                                                val productName = productArrayList[position].itemName
                                                Toast.makeText(this@StoreProducts, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                                                //productArrayList.clear()

                                                val intent = Intent(this@StoreProducts, StoreProducts2::class.java)
                                                intent.putExtra("MALL", mallName)
                                                intent.putExtra("CATEGORY", categoryName)
                                                intent.putExtra("PRODUCT", productName)

                                                Log.d("Store Products", "$mallName")
                                                Log.d("Store Products", "$categoryName")
                                                Log.d("Store Products", "$productName")

                                                startActivity(intent)


                                            }


                                        })


                                    }

                                }


                            })


                        }


                    })


                }

            }


        })

    }



    private fun fetchCategory() {

        val mallName = intent.getStringExtra(OnlineShopping.STORE_KEY)

        val ref = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").child("categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    val category = it.getValue(Category::class.java)
                    if (category != null) {
                        adapter.add(CategoryItem(category))

                    }
                }

                adapter.setOnItemClickListener {item, view ->

                    //val categoryItem = item as CategoryItem

                    //val intent = Intent(view.context, StoreProducts::class.java)
                    //intent.putExtra(CATEGORY, categoryItem.category.title)
                    //startActivity(intent)


                }

                categoryRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

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
                val intent = Intent(this@StoreProducts, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@StoreProducts, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@StoreProducts, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@StoreProducts, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@StoreProducts, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@StoreProducts, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@StoreProducts, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@StoreProducts, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}