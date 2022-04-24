package id.dipoengoro.sleeptracker.sleepdetail

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
import id.dipoengoro.sleeptracker.databinding.FragmentSleepDetailBinding
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SleepDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_detail, container, false
        )
        val application = requireNotNull(this.activity).application
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)
        val viewModel =
            ViewModelProvider(this, viewModelFactory)[SleepDetailViewModel::class.java]

        binding.apply {
            sleepDetailViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.run {
            navigateToSleepTracker.observe(viewLifecycleOwner) {
                it?.let {
                    this@SleepDetailFragment.findNavController().navigate(
                        SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                    )
                    doneNavigating()
                }
            }
        }
        return binding.root
    }
}