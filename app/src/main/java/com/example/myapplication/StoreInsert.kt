package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class StoreInsert : AppCompatActivity() {

    private lateinit var uploadBtn : Button

    private lateinit var selectBtn : Button

    private lateinit var itemImage : ImageView

    private lateinit var ImageUri : Uri

    lateinit var itemTitle : EditText
    lateinit var itemDetails : EditText

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_insert)


        selectBtn = findViewById(R.id.select_image)
        itemImage = findViewById(R.id.item_image)
        uploadBtn = findViewById(R.id.save_to_database)
        itemTitle = findViewById(R.id.item_title)
        itemDetails = findViewById(R.id.item_details)



        selectBtn.setOnClickListener {
            selectImage()
        }

        uploadBtn.setOnClickListener {
            uploadImage()
        }



    }


    private fun uploadImage() {

        var fileName = itemTitle.text.toString()
        var imageRef = FirebaseStorage.getInstance().reference.child("sales/$fileName")

        imageRef.putFile(ImageUri)
            .addOnSuccessListener {
                Log.d("Save", "Succesfull : ${it.metadata?.path}")
                Toast.makeText(this@StoreInsert, "Succesfull",Toast.LENGTH_SHORT).show()
                imageRef.downloadUrl.addOnSuccessListener {
                    Log.d("Save", "Succesfull : $it")
                    //saveStoreToDatabase(it.toString())
                }

            }.addOnFailureListener {
                Toast.makeText(this@StoreInsert, "Failed",Toast.LENGTH_SHORT).show()
            }


    }

    private fun saveStoreToDatabase(storeImageUrl : String) {
        val uid = itemTitle.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Store").child("$uid")

        val store = Mall(uid, storeImageUrl, itemDetails.text.toString())

        ref.setValue(store)
            .addOnSuccessListener {
                Toast.makeText(this@StoreInsert, "Store added",Toast.LENGTH_SHORT).show()
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


}


