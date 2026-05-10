package me.tumur.portfolio.repository.database.model.profile

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

@Entity(tableName = DbConstants.SOCIAL, indices = [Index(value = [DbConstants.ID, DbConstants.OWNER_ID], unique = true)])
@Parcelize
@Serializable
data class SocialModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.NAME) @ColumnInfo(name = DbConstants.NAME) var name: String,
    @SerialName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
): Parcelable
