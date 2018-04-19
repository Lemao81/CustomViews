package com.jueggs.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_test_rangebar.*

class TestRangeBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rangebar)

        rangebarSmooth.observeMinChanging().subscribe { minChanging.text = it.toString() }
        rangebarSmooth.observeMaxChanging().subscribe { maxChanging.text = it.toString() }
        rangebarSmooth.observeRangeChanging().subscribe { rangeChanging.text = it.toString() }
        rangebarSmooth.observeMinChanged().subscribe { minChanged.text = it.toString() }
        rangebarSmooth.observeMaxChanged().subscribe { maxChanged.text = it.toString() }
        rangebarSmooth.observeRangeChanged().subscribe { rangeChanged.text = it.toString() }
        
        rangebarSnap.observeMinChanging().subscribe { minChanging.text = it.toString() }
        rangebarSnap.observeMaxChanging().subscribe { maxChanging.text = it.toString() }
        rangebarSnap.observeRangeChanging().subscribe { rangeChanging.text = it.toString() }
        rangebarSnap.observeMinChanged().subscribe { minChanged.text = it.toString() }
        rangebarSnap.observeMaxChanged().subscribe { maxChanged.text = it.toString() }
        rangebarSnap.observeRangeChanged().subscribe { rangeChanged.text = it.toString() }
        
        rangebarRaster.observeMinChanging().subscribe { minChanging.text = it.toString() }
        rangebarRaster.observeMaxChanging().subscribe { maxChanging.text = it.toString() }
        rangebarRaster.observeRangeChanging().subscribe { rangeChanging.text = it.toString() }
        rangebarRaster.observeMinChanged().subscribe { minChanged.text = it.toString() }
        rangebarRaster.observeMaxChanged().subscribe { maxChanged.text = it.toString() }
        rangebarRaster.observeRangeChanged().subscribe { rangeChanged.text = it.toString() }
    }

    fun setMin(View: View) {
        rangebarSmooth.setMin(33)
        rangebarSnap.setMin(33)
        rangebarRaster.setMin(33)
    }

    fun setMax(View: View) {
        rangebarSmooth.setMax(55)
        rangebarSnap.setMax(55)
        rangebarRaster.setMax(55)
    }

    fun getMin(View: View) {
        currentMin.text = rangebarSmooth.getMin().toString()
    }

    fun getMax(View: View) {
        currentMax.text = rangebarSmooth.getMax().toString()
    }
}