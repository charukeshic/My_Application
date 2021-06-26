package com.example.myapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class CartItemAdapter(private val productList : ArrayList<CartItem>) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_cart_item, parent, false)

        return CartItemViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.itemName
        holder.productPrice.text = String.format("%.2f", currentItem.price)
        holder.productDetails.text = currentItem.itemDetails
        holder.storeName.text = currentItem.store
        Picasso.get().load(currentItem.image).into(holder.productImage)
        //holder.mallImage.setImageResource(currentItem.image)
        holder.productQuantity.text = currentItem.quantity.toString()
        holder.totalCost.text = String.format("%.2f", currentItem.total)
        holder.editQuantity.number = currentItem.quantity.toString()

        holder.editQuantity.setOnValueChangeListener { view, oldValue, newValue ->
            currentItem.quantity = newValue

            currentItem.total = currentItem.price * newValue

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            Log.d("Profile Activity", "username: $uid")
            val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Cart")

            val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

            ref.child("$productName").child("quantity").setValue(newValue)
            ref.child("$productName").child("total").setValue(currentItem.total)


        }


    }


    class CartItemViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {


        val storeName : TextView = itemView.findViewById(R.id.mall_name)
        val productImage : ImageView = itemView.findViewById(R.id.mall_image)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val productPrice : TextView = itemView.findViewById(R.id.product_price)
        val productDetails : TextView = itemView.findViewById(R.id.item_details)
        val productQuantity : TextView = itemView.findViewById(R.id.quantity)
        val totalCost : TextView = itemView.findViewById(R.id.total)
        val editQuantity : ElegantNumberButton = itemView.findViewById(R.id.qty_btn)


        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)


            }

        }



    }

}