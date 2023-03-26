package com.ugrcaan.onetaskman.fragment

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ugrcaan.onetaskman.R
import com.ugrcaan.onetaskman.adapter.TaskAdapter
import com.ugrcaan.onetaskman.data.task.Task
import com.ugrcaan.onetaskman.data.task.TaskViewModel
import com.ugrcaan.onetaskman.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskVM : TaskViewModel
    private lateinit var adapter : TaskAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val currentTime: Calendar = Calendar.getInstance()
        when (currentTime.get(Calendar.HOUR_OF_DAY)) {
            in 6..11 -> binding.welcomeText.text = "Hello, Good Morning!"
            in 12..17 -> binding.welcomeText.text = "Hello, Good Afternoon!"
            else -> binding.welcomeText.text = "Hello, Good Night!"
        }

        binding.newTaskBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
        }

        adapter = TaskAdapter()
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        taskVM = ViewModelProvider(this)[TaskViewModel::class.java]
        taskVM.getAllTasks().observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}