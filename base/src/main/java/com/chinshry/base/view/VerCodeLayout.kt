package com.chinshry.base.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.get
import com.example.base.R
import java.util.*

/**
 * Created by chinshry on 2021/12/29.
 * File Name: VerCodeLayout.kt
 * Describe：验证码框布局
 */
open class VerCodeLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private val mCodes: MutableList<String> = ArrayList()
    private var textViewCount: Int? = null

    private val mTextViews: MutableList<TextView> = ArrayList()
    private var onInputListener: OnInputListener? = null
    private lateinit var mEditText: EditText
    private lateinit var mLinearLayout: LinearLayout

    override fun onFinishInflate() {
        super.onFinishInflate()

        mLinearLayout = getChildAt(0) as LinearLayout
        mEditText = getChildAt(1) as EditText
        textViewCount = mLinearLayout.childCount
        for (i in 0 until textViewCount!!) {
            mTextViews.add(mLinearLayout[i] as TextView)
        }
        mEditText.setOnKeyListener(InnerKeyListener())
        mEditText.addTextChangedListener(InnerTextWatcher())
    }

    internal inner class InnerKeyListener :
        OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && mCodes.size > 0) {
                mCodes.removeAt(mCodes.size - 1)
                showCode()
                return true
            }
            return false
        }
    }

    internal inner class InnerTextWatcher :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            if (editable.isNotEmpty()) {
                mEditText.setText("")
                setCode(editable.toString())
            }
        }
    }

    private fun setCode(code: String) {
        if (TextUtils.isEmpty(code)) {
            return
        }
        for (element in code) {
            if (mCodes.size < textViewCount!!) {
                mCodes.add(element.toString())
            }
        }
        showCode()
    }

    private fun showCode() {
        for (i in 0 until textViewCount!!) {
            val textView: TextView = mTextViews[i]
            if (mCodes.size > i) {
                textView.text = mCodes[i]
            } else {
                textView.text = ""
            }
        }
        setFocusColor()
        setCallBack()
    }

    private fun getCode(): String {
        val sb = StringBuilder()
        for (code in mCodes) {
            sb.append(code)
        }
        return sb.toString()
    }

    private fun setFocusColor() {
        for (i in 0 until textViewCount!!) {
            mTextViews[i].setBackgroundResource(R.drawable.et_verify_code_normal)
        }
        if (mCodes.size < textViewCount!!) {
            mTextViews[mCodes.size].setBackgroundResource(R.drawable.et_verify_code_focused)
        }
    }

    private fun setCallBack() {
        if (onInputListener == null) {
            return
        }
        if (mCodes.size == textViewCount) {
            onInputListener!!.onComplete(getCode())
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