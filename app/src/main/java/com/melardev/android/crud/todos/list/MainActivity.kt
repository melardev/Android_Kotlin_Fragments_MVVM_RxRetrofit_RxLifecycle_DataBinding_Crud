package com.melardev.android.crud.todos.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.melardev.android.crud.R
import com.melardev.android.crud.extensions.replaceFragmentInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragmentInActivity(TodoListFragment.newInstance(Bundle()), R.id.containerFragment)
    }
}
