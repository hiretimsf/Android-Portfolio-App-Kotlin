package me.tumur.portfolio.repository.database.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Serializable
data class AboutModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.HEADER) var header: String,
    @SerialName(DbConstants.TEXT) var text: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
)
