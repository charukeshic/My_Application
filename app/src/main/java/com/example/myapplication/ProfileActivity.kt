package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    lateinit var saveChanges : Button
    lateinit var userImage : ImageView
    lateinit var selectPhoto : Button

    lateinit var email : EditText
    lateinit var username : EditText
    lateinit var mobile : EditText
    lateinit var password : EditText
    lateinit var address : EditText

    private lateinit var ImageUri : Uri

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
        userImage = findViewById(R.id.user_image)

        email = findViewById(R.id.email1)
        username = findViewById(R.id.username1)
        mobile = findViewById(R.id.mobile1)
        password = findViewById(R.id.pass1)
        address = findViewById(R.id.address1)

        selectPhoto.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        saveChanges.setOnClickListener {
            //saveImage()

            val intent = Intent(this@ProfileActivity, Homepage::class.java)
            startActivity(intent)
        }

        //getUserData()


        navigationDrawer()

    }

    private fun getUserData() {
        val mobileNum = intent.getStringExtra("mobile").toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$mobileNum")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)

            }

        })



    }

    private fun saveChangesToDatabase(userImage : String) {
        val userEmail = email.text.toString()
        val userName = username.text.toString()
        val passWord = password.text.toString()
        val mobileNum = mobile.text.toString()
        val userAddress = address.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$mobileNum")

        val user = User(userEmail, userName, passWord, mobileNum, userAddress, userImage)

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Store added",Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImage() {
        var fileName = username.text.toString()
        var imageRef = FirebaseStorage.getInstance().reference.child("users/$fileName")

        imageRef.putFile(ImageUri)
            .addOnSuccessListener {
                Log.d("Save", "Successful : ${it.metadata?.path}")
                Toast.makeText(this@ProfileActivity, "Profile Updated",Toast.LENGTH_SHORT).show()
                imageRef.downloadUrl.addOnSuccessListener {
                    Log.d("Save", "Successful : $it")
                    //saveChangesToDatabase(it.toString())
                }

            }.addOnFailureListener {
                Toast.makeText(this@ProfileActivity, "Failed",Toast.LENGTH_SHORT).show()
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            userImage.setImageURI(data?.data)

            ImageUri = data?.data!!
        }

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
                val intent = Intent(this@ProfileActivity, Homepage::class.java)
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