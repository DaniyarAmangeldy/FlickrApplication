package kz.amangeldy.flickrapplication.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel


@Database(entities = [SearchQueryRoomModel::class], version = 1)
abstract class FlickrDatabase: RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDAO
}