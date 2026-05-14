package hiretimsf.com.app.repository.database.model.settings

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.repository.network.FlexibleIntSerializer

@Parcelize
@Serializable
data class AppModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.TEXT) var text: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
): Parcelable
