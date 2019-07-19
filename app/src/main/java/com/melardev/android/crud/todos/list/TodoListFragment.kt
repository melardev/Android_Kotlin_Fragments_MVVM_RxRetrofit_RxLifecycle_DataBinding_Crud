package com.melardev.android.crud.todos.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melardev.android.crud.R

import com.melardev.android.crud.databinding.TodoListFragmentBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.todos.show.TodoDetailsActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity


class TodoListFragment : Fragment(), TodoListAdapter.TodoRowEventListener {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            TodoListFragment().apply {
                arguments = bundle
            }
    }

    private var binding: TodoListFragmentBinding? = null

    private var todoListViewModel: TodoListViewModel? = null

    private var todoListAdapter: TodoListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todoListAdapter = TodoListAdapter(this)
        binding!!.rvTodos.layoutManager =
            LinearLayoutManager(activity!!.applicationContext, RecyclerView.VERTICAL, false)
        binding!!.rvTodos.adapter = todoListAdapter

        initViewModel()
    }

    @SuppressLint("CheckResult")
    private fun initViewModel() {

        todoListViewModel = ViewModelProviders.of(this)
            .get(TodoListViewModel::class.java)

        lifecycle.addObserver(todoListViewModel!!)

        // Observe the list, if
        todoListViewModel!!.todoListObservable
            .subscribe {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Looper.getMainLooper().isCurrentThread)
                        throw AssertionError("")
                } else {
                    if (Looper.getMainLooper() != Looper.myLooper())
                        throw AssertionError("")
                }
                if (it.isLoading) {
                    displayLoader()
                } else if (it.data != null && it.data.isNotEmpty()) {
                    populateRecyclerView(it.data)
                } else
                    handleErrorResponse(it.fullMessages)
            }

    }

    private fun displayLoader() {
        binding!!.rvTodos.visibility = View.GONE
    }

    private fun hideLoader() {
        binding!!.rvTodos.visibility = View.VISIBLE
    }

    private fun populateRecyclerView(todos: List<Todo>?) {
        hideLoader()
        binding!!.rvTodos.visibility = View.VISIBLE
        todoListAdapter!!.setItems(todos)
    }

    private fun handleErrorResponse(errors: Array<String>?) {
        Toast.makeText(activity, TextUtils.join(",", errors), Toast.LENGTH_LONG)
            .show()
        hideLoader()
        binding!!.rvTodos.visibility = View.GONE
    }

    fun createTodo(view: View) {
        val intent = Intent(activity, TodoCreateEditActivity::class.java)
        startActivity(intent)
    }

    override fun onClicked(todo: Todo) {
        val intent = Intent(activity, TodoDetailsActivity::class.java)
        intent.putExtra("TODO_ID", todo.id)
        startActivity(intent)
    }
}