package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Mall
import com.example.myapplication.MallItem
import com.example.myapplication.StoreProducts
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class StoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val mallRecyclerView = view.findViewById<RecyclerView>(R.id.mallList)

        val ref = FirebaseDatabase.getInstance().getReference("/Store")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    val mall = it.getValue(Mall::class.java)
                    if (mall != null) {
                        adapter.add(MallItem(mall))

                    }

                }

                adapter.setOnItemClickListener {item, view ->

                    val mallItem = item as MallItem

                    val intent = Intent(view.context, StoreProducts::class.java)
                    intent.putExtra(STORE_KEY, mallItem.mall.title)
                    startActivity(intent)


                }

                mallRecyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return view
    }


    companion object {
        val STORE_KEY = "STORE_KEY"
    }




}