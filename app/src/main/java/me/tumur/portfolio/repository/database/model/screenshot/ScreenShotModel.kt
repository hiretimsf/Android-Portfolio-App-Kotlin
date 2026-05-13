package me.tumur.portfolio.repository.database.model.screenshot

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Parcelize
@Serializable
data class ScreenShotModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.URL) var url: String,
    @SerialName(DbConstants.DESCRIPTION) var imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
) : Parcelable
