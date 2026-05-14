package hiretimsf.com.app.repository.database.model.portfolio

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import hiretimsf.com.app.utils.constants.DbConstants
import hiretimsf.com.app.repository.network.IsoDateSerializer
import hiretimsf.com.app.repository.network.FlexibleIntSerializer
import java.util.*

@Parcelize
@Serializable
data class PortfolioModel(
    @SerialName(DbConstants.ID) var id: String,
    @SerialName(DbConstants.OWNER_ID) var ownerId: String,
    @SerialName(DbConstants.TITLE) var title: String,
    @SerialName(DbConstants.SUB_TITLE) var subTitle: String,
    @SerialName(DbConstants.LOGO) var logo: String,
    @SerialName(DbConstants.LOGO_DESCRIPTION) var logoDescription: String,
    @SerialName(DbConstants.COVER_IMAGE) var coverImage: String,
    @SerialName(DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @SerialName(DbConstants.TEXT) var text: String,
    @SerialName(DbConstants.INFO) var info: String,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_FROM) var dateFrom: Date,
    @Serializable(with = IsoDateSerializer::class)
    @SerialName(DbConstants.DATE_TO) var dateTo: Date,
    @SerialName(DbConstants.HEADER) var header: String,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.TYPE) var categoryType: Int,
    @SerialName(DbConstants.VIDEO_URL) var videoUrl: String?,
    @SerialName(DbConstants.LINK_TO_SHARE) var linkToShare: String?,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName(DbConstants.ORDER) var order: Int
) : Parcelable
