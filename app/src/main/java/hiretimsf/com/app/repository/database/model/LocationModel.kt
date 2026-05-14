package hiretimsf.com.app.repository.database.model

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import hiretimsf.com.app.utils.constants.DbConstants

@Parcelize
@Serializable
data class LocationModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.LATITUDE) var latitude: Double? = null,
    @SerialName(DbConstants.LONGITUDE) var longitude: Double? = null
) : Parcelable
