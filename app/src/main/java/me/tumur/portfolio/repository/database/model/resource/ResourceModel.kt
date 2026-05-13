package me.tumur.portfolio.repository.database.model.resource

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.IsoDateSerializer
import me.tumur.portfolio.repository.network.FlexibleIntSerializer
import java.util.*

@Parcelize
@Serializable
data class ResourceModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.IMAGE) var image: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_FROM) var dateFrom: Date,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_TO) var dateTo: Date,
    @SerialName(DbConstants.URL) var url: String?,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
) : Parcelable
