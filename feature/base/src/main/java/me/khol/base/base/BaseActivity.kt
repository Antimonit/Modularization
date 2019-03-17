package me.khol.base.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}