package id.dipoengoro.sleeptracker.sleepquality

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
import id.dipoengoro.sleeptracker.databinding.FragmentSleepQualityBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SleepQualityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSleepQualityBinding>(
            inflater, R.layout.fragment_sleep_quality, container, false
        )

        val application = requireNotNull(this.activity).application
        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)
        val sleepQualityViewModel =
            ViewModelProvider(this, viewModelFactory)[SleepQualityViewModel::class.java]
        binding.sleepQualityViewModel = sleepQualityViewModel
        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
                )
                sleepQualityViewModel.doneNavigating()
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}