package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Product
import com.example.myapplication.R
import com.example.myapplication.adapters.ProductAdapter
import com.google.firebase.database.*

class ProductFragment(val mallName : String, val categoryName : String, val productName : String) : Fragment() {

    //private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var productRecyclerView: RecyclerView

    //private lateinit var dbrefCategories : DatabaseReference
    private lateinit var dbrefProducts : DatabaseReference
    //private lateinit var dbrefgetProducts : DatabaseReference
    //private lateinit var categoryArrayList: ArrayList<Category>
    private lateinit var productArrayList: ArrayList<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        productRecyclerView = view.findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = GridLayoutManager(productRecyclerView.context, 2)

        productArrayList = arrayListOf<Product>()


        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$mallName").
        child("categories").child("$categoryName").child("$productName")


        dbrefProducts.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    val product = snapshot.getValue(Product::class.java)
                    product?.store = mallName.toString()
                    productArrayList.add(product!!)

                    var adapter = ProductAdapter(productArrayList)
                    productRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            val productName = productArrayList[position].itemName
                            Toast.makeText(this@ProductFragment.context, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                        }


                    })


                }

            }


        })



        return view
    }


}