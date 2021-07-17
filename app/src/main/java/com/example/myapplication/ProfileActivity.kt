package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    lateinit var saveChanges : Button
    lateinit var userImage : ImageView
    lateinit var selectPhoto : Button

    lateinit var email : TextView
    lateinit var username : EditText
    lateinit var mobile : EditText
    //lateinit var password : EditText
    lateinit var address : EditText

    private lateinit var ImageUri : Uri

    lateinit var layoutHeader : View
    lateinit var userName : TextView
    lateinit var userEmail : TextView
    lateinit var existingImage : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_profile)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)

        saveChanges = findViewById(R.id.signUpButton)
        selectPhoto = findViewById(R.id.select_image)
        userImage = findViewById(R.id.user_image2)

        email = findViewById(R.id.email1)
        username = findViewById(R.id.username1)
        mobile = findViewById(R.id.mobile1)
        //password = findViewById(R.id.pass1)
        address = findViewById(R.id.address1)


        selectPhoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        saveChanges.setOnClickListener {
            update()
            val intent = Intent(this@ProfileActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        updateNavHeader()

        navigationDrawer()


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("Profile Activity", "username: $uid")
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                email.setText(user?.email.toString())
                username.setText(user?.username.toString())
                mobile.setText(user?.mobileNo.toString())
                address.setText(user?.address.toString())
                //password.setText(user?.password.toString())
                Picasso.get().load(user?.image).into(userImage)

            }

        })



    }

    private fun getUserData() {
        val uid = intent.getStringExtra("userId").toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                email.setText(user?.email.toString())
                username.setText(user?.username.toString())
                mobile.setText(user?.mobileNo.toString())
                address.setText(user?.address.toString())
                //password.setText(user?.password.toString())
                //Picasso.get().load(user?.image).into(userImage)

            }

        })


    }

    private fun updateNavHeader() {

        layoutHeader = navigationView.getHeaderView(0)
        userName = layoutHeader.findViewById(R.id.username1)
        existingImage = layoutHeader.findViewById(R.id.user_image)
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
                Picasso.get().load(user?.image).into(existingImage)

            }

        })


    }


    private fun update() {

        if (isMobileNoChanged() or isNameChanged() or isAddressChanged()) {
            Toast.makeText(this@ProfileActivity, "Data has been updated",Toast.LENGTH_LONG).show()
        }

        else
            Toast.makeText(this@ProfileActivity, "No changes in data",Toast.LENGTH_LONG).show()

    }

    private fun isNameChanged(): Boolean {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        val userName = ref.child("username").toString()

        if(!userName.equals(username.text.toString())) {
            ref.child("username").setValue(username.text.toString())
            return true
        }
        else
            return false

    }

    private fun isMobileNoChanged(): Boolean {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        val userMobile = ref.child("mobileNo").toString()


        if(!userMobile.equals(mobile.text.toString())) {
            ref.child("mobileNo").setValue(mobile.text.toString())
            Log.d("Profile Activity", "mobile : ${mobile.text.toString()}")
            return true
        }
        else
            return false

    }

    private fun isAddressChanged(): Boolean {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        val userAddress = ref.child("address").toString()


        if(!userAddress.equals(address.text.toString())) {
            ref.child("address").setValue(address.text.toString())
            return true
        }

        else
            return false

    }

    private fun saveImage() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var fileName = uid.toString()
        var imageRef = FirebaseStorage.getInstance().reference.child("users/$fileName")

            imageRef.putFile(ImageUri)
                .addOnSuccessListener {
                    Log.d("Save", "Successful : ${it.metadata?.path}")
                    Toast.makeText(this@ProfileActivity, "Profile Picture Changed",Toast.LENGTH_SHORT).show()
                    imageRef.downloadUrl.addOnSuccessListener {
                        Log.d("Save", "Successful : $it")
                        //isImageChanged(it.toString())
                        saveChangesToDatabase(it.toString())

                    }

                }.addOnFailureListener {
                    Toast.makeText(this@ProfileActivity, "Failed",Toast.LENGTH_SHORT).show()
                }


    }

    private fun isImageChanged(image: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        val userImage = ref.child("image").toString()

        if(!userImage.equals(image)) {
            saveChangesToDatabase(image)
        }


    }

    private fun saveChangesToDatabase(image: String) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        ref.child("image").setValue(image)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            userImage.setImageURI(data?.data)

            ImageUri = data?.data!!
            saveImage()

        }

    }



    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_profile)

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
            //super.onBackPressed()
            val intent = Intent(this@ProfileActivity, Homepage::class.java)
            startActivity(intent)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this@ProfileActivity, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@ProfileActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@ProfileActivity, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@ProfileActivity, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@ProfileActivity, ActiveOrders::class.java)
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
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                val intent = Intent(this@ProfileActivity, ShareActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_contact -> {
                val intent = Intent(this@ProfileActivity, ContactInformation::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}