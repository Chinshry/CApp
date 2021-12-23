package com.chinshry.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getBuryNameByModelName
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.util.getPageBuryPoint
import com.chinshry.base.util.logBuryPoint
import com.chinshry.base.view.clickWithTrigger
import com.example.base.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.custom_bar.*

/**
 * Created by chinshry on 2021/12/23.
 * File Name: BaseFragment.kt
 */
abstract class BaseFragment: Fragment() {
    var pageBuryPoint: BuryPointInfo = BuryPointInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 注解埋点
        getPageBuryPoint(this::class.java)?.let {
            pageBuryPoint = BuryPointInfo(
                pageName = it.pageName,
                pageChannel = getBuryNameByModelName(CommonUtils.getModuleName(this.javaClass.name))
            )
        }

        logBuryPoint(pageBuryPoint)

        return inflater.inflate(setLayout(), container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            logBuryPoint(pageBuryPoint)
        }
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar
            .with(this)
            .fitsSystemWindows(true)
            .barColor(R.color.white)
            .autoDarkModeEnable(true)
            .init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCloseButton()
        setBackButton()
    }

    fun setPageTitle(title: String) {
        header_text_title?.text = title
    }

    private fun setCloseButton(
        function: () -> Unit = { onCloseBtn() }
    ) {
        header_btn_close?.clickWithTrigger { function() }
    }

    private fun setBackButton(
        function: () -> Unit = { onBackBtn() }
    ) {
        header_btn_back?.clickWithTrigger { function() }
    }

    open fun onBackBtn() {
        if (!findNavController().navigateUp()) {
            requireActivity().finish()
        }
    }

    open fun onCloseBtn() {
        requireActivity().finish()
    }

    abstract fun setLayout(): Int
}