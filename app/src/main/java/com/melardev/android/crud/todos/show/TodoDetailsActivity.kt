package com.melardev.android.crud.todos.show

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.melardev.android.crud.R
import com.melardev.android.crud.databinding.TodoDetailsBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.todos.base.BaseActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity


class TodoDetailsActivity : BaseActivity() {

    private var todo: Todo? = null
    private var binding: TodoDetailsBinding? = null
    private var todoId: Long = 0
    private var todoDetailsViewModel: TodoDetailsViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent

        todoId = intent.getLongExtra("TODO_ID", -1)
        initView()
        initViewModel()
    }

    @SuppressLint("CheckResult")
    private fun initViewModel() {

        todoDetailsViewModel = ViewModelProviders.of(this, TodoDetailsViewModel.Factory(todoId))
            .get(TodoDetailsViewModel::class.java)

        lifecycle.addObserver(todoDetailsViewModel!!)

        // Observe the list, if
        todoDetailsViewModel!!.todoLoadOperation.subscribe {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Looper.getMainLooper().isCurrentThread)
                    throw AssertionError("")
            } else {
                if (Looper.getMainLooper() != Looper.myLooper())
                    throw AssertionError("")
            }

            when {
                it.isLoading -> displayLoader()
                it.data != null -> {
                    hideLoader()
                    todo = it.data
                    binding!!.txtDetailsId.text = todo!!.id.toString()
                    binding!!.txtDetailsTitle.text = todo!!.title
                    binding!!.txtDetailsDescription.text = todo!!.description
                    binding!!.checkboxCompleted.isChecked = todo!!.isCompleted

                }
                else -> {
                    hideLoader()
                    handleErrorResponse(it.fullMessages)
                }
            }
        }
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_details)
    }

    fun onButtonClicked(view: View) {
        when {
            binding!!.btnDetailsEditTodo === view -> {
                val intent = Intent()
                intent.component = ComponentName(this, TodoCreateEditActivity::class.java)
                intent.putExtra("TODO_ID", todo!!.id)
                startActivity(intent)
            }
            binding!!.btnDetailsDeleteTodo === view -> delete()
            binding!!.btnDetailsGoHome === view -> finish()
        }
    }


    private fun delete() {

        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Are you sure You want to delete this todo?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                todoDetailsViewModel!!.delete(todoId)
                todoDetailsViewModel!!.todoDeleteOperation.subscribe { response ->
                    if (response.isSuccess) {
                        Toast.makeText(this, "Todo Deleted Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else if (response.isLoading) {
                        displayLoader()
                    } else {
                        if (response.fullMessages != null) {
                            Toast.makeText(this, TextUtils.join(",", response.fullMessages), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

