package ru.yamost.bininfo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.yamost.bininfo.R
import ru.yamost.bininfo.databinding.ActivityMainBinding
import ru.yamost.bininfo.presentation.fragments.BinDetailFragment
import ru.yamost.bininfo.presentation.fragments.SearchInstituteFragment
import ru.yamost.bininfo.presentation.fragments.contract.HasCustomTitle
import ru.yamost.bininfo.presentation.fragments.contract.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateToolbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, SearchInstituteFragment())
                .commit()
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        goBack()
        return true
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun showBinDetailScreen(cardBin: String) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, BinDetailFragment.newInstance(cardBin))
            .commit()
    }

    private fun updateToolbar() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = fragment.getNewTitle()
        } else {
            binding.toolbar.title = getString(R.string.app_name)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }
}