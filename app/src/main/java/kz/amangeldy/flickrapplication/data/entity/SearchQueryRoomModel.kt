package kz.amangeldy.flickrapplication.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["query"], unique = true)])
data class SearchQueryRoomModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val query: String
)