package com.jueggs.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_test_rangebar.*

class TestRangeBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rangebar)

        rangebar.observeMinChanging().subscribe { minChanging.text = it.toString() }
        rangebar.observeMaxChanging().subscribe { maxChanging.text = it.toString() }
        rangebar.observeRangeChanging().subscribe { rangeChanging.text = it.toString() }
        rangebar.observeMinChanged().subscribe { minChanged.text = it.toString() }
        rangebar.observeMaxChanged().subscribe { maxChanged.text = it.toString() }
        rangebar.observeRangeChanged().subscribe { rangeChanged.text = it.toString() }
    }

    fun setMin(View: View) = rangebar.setMin(33)

    fun setMax(View: View) = rangebar.setMax(55)

    fun getMin(View: View) {
        currentMin.text = rangebar.getMin().toString()
    }

    fun getMax(View: View) {
        currentMax.text = rangebar.getMax().toString()
    }
}