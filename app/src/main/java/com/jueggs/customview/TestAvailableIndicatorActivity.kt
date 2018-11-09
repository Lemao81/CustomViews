package com.jueggs.customview

import com.jueggs.andutils.base.BaseActivity
import com.jueggs.customview.availableindicator.AvailableState
import kotlinx.android.synthetic.main.activity_test_available_indicator.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TestAvailableIndicatorActivity : BaseActivity() {
    override fun layout() = R.layout.activity_test_available_indicator

    override fun initializeViews() {
        btnOnline.onClick { indicator.setState(AvailableState.ONLINE) }
        btnOffline.onClick { indicator.setState(AvailableState.OFFLINE) }
        btnAvailable.onClick { indicator.setState(AvailableState.AVAILABLE) }
    }
}