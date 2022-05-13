package com.kapilguru.student.customUI

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.kapilguru.student.R
import kotlinx.android.synthetic.main.custom_key_value_view.view.*

class CircleImageText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.custom_circle_image_txt, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircleImageText)
            val title = resources.getText(typedArray.getResourceId(R.styleable.CircleImageText_text_in_circle, R.string.batch_time))
            text_value.text = title
        }
    }
}