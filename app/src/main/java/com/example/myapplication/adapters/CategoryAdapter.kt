package com.example.myapplication.adapters

import android.content.Intent
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.squareup.picasso.Picasso

class CategoryAdapter(private val categoryList : ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    private lateinit var mListener : onItemClickListener


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_category_item, parent, false)

        return CategoryViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        //Picasso.get().load(category.image).into(viewHolder.itemView.findViewById<ImageView>(R.id.mall_image))
        Picasso.get().load(currentItem.image).into(holder.categoryImage)

        holder.categoryName.text = currentItem.title



    }



    class CategoryViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        //val mallImage : ImageView = itemView.findViewById(R.id.mall_image)
        //val mallName : Button = itemView.findViewById(R.id.mall_name)
        val categoryName : TextView = itemView.findViewById(R.id.category_name)
        val categoryImage : ImageView = itemView.findViewById(R.id.mall_image)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }





}