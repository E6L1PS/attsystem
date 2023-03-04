package com.mirea.attsystem.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirea.attsystem.MainActivity
import com.mirea.attsystem.R
import com.mirea.attsystem.adapters.AttendancesAdapter
import com.mirea.attsystem.adapters.PersonsAdapter
import com.mirea.attsystem.databinding.FragmentStartBinding
import com.mirea.attsystem.util.MAIN_ACTIVITY
import com.mirea.attsystem.util.Resource

class StartFragment : Fragment(R.layout.fragment_start) {

    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: AttendancesViewModel
    private lateinit var attendanceAdapter: AttendancesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = MAIN_ACTIVITY.attendancesVM
        /*MAIN_ACTIVITY.supportActionBar?.title = "Посещаемость сотрудников"*/


        setupRecyclerView()

        viewModel.attendances.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { persons ->
                        attendanceAdapter.differ.submitList(persons)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("PERSON_MESSAGE", message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        return view
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        attendanceAdapter = AttendancesAdapter()
        binding.rvAttendances.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = attendanceAdapter
        }
    }
}