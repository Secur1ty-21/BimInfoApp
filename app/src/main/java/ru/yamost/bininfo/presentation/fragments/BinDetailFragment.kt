package ru.yamost.bininfo.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.yamost.bininfo.R
import ru.yamost.bininfo.databinding.FragmentBinDetailBinding
import ru.yamost.bininfo.presentation.BinInfoViewModel
import ru.yamost.bininfo.presentation.fragments.contract.HasCustomTitle

class BinDetailFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentBinDetailBinding
    private val sharingViewModel: BinInfoViewModel by activityViewModels()
    private lateinit var cardBin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardBin = savedInstanceState?.getString(KEY_CARD_BIN) ?: arguments?.getString(ARG_CARD_BIN)
                ?: throw IllegalArgumentException("Card bin arg not found")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBinDetailBinding.inflate(inflater, container, false)
        renderDetailFragment()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CARD_BIN, cardBin)
    }

    private fun renderDetailFragment() {
        sharingViewModel.binInfo.value?.let { binInfo ->
            with(binding) {
                scheme.text = binInfo.scheme
                brand.text = binInfo.brand
                cardLength.text = getString(
                    R.string.value_card_length,
                    binInfo.cardNumber.length
                )
                lunh.text = getString(
                    R.string.value_card_lunh,
                    binInfo.cardNumber.isValidate?.let { translateBooleanToText(it) }
                )
                cardType.text = binInfo.type
                cardPrepaid.text = binInfo.isPrepaid?.let { translateBooleanToText(it) }
                countryName.text = getString(
                    R.string.value_country_name,
                    binInfo.country.emoji,
                    binInfo.country.name
                )
                countryCoordinates.text = getString(
                    R.string.value_country_coordinates,
                    binInfo.country.latitude,
                    binInfo.country.longitude
                )
                bankName.text = getString(
                    R.string.value_bank_name,
                    binInfo.bank.name,
                    binInfo.bank.city
                )
                bankUrl.text = binInfo.bank.url
                bankPhone.text = binInfo.bank.phone
            }
        }
    }

    private fun translateBooleanToText(booleanValue: Boolean): String {
        return if (booleanValue) {
            getString(R.string.translate_boolean_true)
        } else {
            getString(R.string.translate_boolean_false)
        }
    }

    override fun getNewTitle(): String {
        return getString(
            R.string.toolbar_title_bin_detail_fragment,
            cardBin
        )
    }

    companion object {
        @JvmStatic
        private val ARG_CARD_BIN = "ARG_CARD_BIN"
        @JvmStatic
        private val KEY_CARD_BIN = "KEY_CARD_BIN"

        @JvmStatic
        fun newInstance(cardBin: String): BinDetailFragment {
            val args = bundleOf(ARG_CARD_BIN to cardBin)
            val fragment = BinDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}