package com.melardev.android.crud.todos.show

import androidx.lifecycle.*
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import com.melardev.android.crud.datasource.remote.repositories.RetrofitTodoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TodoDetailsViewModel(private val todoId: Long) : ViewModel(), LifecycleObserver {

    private val todoRepository = RetrofitTodoRepository()
    internal val todoLoadOperation = PublishSubject.create<DataSourceOperation<Todo>>()
    internal val todoDeleteOperation = PublishSubject.create<DataSourceOperation<SuccessResponse>>()

    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        disposable.add(todoRepository.getById(todoId)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .filter { it.isLoaded }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { dataSourceOperation -> todoLoadOperation.onNext(dataSourceOperation) })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
    }

    fun delete(todoId: Long) {
        disposable.add(
            todoRepository
                .delete(todoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dataSourceOperation -> todoDeleteOperation.onNext(dataSourceOperation) }
        )
    }

    class Factory(private val todoId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = TodoDetailsViewModel(todoId) as T
    }

}

