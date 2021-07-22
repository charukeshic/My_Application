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
import com.google.firebase.database.DatabaseReference
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
    lateinit var itemDet : EditText

    private lateinit var ref : DatabaseReference

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
        itemDet = findViewById(R.id.item_name)


        selectBtn.setOnClickListener {
            //selectImage()
        }

        uploadBtn.setOnClickListener {
            saveStoreToDatabase()
//            itemTitle = findViewById(R.id.item_title)
//            itemDetails = findViewById(R.id.item_details)
//            itemDescription = findViewById(R.id.item_description)
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

    private fun saveStoreToDatabase() {
//        val uid = itemTitle.text.toString()
//        val itemName = itemDetails.text.toString()
//        val originalPrice = itemDescription.text.toString()
//        val discountedPrice = itemDet.text.toString()

        val storeNames = getString(R.string.title_18)
        val description = getString(R.string.description_18)
        val details = getString(R.string.details_18)
        val operationHour = getString(R.string.opHour_18)
        val address = getString(R.string.addr_18)
        val contactNum = getString(R.string.contact_18)
        val busDetails = getString(R.string.bus_18)
        val trainDetails = getString(R.string.train_18)
        val website = getString(R.string.web_18)
        val promotion = getString(R.string.promo_1)

        ref = FirebaseDatabase.getInstance().getReference("/Physical Store").child("Bakery")
        //val ref2 = FirebaseDatabase.getInstance().getReference("/Store/Tesco").child("categoriesList").child("$uid")
        //val ref3 = FirebaseDatabase.getInstance().getReference("/Store/Giant").child("categoriesList").child("$uid")
        //val ref4 = FirebaseDatabase.getInstance().getReference("/Store/Cold Storage").child("categoriesList").child("$uid")
        //val ref1 = FirebaseDatabase.getInstance().getReference("Store").child("AEON").child("categories").child("Vegetables").child("$item")
        //val ref2 = FirebaseDatabase.getInstance().getReference("Store").child("Tesco").child("categories").child("Vegetables").child("$item")
        //val ref3 = FirebaseDatabase.getInstance().getReference("Store").child("Giant").child("categories").child("Vegetables").child("$item")
        //val ref4 = FirebaseDatabase.getInstance().getReference("Store").child("Cold Storage").child("categories").child("Vegetables").child("$item")


        val store = PhysicalStore(storeNames, description, details, operationHour, address, contactNum, busDetails, trainDetails, website, promotion)

        ref.child("$storeNames").setValue(store)

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

data class PhysicalStore (var storeName : String, var description : String, var details : String, var operationHour : String, var address : String,
                            var contactNum : String, var busDetails : String, var trainDetails : String, var website : String, var promotion : String
)


