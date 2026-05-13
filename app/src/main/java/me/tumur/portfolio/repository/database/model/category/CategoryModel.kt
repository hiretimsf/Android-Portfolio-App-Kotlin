package me.tumur.portfolio.repository.database.model.category

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import me.tumur.portfolio.repository.network.FlexibleIntSerializer

@Parcelize
@Serializable
data class CategoryModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.TYPE) var type: Int,
    @SerialName(DbConstants.ICON) var icon: String,
    @SerialName(DbConstants.ICON_DESCRIPTION) var iconDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
) : Parcelable
