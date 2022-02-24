package dev.mina.currency

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewState by lazy { MainViewState() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { mainBinding ->
            setContentView(mainBinding.root)
            mainBinding.viewState = viewState
        }
    }
}