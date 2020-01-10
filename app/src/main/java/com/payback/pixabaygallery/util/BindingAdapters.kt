package com.payback.pixabaygallery.util

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.payback.pixabaygallery.R
import com.squareup.picasso.Picasso


class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl", "alternativeImageUrl")
        fun loadImage(view: ImageView, imageUrl: String, alternativeImageUrl: String) {
            if (imageUrl != "") {
                val builder = Picasso.Builder(view.context)
                builder.listener { picasso, _, _ ->
                    if (alternativeImageUrl != ""){
                        picasso.load(alternativeImageUrl)
                            .into(view)
                    }
                }

                builder.build().load(imageUrl).into(view)

            } else {
                Picasso.get()
                    .load(R.drawable.placeholder)
                    .into(view)
            }
        }


        @JvmStatic
        @BindingAdapter("countAnimation")
        fun countAnimation(textView: TextView, maxValue: Int) {
            val animator = ValueAnimator.ofInt(0, maxValue)
            animator.duration = 2000
            animator.addUpdateListener { animation ->
                textView.text = animation.animatedValue.toString()
            }
            animator.start()
        }

        @JvmStatic
        @BindingAdapter("fadeAnimation")
        fun fadeAnimation(textView: View, duration: Long) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = duration
            textView.animation = fadeIn
        }
    }
}
