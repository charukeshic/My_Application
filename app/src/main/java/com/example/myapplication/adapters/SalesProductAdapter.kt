package com.example.myapplication.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ProductDetails
import com.example.myapplication.R
import com.example.myapplication.SalesProduct
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.TwitterAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SalesProductAdapter(private val productList : ArrayList<SalesProduct>) : RecyclerView.Adapter<SalesProductAdapter.SalesProductViewHolder>() {

    private lateinit var mListener : onItemClickListener
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_sales_products, parent, false)
        firebaseAnalytics = Firebase.analytics
        return SalesProductViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: SalesProductViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.itemName
        holder.originalPrice.text = currentItem.originalPrice
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        holder.discountedPrice.text = currentItem.discountedPrice
        holder.productDetails.text = currentItem.itemDetails
        holder.storeName.text = currentItem.store
        Picasso.get().load(currentItem.image).into(holder.productImage)
        holder.priceText.setPaintFlags(holder.priceText.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
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

                val prodStore = currentItem.store.toString()
                val prodImage = currentItem.image.toString()
                val prodPrice = currentItem.discountedPrice.toString()
                val prodDetails = currentItem.itemDetails.toString()

                val prod = ProductDetails(currentItem.itemName, prodStore, prodImage, prodPrice, prodDetails)

                ref.child("$productName").setValue(prod)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "$productName added to Favourites", Toast.LENGTH_SHORT).show()
                    }

                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                    param(FirebaseAnalytics.Param.ITEM_ID, productName)
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



    class SalesProductViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {


        val storeName : TextView = itemView.findViewById(R.id.mall_name)
        val productImage : ImageView = itemView.findViewById(R.id.mall_image)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val originalPrice : TextView = itemView.findViewById(R.id.product_originalPrice)
        val discountedPrice : TextView = itemView.findViewById(R.id.product_discountedPrice)
        val productDetails : TextView = itemView.findViewById(R.id.item_details)
        val priceText : TextView = itemView.findViewById(R.id.text_price)
        val addToFav : LinearLayout = itemView.findViewById(R.id.add_to_fav)
        val favBtn : ImageView = itemView.findViewById(R.id.fav_btn)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)

            }

        }


    }

}