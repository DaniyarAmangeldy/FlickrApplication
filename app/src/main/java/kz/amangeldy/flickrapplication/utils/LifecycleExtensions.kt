package kz.amangeldy.flickrapplication.utils

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(@NonNull owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer { it?.let(observer) })
}
 