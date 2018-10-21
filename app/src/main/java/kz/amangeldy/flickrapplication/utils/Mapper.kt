package kz.amangeldy.flickrapplication.utils

interface Mapper<in FROM, out TO> {

    operator fun invoke(from: FROM): TO
}