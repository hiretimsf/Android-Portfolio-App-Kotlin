package me.tumur.portfolio.repository.database.model.resource

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import java.util.*

@Entity(tableName = DbConstants.RESOURCE, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class ResourceModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerializedName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerializedName(DbConstants.IMAGE) @ColumnInfo(name = DbConstants.IMAGE) var image: String,
    @SerializedName(DbConstants.IMAGE_DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @SerializedName(DbConstants.DATE_FROM) @ColumnInfo(name = DbConstants.DATE_FROM) var dateFrom: Date,
    @SerializedName(DbConstants.DATE_TO) @ColumnInfo(name = DbConstants.DATE_TO) var dateTo: Date,
    @SerializedName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String?,
    @SerializedName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
) : Parcelable