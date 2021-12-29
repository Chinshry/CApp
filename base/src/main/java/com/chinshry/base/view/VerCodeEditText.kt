package com.chinshry.base.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.example.base.R

/**
 * Created by chinshry on 2021/12/29.
 * File Name: VerCodeEditText.kt
 * Describe：验证码框
 */
class VerCodeEditText constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    VerCodeLayout(context, attrs, defStyleAttr) {

    private var mLinearLayout: LinearLayout? = null

    private val mCount: Int
    private var mNormalBackground: Drawable?
    private val mFocusedBackground: Drawable?
    private val mWidth: Int
    private val mHeight: Int
    private val mTextSize: Float

    @ColorInt
    private val mTextColor: Int
    private val mMargin: Int

    init {
        val etStyle = context.obtainStyledAttributes(attrs, R.styleable.VerCodeEditText)
        mCount = etStyle.getInt(R.styleable.VerCodeEditText_verCodeCount, 0)
        mNormalBackground = etStyle.getDrawable(R.styleable.VerCodeEditText_verCodeNormalBackground)
        mFocusedBackground = etStyle.getDrawable(R.styleable.VerCodeEditText_verCodeFocusedBackground)
        mTextSize = etStyle.getDimensionPixelSize(R.styleable.VerCodeEditText_verCodeTextSize, 0).toFloat()
        mTextColor = etStyle.getColor(R.styleable.VerCodeEditText_verCodeTextColor, Color.BLACK)
        mWidth = etStyle.getDimension(R.styleable.VerCodeEditText_verCodeWidth, 0f).toInt()
        mHeight = etStyle.getDimension(R.styleable.VerCodeEditText_verCodeHeight, 0f).toInt()
        mMargin = etStyle.getDimension(R.styleable.VerCodeEditText_verCodeMargin, 0f).toInt()
        etStyle.recycle()
        createTextViews()
        initEditText()
    }

    private fun createTextViews() {
        if (mCount <= 0) {
            return
        }

        mLinearLayout = LinearLayout(context)
        mLinearLayout!!.orientation = LinearLayout.HORIZONTAL
        mLinearLayout!!.gravity = Gravity.CENTER
        mLinearLayout!!.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        for (i in 0 until mCount) {
            val mTextView = TextView(context)
            setDefault(mTextView)
            val params = marginLayoutParams
            if (i == 0) {
                setBackground(mTextView, mFocusedBackground)
            } else {
                setBackground(mTextView, mNormalBackground)
            }
            setPadding(mTextView)
            mLinearLayout!!.addView(mTextView, params)
        }
        addView(mLinearLayout)
    }

    private fun initEditText() {
        val editTextWidth = mCount * (mWidth + mMargin * 2)
        val layoutParams = LayoutParams(editTextWidth, mHeight)
        val editText = EditText(context)
        editText.layoutParams = layoutParams
        editText.inputType = InputType.TYPE_CLASS_PHONE
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextColor(Color.TRANSPARENT)
        editText.isCursorVisible = false
        getEditTextFocus(editText)
        addView(editText)
    }

    private fun getEditTextFocus(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
    }

    private fun setDefault(editText: TextView) {
        editText.maxLines = 1
        if (mTextSize != 0f) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        }
        editText.setTextColor(mTextColor)
        editText.gravity = Gravity.CENTER
    }

    private fun setBackground(editText: TextView, drawable: Drawable?) {
        editText.background = drawable
    }

    private fun setPadding(editText: TextView) {
        editText.setPadding(0, 0, 0, 0)
    }

    private val marginLayoutParams: MarginLayoutParams
        get() {
            val params = MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (mMargin != 0) {
                params.leftMargin = mMargin
                params.rightMargin = mMargin
            }
            if (mWidth != 0) {
                params.width = mWidth
            }
            if (mHeight != 0) {
                params.height = mHeight
            }
            return params
        }

}