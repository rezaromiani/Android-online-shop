package com.example.nikestore.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_favorite")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val discount: Int,
    val image: String,
    val previous_price: Int,
    val price: Int,
    val status: Int,
    val title: String
) : Parcelable {
    var isFavorite: Boolean = false
}

const val SORT_LATEST = 0
const val SORT_POPULAR = 1
const val SORT_PRICE_DESC = 2
const val SORT_PRICE_ASC = 3