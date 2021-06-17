package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.adapters.SalesProductAdapter
import com.google.firebase.database.*

class RecommendationFragment : Fragment() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var dbrefStore : DatabaseReference
    private lateinit var dbrefProducts : DatabaseReference
    private lateinit var productArrayList: ArrayList<SalesProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recommendation, container, false)

        productRecyclerView = view.findViewById(R.id.product_recyclerView)
        productRecyclerView.setHasFixedSize(true)
        productRecyclerView.layoutManager = GridLayoutManager(productRecyclerView.context,2)

        productArrayList = arrayListOf<SalesProduct>()

        dbrefStore = FirebaseDatabase.getInstance().getReference("/Store")


        dbrefStore.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    for(store in snapshot.children) {
                        val store = store.getValue(Mall::class.java)
                        val storeName = store!!.title.toString()

                        dbrefProducts = FirebaseDatabase.getInstance().getReference("/Store").child("$storeName").
                        child("sales")

                        dbrefProducts.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()) {
                                    for(productSnapshot in snapshot.children) {

                                        val product = productSnapshot.getValue(SalesProduct::class.java)
                                        product?.store = storeName.toString()
                                        productArrayList.add(product!!)

                                        var adapter = SalesProductAdapter(productArrayList)
                                        productRecyclerView.adapter = adapter
                                        adapter.setOnItemClickListener(object : SalesProductAdapter.onItemClickListener{

                                            override fun onItemClick(position: Int) {

                                                val productName = productArrayList[position].itemName
                                                val mallName = productArrayList[position].store

                                                Toast.makeText(this@RecommendationFragment.context, "You clicked on $productName", Toast.LENGTH_SHORT).show()

                                                val intent = Intent(this@RecommendationFragment.context, GetSalesProductDetails::class.java)
                                                intent.putExtra("SALE_MALL", mallName)
                                                intent.putExtra("SALE_PRODUCT", productName)
                                                startActivity(intent)

                                            }

                                        })

                                    }

                                }
                            }

                        })



                    }



                }

            }


        })






        return view
    }


}