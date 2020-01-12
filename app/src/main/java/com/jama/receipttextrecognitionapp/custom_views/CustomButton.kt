package com.jama.receipttextrecognitionapp.custom_views

import android.content.Context
import android.widget.Button
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.jama.receipttextrecognitionapp.R


class CustomButton: Button {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributes = this.context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomButton,
                0,
                0
            )
            setBackgroundResource(getButtonColor(attributes.getInteger(R.styleable.CustomButton_buttonColor, 0)))
        }
        setTextColor(ContextCompat.getColor(context, R.color.buttonTextColor))
    }

    private fun getButtonColor(value: Int): Int {
        return when(value) {
            0 -> R.drawable.green_button
            1 -> R.drawable.red_button
            2 -> R.drawable.blue_button
            else -> R.drawable.green_button
        }
    }

    override fun setPressed(pressed: Boolean) {
        if (pressed) setPadding(0, 25, 0, 0)
        else setPadding(0, 0, 0, 0)
        super.setPressed(pressed)
    }
}