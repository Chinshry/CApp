package com.chinshry.application.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.chinshry.application.R
import com.chinshry.application.bean.Router
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_test.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_TEST).navigation()
        }

        btn_tabView.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_TABVIEW).navigation()
        }

        btn_scrollView.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_SCROLLVIEW).navigation()
        }

        btn_viewModel.setOnClickListener {
            ARouter.getInstance().build(Router.FRAGMENT_VIEWMODEL).navigation()
        }

    }
}