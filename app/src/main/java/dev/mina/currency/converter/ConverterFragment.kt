package dev.mina.currency.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.databinding.FragmentConverterBinding
import dev.mina.currency.observeForSingleEvent

private const val TAG = "ConverterFragment"

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
    private val viewState by lazy { ConverterViewState(viewModel::updateSelected, viewModel::swap) }

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
        viewModel.fromSymbols.observe(viewLifecycleOwner) {
            viewState.fromSymbols.set(it)
        }
        viewModel.selectedFromLD.observeForSingleEvent(viewLifecycleOwner) {
            viewState.selectedFrom.set(it)
        }
        viewModel.toSymbols.observe(viewLifecycleOwner) {
            viewState.toSymbols.set(it)
        }
        viewModel.selectedToLD.observeForSingleEvent(viewLifecycleOwner) {
            viewState.selectedTo.set(it)
        }
        viewModel.loading.observeForSingleEvent(this) {
            viewState.itemsDisabled.set(it)
            if (it) {
                binding.shimmerViewContainer.startShimmer()
                binding.shimmerViewContainer.visibility = View.VISIBLE
            } else {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}