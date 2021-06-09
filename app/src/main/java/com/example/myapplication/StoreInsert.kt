package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class StoreInsert : AppCompatActivity() {

    private lateinit var uploadBtn : Button

    private lateinit var selectBtn : Button

    private lateinit var itemImage : ImageView

    private lateinit var ImageUri : Uri

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_insert)


        selectBtn = findViewById(R.id.select_image)
        itemImage = findViewById(R.id.item_image)
        uploadBtn = findViewById(R.id.save_to_database)


        selectBtn.setOnClickListener {
            selectImage()
        }

        uploadBtn.setOnClickListener {
            uploadImage()
        }



    }

    var selectedPhoto : Uri? = null

    private fun uploadImage() {

        var fileName = UUID.randomUUID().toString()
        var imageRef = FirebaseStorage.getInstance().reference.child("images/$fileName")

        imageRef.putFile(ImageUri)
            .addOnSuccessListener {

                Toast.makeText(this@StoreInsert, "Succesfull",Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this@StoreInsert, "Failed",Toast.LENGTH_SHORT).show()
            }

    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            itemImage.setImageURI(data?.data)

            ImageUri = data?.data!!
        }

    }

    private fun getUserData() {

        //database = FirebaseDatabase.getInstance().getReference("Store")

    }





}