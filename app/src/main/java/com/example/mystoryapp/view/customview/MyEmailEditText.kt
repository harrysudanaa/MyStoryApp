package com.example.mystoryapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class MyEmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (!isValidEmail(text)) {
            setError("Email must be valid", null)
        } else {
            error = null
        }
    }

    private fun isValidEmail(text: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
    }
}