package me.tumur.portfolio.repository.database.model.button

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Parcelize
@Serializable
data class ButtonModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.URL) var url: String,
    @SerialName(DbConstants.TYPE) var type: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
): Parcelable
