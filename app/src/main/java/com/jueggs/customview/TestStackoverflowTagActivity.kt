package com.jueggs.customview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.jueggs.customview.stackoverflowtag.*
import com.jueggs.jutils.helper.DistinctRandom
import kotlinx.android.synthetic.main.activity_test_stackoverflow_tag.*

class TestStackoverflowTagActivity : AppCompatActivity() {
    private val randomTagNames = listOf("javascript", "java", "c#", "html", "css", "android", "kotlin", "angularjs", "knockout", "spring", "jquery")
    private val random = DistinctRandom(randomTagNames)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_stackoverflow_tag)

        addTagViews(linContainerSmall, Size.SMALL)
        random.reset()
        addTagViews(linContainerMedium, Size.MEDIUM)
        random.reset()
        addTagViews(linContainerBig, Size.BIG)
        random.reset()
        linContainerExtra.addView(StackoverflowTag(this, random.next(), Size.MEDIUM).apply { rightMargin = 20 })
        linContainerExtra.addView(StackoverflowTag(this, random.next(), Size.MEDIUM).apply { rightMargin = 20 })
        linContainerExtra.addView(StackoverflowTag(this, random.next(), Size.MEDIUM).apply { rightMargin = 20 })
    }

    private fun addTagViews(container: LinearLayout, size: Size) = (1..4).forEach {
        container.addView(StackoverflowTag(this, random.next(), size))
    }
}