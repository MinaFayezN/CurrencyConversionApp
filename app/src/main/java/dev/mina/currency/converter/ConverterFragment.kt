package dev.mina.currency.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.databinding.FragmentConverterBinding

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private val viewState by lazy { ConverterViewState() }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false).also {
            it.viewState = viewState
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}