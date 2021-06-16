package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.SalesProduct
import com.squareup.picasso.Picasso

class SalesProductAdapter(private val productList : ArrayList<SalesProduct>) : RecyclerView.Adapter<SalesProductAdapter.SalesProductViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_sales_products, parent, false)

        return SalesProductViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: SalesProductViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.itemName
        holder.originalPrice.text = currentItem.originalPrice
        holder.discountedPrice.text = currentItem.discountedPrice
        holder.productDetails.text = currentItem.itemDetails
        holder.storeName.text = currentItem.store
        Picasso.get().load(currentItem.image).into(holder.productImage)
        //holder.mallImage.setImageResource(currentItem.image)
    }

    class SalesProductViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {


        val storeName : TextView = itemView.findViewById(R.id.mall_name)
        val productImage : ImageView = itemView.findViewById(R.id.mall_image)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val originalPrice : TextView = itemView.findViewById(R.id.product_originalPrice)
        val discountedPrice : TextView = itemView.findViewById(R.id.product_discountedPrice)
        val productDetails : TextView = itemView.findViewById(R.id.item_details)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }

}