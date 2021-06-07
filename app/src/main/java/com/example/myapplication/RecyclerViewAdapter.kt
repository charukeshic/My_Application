package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val storeList : ArrayList<Store>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: onItemClickListener) {

        mListener = listener

    }


    inner class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var image : ImageView
        var textTitle : TextView
        var textDesc : TextView

        init {
            image = itemView.findViewById(R.id.item_image)
            textTitle = itemView.findViewById(R.id.item_title)
            textDesc = itemView.findViewById(R.id.item_details)


            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_model,parent,false)
        return ViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = storeList [position]

        holder.textTitle.text = currentItem.itemTitle
        holder.textDesc.text = currentItem.itemDetails
        currentItem?.itemImage?.let { holder.image.setImageResource(it) }



    }

    override fun getItemCount(): Int {
        return storeList.size
    }




}