package hiretimsf.com.app.repository.database.model.welcome

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.repository.network.FlexibleIntSerializer

@Serializable
data class WelcomeModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.SUB_TITLE) var subTitle: String,
    @SerialName(DbConstants.TEXT) var text: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) val imageDescription: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
)
