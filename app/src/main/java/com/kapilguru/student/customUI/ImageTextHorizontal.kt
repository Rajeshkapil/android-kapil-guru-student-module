package com.kapilguru.student.customUI

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.kapilguru.student.R
import kotlinx.android.synthetic.main.image_text_horizontal.view.*

class ImageTextHorizontal @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.image_text_horizontal, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ImageTextHorizontal)
            val title = resources.getText(typedArray.getResourceId(R.styleable.ImageTextHorizontal_title, R.string.batch_time))
            text.text = title
            val drawable = typedArray.getDrawable(R.styleable.ImageTextHorizontal_image_src)
            image.background = drawable
        }
    }
}