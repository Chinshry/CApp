package com.chinshry.base.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.RectShape
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
import android.graphics.drawable.ShapeDrawable

/**
 * Created by chinshry on 2021/12/29.
 * Describe：验证码布局
 */
class VerCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    VerCodeLayout(context, attrs, defStyleAttr) {

    private val mCount: Int
    private var mNormalBackground: Drawable?
    private val mFocusedBackground: Drawable?
    private val mWidth: Int
    private val mHeight: Int
    private val mTextSize: Float
    private val mDividerWidth: Int
    @ColorInt
    private val mTextColor: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerCodeEditText)
        mCount = typedArray.getInt(R.styleable.VerCodeEditText_verCodeCount, 0)
        mNormalBackground = typedArray.getDrawable(R.styleable.VerCodeEditText_verCodeNormalBackground)
        mFocusedBackground = typedArray.getDrawable(R.styleable.VerCodeEditText_verCodeFocusedBackground)
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.VerCodeEditText_verCodeTextSize, 0).toFloat()
        mTextColor = typedArray.getColor(R.styleable.VerCodeEditText_verCodeTextColor, Color.BLACK)
        mWidth = typedArray.getDimension(R.styleable.VerCodeEditText_verCodeWidth, 0f).toInt()
        mHeight = typedArray.getDimension(R.styleable.VerCodeEditText_verCodeHeight, 0f).toInt()
        mDividerWidth = typedArray.getDimension(R.styleable.VerCodeEditText_verCodeDividerWidth, 0f).toInt()
        typedArray.recycle()

        createTextViews()
        initEditText()
    }

    private fun createTextViews() {
        if (mCount <= 0) {
            return
        }

        val mLinearLayout = LinearLayout(context)
        mLinearLayout.id = R.id.verCodeLinearLayout
        mLinearLayout.orientation = LinearLayout.HORIZONTAL
        mLinearLayout.gravity = Gravity.CENTER
        mLinearLayout.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mLinearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val dividerDrawable = ShapeDrawable(RectShape())
        dividerDrawable.paint.color = Color.TRANSPARENT
        dividerDrawable.paint.style = Paint.Style.FILL
        dividerDrawable.intrinsicWidth = mDividerWidth
        mLinearLayout.dividerDrawable = dividerDrawable

        for (index in 0 until mCount) {
            mLinearLayout.addView(getTextView(index))
        }

        addView(mLinearLayout)
    }

    private fun getTextView(index: Any?):TextView {
        val mTextView = TextView(context)
        mTextView.width = mWidth
        mTextView.height = mHeight
        mTextView.maxLines = 1
        mTextView.gravity = Gravity.CENTER

        if (mTextSize != 0f) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        }
        mTextView.setTextColor(mTextColor)

        if (index == 0) {
            mTextView.background = mFocusedBackground
        } else {
            mTextView.background = mNormalBackground
        }

        return mTextView
    }

    private fun initEditText() {
        val editText = EditText(context)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_START, R.id.verCodeLinearLayout)
        layoutParams.addRule(ALIGN_END, R.id.verCodeLinearLayout)
        editText.id = R.id.verCodeEditTextview
        editText.layoutParams = layoutParams
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.setTextColor(Color.TRANSPARENT)
        editText.isCursorVisible = false
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()

        addView(editText)
    }

}