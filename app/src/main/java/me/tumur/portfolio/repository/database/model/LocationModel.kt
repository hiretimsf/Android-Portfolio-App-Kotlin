package me.tumur.portfolio.repository.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants

@Entity(tableName = DbConstants.LOCATION, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class LocationModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerializedName(DbConstants.LATITUDE) @ColumnInfo(name = DbConstants.LATITUDE) var latitude: Double? = null,
    @SerializedName(DbConstants.LONGITUDE) @ColumnInfo(name = DbConstants.LONGITUDE) var longitude: Double? = null
) : Parcelable