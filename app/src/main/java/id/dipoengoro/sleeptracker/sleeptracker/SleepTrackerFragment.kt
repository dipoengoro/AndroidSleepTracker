package id.dipoengoro.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.dipoengoro.sleeptracker.R
import id.dipoengoro.sleeptracker.database.SleepDatabase
import id.dipoengoro.sleeptracker.databinding.FragmentSleepTrackerBinding
import kotlinx.coroutines.InternalCoroutinesApi

class SleepTrackerFragment : Fragment() {

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSleepTrackerBinding>(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory)[
            SleepTrackerViewModel::class.java
        ]
        binding.apply {
            this.sleepTrackerViewModel = sleepTrackerViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}