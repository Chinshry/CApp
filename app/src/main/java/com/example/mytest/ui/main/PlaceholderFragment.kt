package com.example.mytest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest.R


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    // private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
        //     setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        // }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        // val textView: TextView = root.findViewById(R.id.section_label)
        // pageViewModel.text.observe(this, Observer<String> {
        //     textView.text = it
        // })
        val recyclerView: RecyclerView = root.findViewById(R.id.rl_layout)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = context?.let { arguments?.getStringArrayList("params")?.let { it1 ->
            RecyclerViewAdapter(it,
                it1
            )
        } }


        return root
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
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}