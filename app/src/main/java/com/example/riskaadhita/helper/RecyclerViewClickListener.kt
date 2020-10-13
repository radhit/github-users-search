package com.example.riskaadhita.helper

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}