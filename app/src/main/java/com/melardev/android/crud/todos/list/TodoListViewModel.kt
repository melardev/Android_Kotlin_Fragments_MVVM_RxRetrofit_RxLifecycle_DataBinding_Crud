package com.melardev.android.crud.todos.list

import androidx.lifecycle.*
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.remote.repositories.RetrofitTodoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class TodoListViewModel : ViewModel(), LifecycleObserver {

    private var retrofitTodoRepository: RetrofitTodoRepository =
        RetrofitTodoRepository()
    val todoListObservable = PublishSubject.create<DataSourceOperation<List<Todo>>>()

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        // Schedulers.io() Schedulers.computation() AndroidSchedulers.mainThread()
        disposable.add(retrofitTodoRepository.getAll()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .filter { it.isLoaded }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                todoListObservable.onNext(it)
            }
        )
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
    }
}
