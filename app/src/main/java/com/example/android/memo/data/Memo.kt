package com.example.android.memo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "memos")
data class Memo @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "created_at") var createdAt: String = "",
    @ColumnInfo(name = "updated_at") var updateAt: String = "",
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {

    val titleForList: String
        get() = title.ifEmpty { content }

    val isActive
        get() = !isFavorite

    val isEmpty
        get() = title.isEmpty() || content.isEmpty()

}
