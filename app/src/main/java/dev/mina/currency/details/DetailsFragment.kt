package dev.mina.currency.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.databinding.FragmentDetailsBinding
import dev.mina.currency.utils.observeForSingleEvent

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    /**
     * Using binding with this way is the recommended one by google
     * the old way {private lateinit var binding: FragmentDetailsBinding}
     * leads to memory leaks
     *
     * Please refer to this:
     * @see <a href="https://developer.android.com/topic/libraries/view-binding#fragments">Binding in fragments</a>
     */
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val arguments: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()
    private val viewState: DetailsViewState by lazy { DetailsViewState() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewState = viewState
        }
        uiObservations()
        return binding.root
    }

    private fun uiObservations() {
        viewModel.loading.observeForSingleEvent(this) {
            viewState.itemsDisabled.set(it)
            if (it) {
                binding.shimmerViewContainer.startShimmer()
            } else {
                binding.shimmerViewContainer.stopShimmer()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}