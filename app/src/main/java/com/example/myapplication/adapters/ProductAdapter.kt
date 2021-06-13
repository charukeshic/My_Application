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

class ProductAdapter(private val productList : ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    private lateinit var mListener : onItemClickListener


    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_products, parent, false)

        return ProductViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.itemName
        holder.productPrice.text = currentItem.itemDetails
        //holder.mallImage.setImageResource(currentItem.image)


    }



    class ProductViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        //val mallImage : ImageView = itemView.findViewById(R.id.mall_image)
        //val mallName : Button = itemView.findViewById(R.id.mall_name)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val productPrice : TextView = itemView.findViewById(R.id.product_price)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }






}