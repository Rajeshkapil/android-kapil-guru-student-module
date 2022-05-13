package com.kapilguru.student.customUI

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kapilguru.student.R
import kotlinx.android.synthetic.main.loading_button.view.*


class CustomLoadingButton@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

     var  loadingCircularIndicator: CircularProgressIndicator


    init {
        val view = inflate(getContext(), R.layout.loading_button, this)
        loadingCircularIndicator = view.loadingCircularIndicator
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomLoadingButton)
            val title = resources.getText(typedArray.getResourceId(R.styleable.CustomLoadingButton_customText, R.string.login))
            loadingButtonText.text = title
            val loadingIndicator = typedArray.getBoolean(R.styleable.CustomLoadingButton_shouldLoadingShow, false)
            shouldShowLoading(loadingIndicator)

//            val drawable = typedArray.getDrawable(R.styleable.ImageTextHorizontal_image_src)
//            image.background = drawable
        }
    }



    fun shouldShowLoading(loadingIndicator: Boolean){
        loadingCircularIndicator.visibility = if(loadingIndicator) View.VISIBLE else  View.GONE
    }
}