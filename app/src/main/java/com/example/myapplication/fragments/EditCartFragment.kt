package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class EditCartFragment : Fragment() {

    var selectedIndex = 0
    val paymentMethods = arrayOf(
        "Cash On Delivery", "Maybank2u", "CIMB Online Banking", "Hong Leong Bank",
        "Online Banking - Public Bank", "Touch 'n Go eWallet"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_cart, container, false)




        return view
    }


}