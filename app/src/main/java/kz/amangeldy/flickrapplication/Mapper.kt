package kz.amangeldy.flickrapplication

interface Mapper<in FROM, out TO> {

    operator fun invoke(from: FROM): TO
}