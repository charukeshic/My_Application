package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.fragments.StoreFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class OnlineShopping : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //private lateinit var dbref : DatabaseReference
    private lateinit var mallRecyclerView: RecyclerView

    //private lateinit var mallArrayList: ArrayList<Mall>

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_online_shopping)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)

        mallRecyclerView = findViewById(R.id.mallList)

        fetchMall()

        navigationDrawer()



    }


    companion object {
        val STORE_KEY = "STORE_KEY"
    }

    private fun fetchMall() {

        val ref = FirebaseDatabase.getInstance().getReference("/Store")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    val mall = it.getValue(Mall::class.java)
                    if (mall != null) {
                        adapter.add(MallItem(mall))

                    }
                }

                adapter.setOnItemClickListener {item, view ->

                    val mallItem = item as MallItem

                    val intent = Intent(view.context, StoreProducts::class.java)
                    intent.putExtra(STORE_KEY, mallItem.mall.title)
                    startActivity(intent)

                    finish()

                }

                mallRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }


    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)

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
                val intent = Intent(this@OnlineShopping, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@OnlineShopping, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@OnlineShopping, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@OnlineShopping, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@OnlineShopping, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@OnlineShopping, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@OnlineShopping, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@OnlineShopping, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}

class MallItem (val mall : Mall) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.findViewById<Button>(R.id.mall_name).text = mall.title
        Picasso.get().load(mall.image).into(viewHolder.itemView.findViewById<ImageView>(R.id.mall_image))

        viewHolder.itemView.findViewById<Button>(R.id.mall_name).setOnClickListener {

            val intent = Intent(viewHolder.itemView.findViewById<Button>(R.id.mall_name).context, StoreProducts::class.java)
            intent.putExtra(StoreFragment.STORE_KEY, mall.title)
            startActivity(viewHolder.itemView.findViewById<Button>(R.id.mall_name).context, intent, null)

        }

    }

    override fun getLayout(): Int {
        return R.layout.store_item
    }


}

class CategoryItem (val category : Category) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.findViewById<TextView>(R.id.category_name).text = category.title
        Picasso.get().load(category.image).into(viewHolder.itemView.findViewById<ImageView>(R.id.mall_image))
    }

    override fun getLayout(): Int {
        return R.layout.category_item
    }


}