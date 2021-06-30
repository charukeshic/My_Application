package com.example.myapplication.adapters

import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext

class ProductDetailsAdapter(private val productList : ArrayList<ProductDetails>) : RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_product_details, parent, false)

        return ProductDetailsViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productName.text = currentItem.itemName
        holder.productPrice.text = currentItem.price
        holder.productDetails.text = currentItem.itemDetails
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

        holder.addEvent.setOnClickListener {

            val title = "Reminder to buy ".plus(currentItem.itemName.toString()).plus(" from ").plus(currentItem.store.toString())

            val intent = Intent(Intent.ACTION_INSERT)
            intent.data = CalendarContract.Events.CONTENT_URI
            intent.putExtra(CalendarContract.Events.TITLE, title)

            if(intent.resolveActivity(holder.itemView.context.packageManager) != null){
                //addTheEvent(currentItem!!)
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Events")

                val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")
                ref.child("$productName").setValue(currentItem)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Creating event for $productName", Toast.LENGTH_SHORT).show()
                    }

                holder.itemView.context.startActivity(intent)
            }
            else{
                Toast.makeText(holder.itemView.context, "There is no app that support this action", Toast.LENGTH_SHORT).show();
            }

        }

        holder.addToCart.setOnClickListener {

            val cartItemName = currentItem.itemName
            val cartItemStore = currentItem.store
            val cartItemImage = currentItem.image
            val cartItemDetails = currentItem.itemDetails
            val cartItemQuantity = 1
            val cartItemPrice = String.format("%.2f", currentItem.price.toDouble()).toDouble()
            val cartItemTotal = String.format("%.2f", (cartItemQuantity * cartItemPrice)).toDouble()

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid").child("Cart")

            val productName = currentItem.itemName.toString().plus("(").plus(currentItem.store).plus(")")

            val cartItem = CartItem(cartItemName, cartItemStore, cartItemImage, cartItemPrice, cartItemDetails, cartItemQuantity, cartItemTotal)

            ref.child("$productName").setValue(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "$productName added to cart", Toast.LENGTH_SHORT).show()
                }

        }


    }




    class ProductDetailsViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {


        val storeName : TextView = itemView.findViewById(R.id.mall_name)
        val productImage : ImageView = itemView.findViewById(R.id.mall_image)
        val productName : TextView = itemView.findViewById(R.id.product_name)
        val productPrice : TextView = itemView.findViewById(R.id.product_price)
        val productDetails : TextView = itemView.findViewById(R.id.item_details)
        val addEvent : LinearLayout = itemView.findViewById(R.id.create_event)
        val addToFav : LinearLayout = itemView.findViewById(R.id.add_to_fav)
        val addToCart : LinearLayout = itemView.findViewById(R.id.add_to_cart)
        val favBtn : ImageView = itemView.findViewById(R.id.fav_btn)

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)


            }

        }



    }

}