package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class StoreNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    private lateinit var recyclerView: RecyclerView
    private lateinit var storeArrayList: ArrayList<Store>
    private lateinit var tempArrayList: ArrayList<Store>

    lateinit var imageId : Array<Int>
    lateinit var title : Array<String>
    lateinit var details : Array<String>
    lateinit var description : Array<String>
    lateinit var opHour : Array<String>
    lateinit var address : Array<String>
    lateinit var contactNum : Array<String>
    lateinit var bus : Array<String>
    lateinit var train : Array<String>
    lateinit var web : Array<String>
    lateinit var category : Array<String>

    lateinit var layoutHeader : View
    lateinit var userImage : ImageView
    lateinit var userName : TextView
    lateinit var userEmail : TextView

    lateinit var search_toolbar: Toolbar
    lateinit var chatbot : FloatingActionButton

    lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_store_navigation)

        search_toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        chatbot = findViewById(R.id.chatbot)

        spinner = findViewById(R.id.category_spinner)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        storeArrayList = arrayListOf<Store>()

        tempArrayList = arrayListOf<Store>()


        imageId = arrayOf(
            R.drawable.marini,
            R.drawable.troika,
            R.drawable.thirty8,
            R.drawable.enakkl,
            R.drawable.nobukl,
            R.drawable.marble8,
            R.drawable.toroloco,
            R.drawable.positano,
            R.drawable.roof_top4,
            R.drawable.qureshi
        )

        title = arrayOf(
            getString(R.string.title_1),
            getString(R.string.title_2),
            getString(R.string.title_3),
            getString(R.string.title_4),
            getString(R.string.title_5),
            getString(R.string.title_6),
            getString(R.string.title_7),
            getString(R.string.title_8),
            getString(R.string.title_9),
            getString(R.string.title_10)

        )

        details = arrayOf(
            getString(R.string.details_1),
            getString(R.string.details_2),
            getString(R.string.details_3),
            getString(R.string.details_4),
            getString(R.string.details_5),
            getString(R.string.details_6),
            getString(R.string.details_7),
            getString(R.string.details_8),
            getString(R.string.details_9),
            getString(R.string.details_10)
        )

        description = arrayOf(
            getString(R.string.description_1),
            getString(R.string.description_2),
            getString(R.string.description_3),
            getString(R.string.description_4),
            getString(R.string.description_5),
            getString(R.string.description_6),
            getString(R.string.description_7),
            getString(R.string.description_8),
            getString(R.string.description_9),
            getString(R.string.description_10)

        )

        opHour = arrayOf(
            getString(R.string.opHour_1),
            getString(R.string.opHour_2),
            getString(R.string.opHour_3),
            getString(R.string.opHour_4),
            getString(R.string.opHour_5),
            getString(R.string.opHour_6),
            getString(R.string.opHour_7),
            getString(R.string.opHour_8),
            getString(R.string.opHour_9),
            getString(R.string.opHour_10)
        )

        address = arrayOf(
            getString(R.string.addr_1),
            getString(R.string.addr_2),
            getString(R.string.addr_3),
            getString(R.string.addr_4),
            getString(R.string.addr_5),
            getString(R.string.addr_6),
            getString(R.string.addr_7),
            getString(R.string.addr_8),
            getString(R.string.addr_9),
            getString(R.string.addr_10)
        )

        contactNum = arrayOf(
            getString(R.string.contact_1),
            getString(R.string.contact_2),
            getString(R.string.contact_3),
            getString(R.string.contact_4),
            getString(R.string.contact_5),
            getString(R.string.contact_6),
            getString(R.string.contact_7),
            getString(R.string.contact_8),
            getString(R.string.contact_9),
            getString(R.string.contact_10)
        )

        bus = arrayOf(
            getString(R.string.bus_1),
            getString(R.string.bus_2),
            getString(R.string.bus_3),
            getString(R.string.bus_4),
            getString(R.string.bus_5),
            getString(R.string.bus_6),
            getString(R.string.bus_7),
            getString(R.string.bus_8),
            getString(R.string.bus_9),
            getString(R.string.bus_10)

        )

        train = arrayOf(
            getString(R.string.train_1),
            getString(R.string.train_2),
            getString(R.string.train_3),
            getString(R.string.train_4),
            getString(R.string.train_5),
            getString(R.string.train_6),
            getString(R.string.train_7),
            getString(R.string.train_8),
            getString(R.string.train_9),
            getString(R.string.train_10)
        )

        web = arrayOf(
            getString(R.string.web_1),
            getString(R.string.web_2),
            getString(R.string.web_3),
            getString(R.string.web_4),
            getString(R.string.web_5),
            getString(R.string.web_6),
            getString(R.string.web_7),
            getString(R.string.web_8),
            getString(R.string.web_9),
            getString(R.string.web_10)
        )

        category = arrayOf(
            getString(R.string.cat_1),
            getString(R.string.cat_2),
            getString(R.string.cat_3),
            getString(R.string.cat_1),
            getString(R.string.cat_2),
            getString(R.string.cat_3),
            getString(R.string.cat_1),
            getString(R.string.cat_2),
            getString(R.string.cat_3),
            getString(R.string.cat_1)
        )


        for (i in imageId.indices) {

            val store = Store(title[i],details[i],imageId[i],description[i],opHour[i],address[i],contactNum[i], bus[i], train[i], web[i], category[i])
            storeArrayList.add(store)

        }

        recyclerView.adapter = RecyclerViewAdapter(storeArrayList)


        tempArrayList.addAll(storeArrayList)


        val adapter = RecyclerViewAdapter(tempArrayList)

        recyclerView.adapter = adapter


        adapter.setOnItemClickListener(object : RecyclerViewAdapter.onItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(this@StoreNavigation, StoreDescription::class.java)
                intent.putExtra("title", storeArrayList[position].itemTitle)
                intent.putExtra("image", storeArrayList[position].itemImage)
                intent.putExtra("details", storeArrayList[position].itemDetails)
                intent.putExtra("description", storeArrayList[position].itemDescription)
                intent.putExtra("operation", storeArrayList[position].opHour)
                intent.putExtra("address", storeArrayList[position].address)
                intent.putExtra("contact", storeArrayList[position].contactNum)
                intent.putExtra("bus", storeArrayList[position].busDetails)
                intent.putExtra("train", storeArrayList[position].trainDetails)
                intent.putExtra("web", storeArrayList[position].webDetails)
                startActivity(intent)

            }


        })


        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)


        navigationDrawer()

        updateNavHeader()

        val options = arrayOf("All", "Restaurant", "Salon", "Grocery Store")

        spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                tempArrayList.clear()
                val searchText = options[position]
                if (searchText.isNotEmpty()) {
                    storeArrayList.forEach {

                        if (it.category!!.equals(searchText)) {

                            tempArrayList.add(it)

                        }
                        else if (options[position].equals("All")) {

                            tempArrayList.addAll(storeArrayList)

                        }


                    }

                    recyclerView.adapter!!.notifyDataSetChanged()

                }

            }

        }



        chatbot.setOnClickListener {

            val chat = "https://dialogflow.cloud.google.com/#/agent/shoppingchatbot-alpv/integrations"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(("$chat")))
            startActivity(intent)


        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.action_filter_search)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    storeArrayList.forEach {

                        if (it.itemTitle!!.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            it.itemDetails!!.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            it.itemDescription!!.toLowerCase(Locale.getDefault()).contains(searchText) ) {

                            tempArrayList.add(it)

                        }


                    }

                    recyclerView.adapter!!.notifyDataSetChanged()

                }
                else {
                    tempArrayList.clear()
                    tempArrayList.addAll(storeArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
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
                val intent = Intent(this@StoreNavigation, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@StoreNavigation, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@StoreNavigation, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@StoreNavigation, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this@StoreNavigation, OnlineShopping::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@StoreNavigation, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_contact -> {
                val intent = Intent(this@StoreNavigation, Favourites::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




}