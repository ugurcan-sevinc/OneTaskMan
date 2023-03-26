package com.ugrcaan.onetaskman.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ugrcaan.onetaskman.R
import com.ugrcaan.onetaskman.data.task.Task
import com.ugrcaan.onetaskman.data.task.TaskViewModel
import com.ugrcaan.onetaskman.databinding.FragmentAddTaskBinding
import java.util.*
import kotlin.math.absoluteValue

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private lateinit var gestureDetector: GestureDetectorCompat
    private val binding get() = _binding!!

    private lateinit var taskVM: TaskViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)


        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        binding.pickedDueDateHolder.text = formattedDate
        // initialize gesture detector
        gestureDetector = GestureDetectorCompat(requireContext(), GestureListener())

        // set touch listener for the fragment's view
        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.addTaskBtn.setOnClickListener{
            val task = Task(
                header = binding.headerText.text.toString(),
                description = binding.descriptionText.text.toString(),
                date = binding.pickedDueDateHolder.text.toString(),
                isDone = false
            )
            taskVM.addTask(task)
            findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)
        }


        binding.pickDueDateBtn.setOnClickListener {
            showDatePicker()
        }

        taskVM = ViewModelProvider(this)[TaskViewModel::class.java]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val datePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val selectedDate = calendar.time

            // Check if the selected date is before today's date
            if (selectedDate.before(Calendar.getInstance().time)) {
                // Set today's date as the selected date
                calendar.time = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
                val formattedDate = dateFormat.format(calendar.time)
                binding.pickedDueDateHolder.text = formattedDate

                // Show toast message
                Toast.makeText(context, "You can't pick a date from the past", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Format the selected date as "dd/MMM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
                val formattedDate = dateFormat.format(selectedDate)

                // Set the formatted date in the TextView
                binding.pickedDueDateHolder.text = formattedDate
            }
        }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), datePickerListener, year, month, day)
        datePickerDialog.show()
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // calculate distance traveled on X and Y axis
            val deltaX = e2.x.minus(e1.x ?: 0f) ?: 0f
            val deltaY = e2.y.minus(e1.y ?: 0f) ?: 0f

            // only trigger if the swipe is mostly horizontal and fast enough
            if (deltaX.absoluteValue > deltaY.absoluteValue && deltaX.absoluteValue > SWIPE_THRESHOLD && velocityX.absoluteValue > SWIPE_VELOCITY_THRESHOLD) {
                // swipe from right to left
                if (deltaX > 0) {
                    // slide in from right and slide out to left
                    val anim = AnimationUtils.loadAnimation(
                        context,
                        com.google.android.material.R.anim.m3_side_sheet_slide_out
                    )
                    view?.startAnimation(anim)
                    findNavController().navigateUp()
                    return true
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }


    }

    companion object {
        private const val SWIPE_THRESHOLD = 100 // minimum distance in pixels to trigger a swipe
        private const val SWIPE_VELOCITY_THRESHOLD =
            100 // minimum velocity in pixels per second to trigger a swipe
    }

}


