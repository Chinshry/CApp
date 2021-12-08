package com.chinshry.application.tabView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chinshry.application.R
import java.io.Serializable


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var dataAdpter: RecyclerViewAdapter
    private lateinit var viewModel: MyViewModel

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tabview, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.rl_layout)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                println("chengshu scroll newState = " + newState)
                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (recyclerView.isInTouchMode) {
                            println("chengshu scroll stop")
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.isInTouchMode) {
                    println("chengshu scroll move")
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        recyclerView.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_OUTSIDE -> {
                    if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                        println("chengshu touch stop")
                    }
                }
                MotionEvent.ACTION_MOVE,
                MotionEvent.ACTION_SCROLL -> {
                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        println("chengshu touch move")
                    }
                }
            }
            return@setOnTouchListener false
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        dataAdpter = RecyclerViewAdapter(
            requireContext(),
            mutableListOf()
        )
        recyclerView.adapter = dataAdpter

        viewModel.data.observe(viewLifecycleOwner, Observer {
            dataAdpter.data = it
            dataAdpter.notifyDataSetChanged()
        })

        refreshData((arguments?.getSerializable(ARG_SECTION_NUMBER) as MutableList<DataBean>))

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newData: MutableList<DataBean>?) {
        viewModel.updateData(newData)
    }

    fun getData() :MutableList<DataBean>? {
        return viewModel.data.value
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(data: MutableList<DataBean>): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_NUMBER, data as Serializable)
                }
            }
        }
    }
}