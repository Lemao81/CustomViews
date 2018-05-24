package com.jueggs.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jueggs.customview.stackoverflowtag.StackoverflowTag
import com.jueggs.jutils.extension.random
import kotlinx.android.synthetic.main.activity_test_stackoverflow_tag.*

class TestStackoverflowTagActivity : AppCompatActivity() {
    private val randomTagNames = listOf("javascript", "java", "c#", "html", "css", "android", "kotlin", "angularjs", "knockout", "spring", "jquery")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_stackoverflow_tag)
    }

    fun onAddTag(view: View) {
        val input = edtTagName.text.toString()
        linTagContainer.addView(StackoverflowTag(this, if (input.isBlank()) randomTagNames.random() else input))
    }
}