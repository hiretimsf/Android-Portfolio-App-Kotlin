package me.tumur.portfolio.repository.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants

@Entity(tableName = DbConstants.LOCATION, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
@Serializable
data class LocationModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.LATITUDE) @ColumnInfo(name = DbConstants.LATITUDE) var latitude: Double? = null,
    @SerialName(DbConstants.LONGITUDE) @ColumnInfo(name = DbConstants.LONGITUDE) var longitude: Double? = null
) : Parcelable
