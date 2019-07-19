package com.melardev.android.crud.todos.write


import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.melardev.android.crud.R
import com.melardev.android.crud.databinding.TodoWriteBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.todos.base.BaseActivity

class TodoCreateEditActivity : BaseActivity() {

    private lateinit var todo: Todo
    private var todoListViewModel: TodoWriteViewModel? = null
    private var editMode: Boolean = false
    private var todoId: Long = 0

    private var binding: TodoWriteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent != null) {
            this.todoId = intent.getLongExtra("TODO_ID", -1)
            this.editMode = todoId.toInt() != -1
        }
        initView()
        initViewModel()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_create_edit)
        if (editMode) {
            binding!!.btnSaveTodo.text = "Save Changes"
        } else {
            binding!!.btnSaveTodo.text = "Create"
        }
    }

    private fun initViewModel() {

        todoListViewModel = ViewModelProviders.of(this)
            .get(TodoWriteViewModel::class.java)

        if (editMode) {
            // Observe the list, if
            todoListViewModel!!.todo.observe(this, Observer { resource ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Looper.getMainLooper().isCurrentThread)
                        throw AssertionError("")
                } else {
                    if (Looper.getMainLooper() != Looper.myLooper())
                        throw AssertionError("")
                }

                when {
                    resource.isLoading -> displayLoader()
                    resource.data != null -> {
                        todo = resource.data
                        binding!!.eTxtTitle.setText(todo.title)
                        binding!!.eTxtDescription.setText(todo.description)
                        binding!!.eCheckboxCompleted.isChecked = todo.isCompleted
                        binding!!.txtId.text = todo.id.toString()
                    }
                    else -> handleErrorResponse(resource.fullMessages)
                }
            })

            todoListViewModel!!.loadTodo(todoId)
        }
    }

    fun saveTodo(view: View) {
        val title = binding!!.eTxtTitle.text.toString()
        val description = binding!!.eTxtDescription.text.toString()
        val completed = binding!!.eCheckboxCompleted.isChecked
        if (editMode) {
            todo.title = title
            todo.description = description
            todo.isCompleted = completed

            todoListViewModel!!.update(todo)

            Toast.makeText(this@TodoCreateEditActivity, "Todo Updated!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            todoListViewModel!!.createTodo(title, description)
        }

        todoListViewModel!!.writeTodoOperation.observe(this, Observer { resource ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Looper.getMainLooper().isCurrentThread)
                    throw AssertionError("")
            } else {
                if (Looper.getMainLooper() != Looper.myLooper())
                    throw AssertionError("")
            }

            when {
                resource.isLoading -> displayLoader()
                resource.data != null -> {
                    hideLoader()
                    if (editMode)
                        Toast.makeText(this@TodoCreateEditActivity, "Todo Updated!", Toast.LENGTH_LONG).show()
                    else {
                        Toast.makeText(this@TodoCreateEditActivity, "Todo Created!", Toast.LENGTH_LONG).show()
                        intent.putExtra("todo", todo)
                        setResult(RESULT_OK, intent)
                    }
                    finish()
                }
                else -> {
                    hideLoader()
                    handleErrorResponse(resource.fullMessages)
                }
            }
        })
    }
}
