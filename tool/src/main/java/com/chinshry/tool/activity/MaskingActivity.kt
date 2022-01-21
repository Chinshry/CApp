package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.tool.R
import kotlinx.android.synthetic.main.activity_masking.*


/**
 * Created by chinshry on 2021/12/23.
 * Describe：脱敏测试
 */
@BuryPoint(pageName = "脱敏测试")
@Route(path = Router.MASKING)
class MaskingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masking)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        rl_activity.setOnTouchListener { _, _ ->
            rl_activity.requestFocus()
        }

        val refreshMaskView = {
            mask_custom.requestFocus()
            rl_activity.requestFocus()
            // mask_custom.clearFocus()
        }

        ed_mask_start.let {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    mask_custom.maskStart = it.text.toString().toIntOrNull()
                    refreshMaskView()
                }
            }
        }

        ed_mask_end.let {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    mask_custom.maskEnd = it.text.toString().toIntOrNull()
                    refreshMaskView()
                }
            }
        }

        ed_mask_replacement.let {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    mask_custom.maskReplacement = it.text.toString()
                    refreshMaskView()
                }
            }
        }

        ed_mask_replace_all.let {
            it.setOnCheckedChangeListener { _, isChecked ->
                mask_custom.maskReplacementAll = isChecked
                refreshMaskView()
            }
        }

    }

}