package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MallAdapter(private val mallList : ArrayList<Mall>) : RecyclerView.Adapter<MallAdapter.MallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MallViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)

        return MallViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mallList.size
    }

    override fun onBindViewHolder(holder: MallViewHolder, position: Int) {
        val currentItem = mallList[position]

        holder.mallName.text = currentItem.title
        //holder.mallImage.setImageResource(currentItem.image)
    }


    class MallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mallImage : ImageView = itemView.findViewById(R.id.mall_image)
        val mallName : Button = itemView.findViewById(R.id.mall_name)

    }



}