package com.chinshry.application.tabView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tabview, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.rl_layout)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        recyclerView.layoutManager = LinearLayoutManager(context)
        dataAdpter = RecyclerViewAdapter(
            requireContext(),
            mutableListOf()
        )
        recyclerView.adapter = dataAdpter

        viewModel.data.observe(this, Observer {
            dataAdpter.datas = it
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
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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