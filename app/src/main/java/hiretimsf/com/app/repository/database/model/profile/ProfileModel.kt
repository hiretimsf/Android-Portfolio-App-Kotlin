package hiretimsf.com.app.repository.database.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.repository.network.FlexibleIntSerializer

@Serializable
data class ProfileModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.GREETING) var greeting: String,
    @SerialName(DbConstants.NAME) var name: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.IMAGE) var image: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @SerialName(DbConstants.EMAIL) var email: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
)
