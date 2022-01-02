package com.chinshry.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import com.blankj.utilcode.util.KeyboardUtils
import com.example.base.R

/**
 * Created by chinshry on 2021/12/29.
 * Describe：验证码逻辑处理
 */
open class VerCodeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mTextViews: List<TextView> = listOf()
    private val mCodes: MutableList<String> = mutableListOf()

    private var onInputListener: OnInputListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        findViewById<LinearLayout>(R.id.verCodeLinearLayout)?.let { linearLayout ->
            mTextViews = (linearLayout.children.map {(it as TextView)}).toList()
        }

        findViewById<EditText>(R.id.verCodeEditTextview)?.let { editText ->
            editText.setOnKeyListener { _, keyCode, event ->
                // 删除处理
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    mCodes.size > 0
                ) {
                    mCodes.removeAt(mCodes.size - 1)
                    showCode()
                    true
                } else {
                    false
                }
            }

            editText.showSoftInputOnFocus

            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    KeyboardUtils.showSoftInput()
                }
            }

            editText.doAfterTextChanged { inputChar ->
                if (!inputChar.isNullOrEmpty()) {
                    // 清空EditText
                    editText.setText("")

                    // 处理粘贴输入
                    inputChar.forEach { eachChar ->
                        if (mCodes.size < mTextViews.size) {
                            mCodes.add(eachChar.toString())
                        }
                    }

                    showCode()
                }
            }
        }

    }

    private fun showCode() {
        mTextViews.forEachIndexed { index, textView ->
            // 设置文字
            textView.text = mCodes.getOrElse(index) { "" }

            // 设置背景
            textView.setBackgroundResource(
                if (index == mCodes.size && mCodes.size < mTextViews.size) {
                    R.drawable.et_verify_code_focused
                } else {
                    R.drawable.et_verify_code_normal
                }
            )

        }

        onInputListener?.let { listener ->
            if (mCodes.size == mTextViews.size) {
                listener.onComplete(mCodes.joinToString(""))
            } else {
                listener.onInput()
            }
        }
    }

    interface OnInputListener {
        fun onComplete(code: String?)
        fun onInput()
    }

    fun setOnInputListener(onInputListener: OnInputListener?) {
        this.onInputListener = onInputListener
    }
}