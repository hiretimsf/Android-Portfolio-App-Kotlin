package me.tumur.portfolio.repository.database.model.welcome

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Entity(tableName = DbConstants.WELCOME, indices = [Index(value = [DbConstants.ID], unique = true)])
@Serializable
data class WelcomeModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.SUB_TITLE) @ColumnInfo(name = DbConstants.SUB_TITLE) var subTitle: String,
    @SerialName(DbConstants.TEXT) @ColumnInfo(name = DbConstants.TEXT) var text: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) val imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
)
