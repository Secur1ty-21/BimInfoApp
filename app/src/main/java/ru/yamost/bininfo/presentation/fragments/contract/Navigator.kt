package ru.yamost.bininfo.presentation.fragments.contract

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator = requireActivity() as Navigator

interface Navigator {
    fun showBinDetailScreen(cardBin: String)
    fun goBack()
}