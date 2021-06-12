package com.example.myapplication.adapters

import android.content.Intent
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

        holder.categoryName.text = currentItem.title
        val selected = currentItem.title
        //holder.mallImage.setImageResource(currentItem.image)


    }



    class CategoryViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        //val mallImage : ImageView = itemView.findViewById(R.id.mall_image)
        //val mallName : Button = itemView.findViewById(R.id.mall_name)
        val categoryName : TextView = itemView.findViewById(R.id.category_name)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }

    companion object {
        var CATEGORY = "CATEGORY"
    }




}