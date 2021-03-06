package com.example.riskaadhita.helper

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.example.riskaadhita.helper.RecyclerViewClickListener

class RecyclerViewTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: RecyclerViewClickListener?) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child))
                }
            }
        })
    }
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildLayoutPosition(child))
            return true
        }
        return false
    }
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}