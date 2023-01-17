package ru.yamost.bininfo.presentation.fragments

import android.content.Intent
import android.net.Uri
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
                        country.longitude?.let { longitude ->
                            countryCoordinates.text = getString(
                                R.string.value_country_coordinates,
                                latitude, longitude
                            )
                            countryCoordinates.setOnClickListener {
                                onCoordinatesClicked(
                                    latitude,
                                    longitude
                                )
                            }
                            titleCountry.visibility = TextView.VISIBLE
                        }
                    }
                }
                binInfo.bank?.let { bank ->
                    bank.name?.let { name ->
                        bankName.text = getString(
                            R.string.value_bank_name,
                            name, bank.city ?: ""
                        )
                        titleBank.visibility = TextView.VISIBLE
                    }
                    bank.url?.let { url ->
                        bankUrl.text = url
                        bankUrl.setOnClickListener { onBankUrlClicked(url) }
                        titleBank.visibility = TextView.VISIBLE
                    }
                    bank.phone?.let { phone ->
                        bankPhone.text = phone
                        bankPhone.setOnClickListener { onPhoneClicked(phone) }
                        titleBank.visibility = TextView.VISIBLE
                    }
                }
            }
        }
    }

    private fun onCoordinatesClicked(latitude: Double, longitude: Double) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude"))
        startImplicitIntent(intent)
    }

    private fun onBankUrlClicked(_url: String) {
        var url = _url
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://$url"
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startImplicitIntent(intent)
    }

    private fun onPhoneClicked(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startImplicitIntent(intent)
    }

    private fun startImplicitIntent(intent: Intent) {
        if (isIntentSafe(intent)) {
            startActivity(intent)
        }
    }

    private fun isIntentSafe(intent: Intent): Boolean {
        val packageManager = requireActivity().packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities.size > 0
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