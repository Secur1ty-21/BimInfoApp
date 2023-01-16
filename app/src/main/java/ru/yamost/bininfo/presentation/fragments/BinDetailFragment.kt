package ru.yamost.bininfo.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
                binInfo.scheme?.let {
                    titleScheme.visibility = TextView.VISIBLE
                    scheme.text = it
                }
                binInfo.brand?.let {
                    titleBrand.visibility = TextView.VISIBLE
                    brand.text = it
                }
                binInfo.cardNumber?.let { cardNumber ->
                    cardNumber.length?.let { length ->
                        cardLength.text = getString(R.string.value_card_length, length)
                        titleCardNumber.visibility = TextView.VISIBLE
                    }
                    cardNumber.isValidate?.let { isValidate ->
                        lunh.text =
                            getString(R.string.value_card_lunh, translateBooleanToText(isValidate))
                        titleCardNumber.visibility = TextView.VISIBLE
                    }
                }
                binInfo.type?.let {
                    titleType.visibility = TextView.VISIBLE
                    cardType.text = it
                }
                binInfo.isPrepaid?.let {
                    titlePrepaid.visibility = TextView.VISIBLE
                    cardPrepaid.text = translateBooleanToText(it)
                }
                binInfo.country?.let { country ->
                    titleCountry.visibility = TextView.VISIBLE
                    country.name?.let { name ->
                        countryName.text = getString(
                            R.string.value_country_name,
                            country.emoji ?: "", name
                        )
                        titleCountry.visibility = TextView.VISIBLE
                    }
                    country.latitude?.let { latitude ->
                        countryCoordinates.text = getString(
                            R.string.value_country_coordinates,
                            latitude, country.longitude ?: 0.0
                        )
                        titleCountry.visibility = TextView.VISIBLE
                    }
                }
                binInfo.bank?.let { bank ->
                    titleBank.visibility = TextView.VISIBLE
                    bank.name?.let { name ->
                        getString(
                            R.string.value_bank_name,
                            name, bank.city ?: ""
                        )
                        titleBank.visibility = TextView.VISIBLE
                    }
                    bank.url?.let {
                        bankUrl.text = it
                        titleBank.visibility = TextView.VISIBLE
                    }
                    bank.phone?.let {
                        bankPhone.text = it
                        titleBank.visibility = TextView.VISIBLE
                    }
                }
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