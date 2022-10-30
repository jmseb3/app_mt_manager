package com.wonddak.mtmanger.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.wonddak.mtmanger.R
import java.text.DecimalFormat

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("bindDecText")
    fun TextView.bindDec(getText: Int) {
        val dec = DecimalFormat("#,###")
        (dec.format(getText).toString()).also { this.text = it }

    }

    @JvmStatic
    @BindingAdapter("bindSetArrow")
    fun ImageView.setArrow(up: Boolean) {
        val resId = if (up) {
            R.drawable.ic_baseline_arrow_drop_up_24
        } else {
            R.drawable.ic_baseline_arrow_drop_down_24
        }
        this.setImageDrawable(ContextCompat.getDrawable(this.context,resId))
    }

    @JvmStatic
    @BindingAdapter("bindSetArrowDim")
    fun View.setArrowDim(up: Boolean) {
        if (up) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("bindUrlImage")
    fun ImageView.bindUrlImage(url: String) {
        if (url.isEmpty()) return
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("bindViewDim")
    fun View.bindViewDim(value: String?) {
        if (value.isNullOrEmpty()) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }
}
