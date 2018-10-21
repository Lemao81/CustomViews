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

        addTagViews(linContainer1, 1)
        random.reset()
        addTagViews(linContainer2, 2)
        random.reset()
        addTagViews(linContainer3, 3)
        random.reset()
        addTagViews(linContainer4, 4)
        random.reset()
        addTagViews(linContainer5, 5)
        random.reset()
        addTagViews(linContainer6, 6)
        random.reset()
        addTagViews(linContainer7, 7)
        random.reset()
        addTagViews(linContainer8, 8)
        random.reset()
        linContainerExtra.addView(StackoverflowTag(this, random.next()).apply { rightMargin = 20 })
        linContainerExtra.addView(StackoverflowTag(this, random.next()).apply { rightMargin = 20 })
        linContainerExtra.addView(StackoverflowTag(this, random.next()).apply { rightMargin = 20 })
    }

    private fun addTagViews(container: LinearLayout, size: Int) = (1..4).forEach {
        container.addView(StackoverflowTag(this, random.next(), size))
    }
}