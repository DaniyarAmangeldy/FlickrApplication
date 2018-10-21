package kz.amangeldy.flickrapplication.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import kz.amangeldy.flickrapplication.data.entity.SearchQueryRoomModel

@Dao
interface SearchQueryDAO {

    @Query("SELECT * FROM SearchQueryRoomModel")
    fun getAllSearchQueries(): Flowable<List<SearchQueryRoomModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchQuery(query: SearchQueryRoomModel)
}