package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val mallRecyclerView = view.findViewById<RecyclerView>(R.id.categoryList)

        val ref = FirebaseDatabase.getInstance().getReference("/Store").child("99 Sppedmart").child("categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                snapshot.children.forEach {
                    val category = it.getValue(Category::class.java)
                    if (category != null) {
                        adapter.add(CategoryItem(category))

                    }
                }

                adapter.setOnItemClickListener {item, view ->

                    val categoryItem = item as CategoryItem

                    val intent = Intent(view.context, StoreProducts::class.java)
                    intent.putExtra(CATEGORY_KEY, categoryItem.category.title)
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
        val CATEGORY_KEY = "Category_Key"
    }


}