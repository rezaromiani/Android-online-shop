package com.example.nikestore.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.nikestore.R

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var onBackButtonClickListener: View.OnClickListener? = null
        set(value) {
            field = value
            findViewById<ImageView>(R.id.backBtn).setOnClickListener(onBackButtonClickListener)
        }

    init {
        inflate(context, R.layout.view_toolbar, this)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.NikeToolbar)
            val title = a.getString(R.styleable.NikeToolbar_nt_title)
            if (title != null && title.isNotEmpty())
                findViewById<TextView>(R.id.toolbarTitleTv).text = title
            a.recycle()
        }
    }
}