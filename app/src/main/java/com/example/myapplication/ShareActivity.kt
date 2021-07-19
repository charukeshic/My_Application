package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton

class ShareActivity : AppCompatActivity() {

    lateinit var facebook : AppCompatImageButton
    lateinit var instagram : AppCompatImageButton
    lateinit var twitter : AppCompatImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        facebook = findViewById(R.id.facebookBtn)
        instagram = findViewById(R.id.instagramBtn)
        twitter = findViewById(R.id.twitterBtn)

        facebook.setOnClickListener {

            val installed  : Boolean = appInstalledOrNot("com.facebook.katana")

            if (installed) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
                startActivity(intent)


            }
            else {
                Log.d("Start", "what happen to this function")
                Toast.makeText(this, "Grab app not installed", Toast.LENGTH_SHORT)
            }

        }

        instagram.setOnClickListener {

            val installed  : Boolean = appInstalledOrNot("com.instagram.android")

            if (installed) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
                startActivity(intent)


            }
            else {
                Log.d("Start", "what happen to this function")
                Toast.makeText(this, "Grab app not installed", Toast.LENGTH_SHORT)
            }

        }

        twitter.setOnClickListener {

            val installed  : Boolean = appInstalledOrNot("com.twitter.android")

            if (installed) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/login?lang=en"))
                startActivity(intent)


            }
            else {
                Log.d("Start", "what happen to this function")
                Toast.makeText(this, "Grab app not installed", Toast.LENGTH_SHORT)
            }

        }



    }

    private fun appInstalledOrNot(url : String): Boolean {
        val packageManager = packageManager
        val appInstalled : Boolean
        appInstalled = try {
            Log.d("Start", "nvr reach this function")
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("Start", "done this function")
            false
        }
        return appInstalled

    }


}