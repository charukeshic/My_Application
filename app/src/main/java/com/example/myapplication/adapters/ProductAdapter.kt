package com.example.myapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

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
        holder.productPrice.text = currentItem.price
        //holder.productDetails.text = currentItem.itemDetails
        holder.storeName.text = currentItem.store
        Picasso.get().load(currentItem.image).into(holder.productImage)
        //holder.mallImage.setImageResource(currentItem.image)

        holder.addToFav.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            Log.d("Profile Activity", "username: $uid")
            val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites")

            val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

            ref.child("$productName").setValue(currentItem)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                }

        }

    }



    class ProductViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val productName : TextView = itemView.findViewById(R.id.product_name)
        val productPrice : TextView = itemView.findViewById(R.id.product_price)
        //val productDetails : TextView = itemView.findViewById(R.id.item_details)
        val storeName : TextView = itemView.findViewById(R.id.mall_name)
        val productImage : ImageView = itemView.findViewById(R.id.mall_image)

        val addToFav : LinearLayout = itemView.findViewById(R.id.add_to_fav)


        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }






}