package me.tumur.portfolio.repository.database.model.profile

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants

@Entity(tableName = DbConstants.SOCIAL, indices = [Index(value = [DbConstants.ID, DbConstants.OWNER_ID], unique = true)])
@Parcelize
data class SocialModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerializedName(DbConstants.NAME) @ColumnInfo(name = DbConstants.NAME) var name: String,
    @SerializedName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String,
    @SerializedName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
): Parcelable