package dev.mina.currency.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.databinding.FragmentConverterBinding
import dev.mina.currency.toParams
import dev.mina.currency.utils.observeForSingleEvent

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    /**
     * Using binding with this way is the recommended one by google
     * the old way {private lateinit var binding: FragmentConverterBinding}
     * leads to memory leaks
     *
     * Please refer to this:
     * @see <a href="https://developer.android.com/topic/libraries/view-binding#fragments">Binding in fragments</a>
     */
    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConverterViewModel by viewModels()
    private val viewState by lazy {
        ConverterViewState(
            onSelectionChanged = viewModel::updateSelected,
            onSwapClick = viewModel::swap,
            onDetailsClick = this::navigateToDetails
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewState = viewState
        }
        uiObservations()
        return binding.root
    }

    private fun uiObservations() {
        viewModel.rate.observe(viewLifecycleOwner, viewState::updateRate)
        viewModel.fromSymbols.observe(viewLifecycleOwner, viewState.fromSymbols::set)
        viewModel.toSymbols.observe(viewLifecycleOwner, viewState.toSymbols::set)
        viewModel.selectedFromLD.observeForSingleEvent(viewLifecycleOwner,
            viewState.selectedFrom::set)
        viewModel.selectedToLD.observeForSingleEvent(viewLifecycleOwner, viewState.selectedTo::set)
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

    private fun navigateToDetails() {
        findNavController().navigate(ConverterFragmentDirections.startDetailsFragment(
            base = viewModel.retrieveCurrentBase(),
            symbols = viewModel.toSymbols.value?.toParams()))
    }
}