package kz.amangeldy.flickrapplication.utils


fun <T> MutableList<T>.removeIfInstance(predicate: (item: T) -> Boolean) {

    val index = this.indexOfFirst(predicate)
    if (index >= 0) {
        this.removeAt(index)
    }
}