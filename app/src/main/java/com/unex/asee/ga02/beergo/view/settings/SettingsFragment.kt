package com.unex.asee.ga02.beergo.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.unex.asee.ga02.beergo.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}