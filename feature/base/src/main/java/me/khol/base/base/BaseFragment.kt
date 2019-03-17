package me.khol.base.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment() {

    protected val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}