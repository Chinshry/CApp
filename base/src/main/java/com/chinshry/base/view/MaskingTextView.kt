package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.chinshry.base.util.MaskingUtil
import com.chinshry.base.view.MaskingTextView.MaskType.Companion.getMaskType
import com.example.base.R

/**
 * Created by chinshry on 2022/01/21.
 * Describe：脱敏文本框自定义组件
 */
open class MaskingTextView(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    var maskType: MaskType? = null
    var maskStart: Int? = null
    var maskEnd: Int? = null
    var maskReplacement: String? = null
    var maskReplacementAll: Boolean? = null

    var value: String = ""

    companion object {
        val MaskTypeDef = MaskType.CUSTOM
        const val MaskStartDef = 0
        const val MaskEndDef = 0
        const val MaskReplacementDef = "*"
        const val MaskReplacementAllDef = false
    }

    init {
        initStyle(attrs)
        initView()
    }

    enum class MaskType {
        CUSTOM, MOBILE, NAME, CRT, BANKCARD;

        companion object {
            fun getMaskType(typeInt: Int): MaskType {
                values().forEach {
                    if (typeInt == it.ordinal) return it
                }
                return CUSTOM
            }
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun initStyle(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskingText)

        maskType = getMaskType(typedArray.getInteger(R.styleable.MaskingText_mask_type, MaskTypeDef.ordinal))
        maskStart = typedArray.getInteger(R.styleable.MaskingText_mask_start, MaskStartDef)
        maskEnd = typedArray.getInteger(R.styleable.MaskingText_mask_end, MaskEndDef)
        maskReplacement = typedArray.getString(R.styleable.MaskingText_mask_replacement) ?: MaskReplacementDef
        maskReplacementAll = typedArray.getBoolean(R.styleable.MaskingText_mask_replace_all, MaskReplacementAllDef)

        typedArray.recycle()
    }

    private fun initView() {
        doAfterTextChanged {
            if (isFocused) {
                value = it.toString()
            }
        }

        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                this.setText(value)
            } else {
                this.setText(getMaskText())
            }
        }

    }

    private fun getMaskText(): String {
        when (maskType) {
            MaskType.CUSTOM -> {
                return if (maskReplacementAll == true) {
                    MaskingUtil.getMaskingStringAll(
                        text.toString(),
                        maskStart ?: MaskStartDef,
                        maskEnd ?: MaskEndDef,
                        maskReplacement ?: MaskReplacementDef
                    )
                } else {
                    MaskingUtil.getMaskingString(
                        text.toString(),
                        maskStart ?: MaskStartDef,
                        maskEnd ?: MaskEndDef,
                        maskReplacement ?: MaskReplacementDef
                    )
                }
            }
            MaskType.MOBILE -> {
                return MaskingUtil.getMaskingMobile(text.toString())
            }
            MaskType.NAME -> {
                return MaskingUtil.getMaskingName(text.toString())
            }
            MaskType.CRT -> {
                return MaskingUtil.getMaskingCertNum(text.toString())
            }
            MaskType.BANKCARD -> {
                return MaskingUtil.getMaskingBankNum(text.toString())
            }
            else -> {
                return text.toString()
            }
        }
    }

}