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


class StoreInsertDetails : AppCompatActivity() {

    private lateinit var uploadBtn : Button

    private lateinit var selectBtn : Button

    private lateinit var itemImage : ImageView

    private lateinit var ImageUri : Uri

    lateinit var itemTitle : EditText
    lateinit var itemDetails : EditText
    lateinit var itemDescription : EditText
    lateinit var itemName : EditText

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_insert_details)


        selectBtn = findViewById(R.id.select_image)
        itemImage = findViewById(R.id.item_image)
        uploadBtn = findViewById(R.id.save_to_database)
        itemTitle = findViewById(R.id.item_title)
        itemDetails = findViewById(R.id.item_details)
        itemDescription = findViewById(R.id.item_description)
        itemName = findViewById(R.id.item_name)


        selectBtn.setOnClickListener {
            selectImage()
        }

        uploadBtn.setOnClickListener {
            uploadImage()
        }



    }


    private fun uploadImage() {

        var fileName = itemTitle.text.toString()
        var imageRef = FirebaseStorage.getInstance().reference.child("/category/$fileName")

        imageRef.putFile(ImageUri)
            .addOnSuccessListener {
                Log.d("Save", "Succesfull : ${it.metadata?.path}")
                Toast.makeText(this@StoreInsertDetails, "Succesfull",Toast.LENGTH_SHORT).show()
                imageRef.downloadUrl.addOnSuccessListener {
                    Log.d("Save", "Succesfull : $it")
                    //saveStoreToDatabase(it.toString())
                }

            }.addOnFailureListener {
                Toast.makeText(this@StoreInsertDetails, "Failed",Toast.LENGTH_SHORT).show()
            }


    }

    private fun saveStoreToDatabase(storeImageUrl : String) {
        val uid = itemDetails.text.toString()
        //val item = itemDescription.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Store/99 Sppedmart")
        //val ref2 = FirebaseDatabase.getInstance().getReference("/Store/Tesco").child("categoriesList").child("$uid")
        //val ref3 = FirebaseDatabase.getInstance().getReference("/Store/Giant").child("categoriesList").child("$uid")
        //val ref4 = FirebaseDatabase.getInstance().getReference("/Store/Cold Storage").child("categoriesList").child("$uid")
        //val ref1 = FirebaseDatabase.getInstance().getReference("Store").child("AEON").child("categories").child("Vegetables").child("$item")
        //val ref2 = FirebaseDatabase.getInstance().getReference("Store").child("Tesco").child("categories").child("Vegetables").child("$item")
        //val ref3 = FirebaseDatabase.getInstance().getReference("Store").child("Giant").child("categories").child("Vegetables").child("$item")
        //val ref4 = FirebaseDatabase.getInstance().getReference("Store").child("Cold Storage").child("categories").child("Vegetables").child("$item")

        val store = StoreCategories(storeImageUrl)

        ref.setValue(store)
            .addOnSuccessListener {
                Toast.makeText(this@StoreInsertDetails, "Store added",Toast.LENGTH_SHORT).show()
            }

        //ref1.setValue(store)
        //ref2.setValue(store)
        //ref3.setValue(store)
        //ref4.setValue(store)

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

data class StoreCategories (var image : String


)


