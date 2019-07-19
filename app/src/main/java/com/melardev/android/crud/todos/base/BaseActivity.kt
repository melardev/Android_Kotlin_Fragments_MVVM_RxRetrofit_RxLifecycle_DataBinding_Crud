package com.melardev.android.crud.todos.base

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initProgressDialog()
    }

    private fun initProgressDialog() {
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        progressBar!!.isIndeterminate = true

        val relativeLayout = RelativeLayout(this)
        relativeLayout.gravity = Gravity.CENTER

        relativeLayout.addView(progressBar)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        progressBar!!.visibility = View.VISIBLE

        this.addContentView(relativeLayout, params)
    }

    protected fun handleErrorResponse(errors: Array<String>?) {
        hideLoader()

        // Build.VERSION.SDK_INT >= Build.VERSION_CODES.O:
        // You could also use String.join(",", getTodoOperation.fullMessages)

        if (errors != null && errors.isNotEmpty())
            Toast.makeText(this, TextUtils.join("\n", errors), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Unknown Error TodoDetailsActivity", Toast.LENGTH_SHORT).show()
    }

    protected fun hideLoader() {
        progressBar!!.visibility = View.GONE
    }

    protected fun displayLoader() {
        progressBar!!.visibility = View.VISIBLE
    }
}
