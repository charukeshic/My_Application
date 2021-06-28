package com.example.myapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class OrderHistoryAdapter(private val productList : ArrayList<Order>) : RecyclerView.Adapter<OrderHistoryAdapter.CartItemViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_order_history_menu, parent, false)

        return CartItemViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.totalCost.text = String.format("%.2f", currentItem.orderPayment)
        holder.orderDate.text = currentItem.paymentDate.toString()



    }


    class CartItemViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val orderDate : TextView = itemView.findViewById(R.id.order_date)
        val totalCost : TextView = itemView.findViewById(R.id.order_total)


        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)


            }

        }



    }

}