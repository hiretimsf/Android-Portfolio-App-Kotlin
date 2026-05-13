package me.tumur.portfolio.repository.database.model.experience

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.IsoDateSerializer
import me.tumur.portfolio.repository.network.FlexibleIntSerializer
import java.util.*

@Serializable
data class ExperienceModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.COMPANY) var company: String,
    @SerialName(DbConstants.INFO) var info: String,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_FROM) var dateFrom: Date,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_TO) var dateTo: Date,
    @SerialName(DbConstants.LOCATION) var location: String,
    @SerialName(DbConstants.LOGO) var logo: String,
    @SerialName(DbConstants.LOGO_DESCRIPTION) var logoDescription: String,
    @SerialName(DbConstants.COVER_IMAGE) var coverImage: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
)
