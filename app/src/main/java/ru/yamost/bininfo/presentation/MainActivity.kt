package ru.yamost.bininfo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.yamost.bininfo.databinding.BinDetailFragmentBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: BinDetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BinDetailFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}