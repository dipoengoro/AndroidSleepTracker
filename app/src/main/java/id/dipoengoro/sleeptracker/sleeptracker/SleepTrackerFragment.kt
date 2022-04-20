package id.dipoengoro.sleeptracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import id.dipoengoro.sleeptracker.R
import id.dipoengoro.sleeptracker.database.SleepDatabase
import id.dipoengoro.sleeptracker.databinding.FragmentSleepTrackerBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SleepTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSleepTrackerBinding>(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )
        val adapter = SleepNightAdapter()
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory)[
                SleepTrackerViewModel::class.java
        ]
        binding.apply {
            this.sleepTrackerViewModel = sleepTrackerViewModel
            sleepList.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
        }
        sleepTrackerViewModel.apply {
            navigateToSleepQuality.observe(viewLifecycleOwner) {
                it?.let {
                    this@SleepTrackerFragment.findNavController().navigate(
                        SleepTrackerFragmentDirections
                            .actionSleepTrackerFragmentToSleepQualityFragment(it.nightId)
                    )
                    doneNavigating()
                }
            }
            nights.observe(viewLifecycleOwner) {
                adapter.data = it
            }
        }
        return binding.root
    }
}