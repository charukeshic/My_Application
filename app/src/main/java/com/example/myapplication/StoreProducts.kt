package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.CategoryAdapter
import com.google.android.gms.common.data.DataBufferRef
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class StoreProducts : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    private lateinit var categoryRecyclerView: RecyclerView

    private lateinit var dbref : DatabaseReference
    private lateinit var categoryArrayList: ArrayList<Category>

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


        navigationDrawer()

        //fetchCategory()


        categoryRecyclerView = findViewById(R.id.category_recyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.setHasFixedSize(true)

        categoryArrayList = arrayListOf<Category>()
        getCategory()



    }

    private fun getCategory() {

        val mallName = intent.getStringExtra(OnlineShopping.STORE_KEY)

        dbref = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").child("categories")

        dbref.addValueEventListener(object: ValueEventListener {
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
        navigationView.setCheckedItem(R.id.nav_favourites)

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
                val intent = Intent(this@StoreProducts, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {

            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}