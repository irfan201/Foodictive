package com.example.foodictive.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.foodictive.R
import com.example.foodictive.emailValid

class AllEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var buttonClearImage: Drawable
    private lateinit var emailImage: Drawable
    private lateinit var passwordImage: Drawable
    private lateinit var enabledBackground: Drawable
    private var isEmail: Boolean = false
    private var isPassword: Boolean = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs, defStyleAttr)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setPadding(32, 32, 32, 32)
        background = enabledBackground
        gravity = Gravity.CENTER_VERTICAL
        compoundDrawablePadding = 16
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AllEditText, defStyleAttr, 0)

        isEmail = a.getBoolean(R.styleable.AllEditText_email, false)
        isPassword = a.getBoolean(R.styleable.AllEditText_password, false)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.edit_txt_bg) as Drawable
        buttonClearImage = ContextCompat.getDrawable(context, R.drawable.clear_ic) as Drawable
        emailImage = ContextCompat.getDrawable(context, R.drawable.email_ic) as Drawable
        passwordImage = ContextCompat.getDrawable(context, R.drawable.lock_ic) as Drawable

        if (isEmail) {
            setButtonDrawables(startOfTheText = emailImage)
        } else if (isPassword) {
            setButtonDrawables(startOfTheText = passwordImage)
        }

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = s.toString()
                val emailError = resources.getString(R.string.email_inv)
                val passwordError = resources.getString(R.string.pass_inv)

                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
                error =
                    if (isPassword && input.length < 6 && input.isNotEmpty()) {
                        passwordError
                    } else if (isEmail && !input.emailValid()) {
                        emailError
                    } else {
                        null
                    }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        a.recycle()
    }

    private fun showClearButton() {
        when {
            isEmail -> setButtonDrawables(
                startOfTheText = emailImage,
                endOfTheText = buttonClearImage
            )
            isPassword -> setButtonDrawables(
                startOfTheText = passwordImage,
                endOfTheText = buttonClearImage
            )
            else -> setButtonDrawables(endOfTheText = buttonClearImage)
        }
    }

    private fun hideClearButton() {
        when {
            isEmail -> setButtonDrawables(startOfTheText = emailImage)
            isPassword -> setButtonDrawables(startOfTheText = passwordImage)
            else -> setButtonDrawables()
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (buttonClearImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - buttonClearImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        buttonClearImage =
                            ContextCompat.getDrawable(context, R.drawable.clear_ic) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        buttonClearImage =
                            ContextCompat.getDrawable(context, R.drawable.clear_ic) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}