package me.tumur.portfolio.repository.database.model.experience

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.IsoDateSerializer
import me.tumur.portfolio.repository.network.FlexibleIntSerializer
import java.util.*

@Entity(tableName = DbConstants.EXPERIENCE, indices = [Index(value = [DbConstants.ID], unique = true)])
@Serializable
data class ExperienceModel(
    @PrimaryKey(autoGenerate = false)
    @SerialName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.COMPANY) @ColumnInfo(name = DbConstants.COMPANY) var company: String,
    @SerialName(DbConstants.INFO) @ColumnInfo(name = DbConstants.INFO) var info: String,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_FROM) @ColumnInfo(name = DbConstants.DATE_FROM) var dateFrom: Date,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_TO) @ColumnInfo(name = DbConstants.DATE_TO) var dateTo: Date,
    @SerialName(DbConstants.LOCATION) @ColumnInfo(name = DbConstants.LOCATION) var location: String,
    @SerialName(DbConstants.LOGO) @ColumnInfo(name = DbConstants.LOGO) var logo: String,
    @SerialName(DbConstants.LOGO_DESCRIPTION) @ColumnInfo(name = DbConstants.LOGO_DESCRIPTION) var logoDescription: String,
    @SerialName(DbConstants.COVER_IMAGE) @ColumnInfo(name = DbConstants.COVER_IMAGE) var coverImage: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
)
