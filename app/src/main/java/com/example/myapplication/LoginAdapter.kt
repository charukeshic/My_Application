package com.example.myapplication

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class LoginAdapter(fm: FragmentManager?, private val context: Context, var totalTabs: Int) : FragmentPagerAdapter(fm!!) {
    //class LoginAdapter(fm: FragmentManager?, private val context: Context, var totalTabs: Int) : FragmentPagerAdapter(fm!!, totalTabs)


    override fun getCount(): Int {
        return totalTabs

    }


    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> {
                LoginTabFragment()
            }
            1 -> {
                SignUpTabFragment()
            }
            else -> {
                LoginTabFragment()
            }

        }


    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "Login"
            }
            1 -> {
                return "Sign Up"
            }

        }
        return super.getPageTitle(position)

    }





}