package ru.yamost.bininfo.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yamost.bininfo.R
import ru.yamost.bininfo.databinding.FragmentSearchInstituteBinding
import ru.yamost.bininfo.presentation.BinInfoApiStatus
import ru.yamost.bininfo.presentation.BinInfoViewModel
import ru.yamost.bininfo.presentation.adapter.SearchHistoryAdapter
import ru.yamost.bininfo.presentation.adapter.SearchHistoryListener
import ru.yamost.bininfo.presentation.fragments.contract.navigator
import java.util.InputMismatchException

class SearchInstituteFragment : Fragment() {

    private lateinit var binding: FragmentSearchInstituteBinding
    private val sharingViewModel: BinInfoViewModel by activityViewModels()
    private lateinit var cardBin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString(KEY_CARD_BIN)?.let {
            cardBin = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchInstituteBinding.inflate(inflater, container, false)
        binding.buttonSearch.setOnClickListener {
            onPressedButtonSearch()
        }

        binding.inputCardNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onPressedButtonSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.historyList.layoutManager = LinearLayoutManager(context)
        val adapter = SearchHistoryAdapter(object : SearchHistoryListener {
            override fun onItemClickListener(cardBin: String) {
                this@SearchInstituteFragment.cardBin = cardBin
                sharingViewModel.getBinInstitute(cardBin)
            }
        })
        binding.historyList.adapter = adapter
        adapter.submitList(sharingViewModel.searchHistory)

        with(sharingViewModel) {
            status.observe(viewLifecycleOwner) {
                onStatusChanged()
            }
        }

        return binding.root
    }

    private fun onStatusChanged() {
        sharingViewModel.status.value?.let { status ->
            when (status) {
                BinInfoApiStatus.DONE -> {
                    binding.progressIndicator.hide()
                    sharingViewModel.setDefaultState()
                    navigator().showBinDetailScreen(cardBin)
                }
                BinInfoApiStatus.ERROR -> {
                    binding.progressIndicator.hide()
                    when (sharingViewModel.error) {
                        is InputMismatchException -> {
                            binding.inputCardNumber.error =
                                getString(R.string.invalid_input_error_text)
                        }
                        is retrofit2.HttpException -> {
                            binding.inputCardNumber.error = getString(R.string.not_found_error_text)
                        }
                    }
                    sharingViewModel.setDefaultState()
                }
                BinInfoApiStatus.LOADING -> {
                    binding.progressIndicator.show()
                }
                BinInfoApiStatus.DEFAULT -> {
                    binding.progressIndicator.hide()
                }
            }
        }
    }

    private fun onPressedButtonSearch() {
        cardBin = binding.inputCardNumber.text?.toString() ?: ""
        sharingViewModel.getBinInstitute(cardBin)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CARD_BIN, cardBin)
    }

    companion object {
        @JvmStatic
        private val KEY_CARD_BIN = "KEY_CARD_BIN"
    }
}