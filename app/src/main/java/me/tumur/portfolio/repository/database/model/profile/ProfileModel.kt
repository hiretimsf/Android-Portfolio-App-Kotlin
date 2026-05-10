package me.tumur.portfolio.repository.database.model.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Entity(
    tableName = DbConstants.PROFILE,
    indices = [Index(value = [DbConstants.ID, DbConstants.NAME], unique = true)]
)
@Serializable
data class ProfileModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.GREETING) @ColumnInfo(name = DbConstants.GREETING) var greeting: String,
    @SerialName(DbConstants.NAME) @ColumnInfo(name = DbConstants.NAME) var name: String,
    @SerialName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.IMAGE) @ColumnInfo(name = DbConstants.IMAGE) var image: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @SerialName(DbConstants.EMAIL) @ColumnInfo(name = DbConstants.EMAIL) var email: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
)
