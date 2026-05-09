package me.tumur.portfolio.repository.database.model.portfolio

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import java.util.*

@Entity(tableName = DbConstants.PORTFOLIO, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class PortfolioModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerializedName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerializedName(DbConstants.SUB_TITLE) @ColumnInfo(name = DbConstants.SUB_TITLE) var subTitle: String,
    @SerializedName(DbConstants.LOGO) @ColumnInfo(name = DbConstants.LOGO) var logo: String,
    @SerializedName(DbConstants.LOGO_DESCRIPTION) @ColumnInfo(name = DbConstants.LOGO_DESCRIPTION) var logoDescription: String,
    @SerializedName(DbConstants.COVER_IMAGE) @ColumnInfo(name = DbConstants.COVER_IMAGE) var coverImage: String,
    @SerializedName(DbConstants.IMAGE_DESCRIPTION) @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @SerializedName(DbConstants.TEXT) @ColumnInfo(name = DbConstants.TEXT) var text: String,
    @SerializedName(DbConstants.INFO) @ColumnInfo(name = DbConstants.INFO) var info: String,
    @SerializedName(DbConstants.DATE_FROM) @ColumnInfo(name = DbConstants.DATE_FROM) var dateFrom: Date,
    @SerializedName(DbConstants.DATE_TO) @ColumnInfo(name = DbConstants.DATE_TO) var dateTo: Date,
    @SerializedName(DbConstants.HEADER) @ColumnInfo(name = DbConstants.HEADER) var header: String,
    @SerializedName(DbConstants.TYPE) @ColumnInfo(name = DbConstants.TYPE) var categoryType: Int,
    @SerializedName(DbConstants.VIDEO_URL) @ColumnInfo(name = DbConstants.VIDEO_URL) var videoUrl: String?,
    @SerializedName(DbConstants.LINK_TO_SHARE) @ColumnInfo(name = DbConstants.LINK_TO_SHARE) var linkToShare: String?,
    @SerializedName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
) : Parcelable