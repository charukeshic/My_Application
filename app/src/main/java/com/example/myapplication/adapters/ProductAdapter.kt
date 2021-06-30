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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ProductAdapter(private val productList : ArrayList<ProductDetails>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


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

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites").child("$productName")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    holder.favBtn.setImageResource(R.drawable.fav_icon_pink)
                }
                else
                    holder.favBtn.setImageResource(R.drawable.heart_icon)

            }

        })

        holder.addToFav.setOnClickListener {

            if(holder.favBtn.drawable.constantState == holder.favBtn.resources.getDrawable(R.drawable.heart_icon).constantState){

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                Log.d("Profile Activity", "username: $uid")
                val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites")

                val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

                ref.child("$productName").setValue(currentItem)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                    }

                holder.favBtn.setImageResource(R.drawable.fav_icon_pink)
            }
            else if(holder.favBtn.drawable.constantState == holder.favBtn.resources.getDrawable(R.drawable.fav_icon_pink).constantState){

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                Log.d("Profile Activity", "username: $uid")
                val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Favourites")

                val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

                ref.child("$productName").removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "$productName removed from Favourites", Toast.LENGTH_SHORT).show()
                    }


                holder.favBtn.setImageResource(R.drawable.heart_icon)
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
        val favBtn : ImageView = itemView.findViewById(R.id.fav_btn)


        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }






}