package me.tumur.portfolio.repository.database.model.button

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

@Entity(tableName = DbConstants.BUTTON, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
@Serializable
data class ButtonModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String,
    @SerialName(DbConstants.TYPE) @ColumnInfo(name = DbConstants.TYPE) var type: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
): Parcelable
