package me.tumur.portfolio.repository.database.model.category

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Entity(tableName = DbConstants.CATEGORY, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
@Serializable
data class CategoryModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.TYPE) @ColumnInfo(name = DbConstants.TYPE) var type: Int,
    @SerialName(DbConstants.ICON) @ColumnInfo(name = DbConstants.ICON) var icon: String,
    @SerialName(DbConstants.ICON_DESCRIPTION) @ColumnInfo(name = DbConstants.ICON_DESCRIPTION) var iconDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
) : Parcelable
