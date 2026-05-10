package me.tumur.portfolio.repository.database.model.screenshot

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

@Entity(tableName = DbConstants.SCREENSHOT, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
@Serializable
data class ScreenShotModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String,
    @SerialName(DbConstants.DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
) : Parcelable
