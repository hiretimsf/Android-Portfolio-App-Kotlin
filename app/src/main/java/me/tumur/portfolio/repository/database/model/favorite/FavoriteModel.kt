package me.tumur.portfolio.repository.database.model.favorite

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import java.util.*

@Entity(tableName = DbConstants.FAVORITE, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class FavoriteModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DbConstants.ID) var id: String,
    @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @ColumnInfo(name = DbConstants.SUB_TITLE) var subTitle: String,
    @ColumnInfo(name = DbConstants.LOGO) var logo: String,
    @ColumnInfo(name = DbConstants.LOGO_DESCRIPTION) var logoDescription: String,
    @ColumnInfo(name = DbConstants.COVER_IMAGE) var coverImage: String,
    @ColumnInfo(name = DbConstants.IMAGE_DESCRIPTION) var imageDescription: String,
    @ColumnInfo(name = DbConstants.TEXT) var text: String,
    @ColumnInfo(name = DbConstants.INFO) var info: String,
    @ColumnInfo(name = DbConstants.DATE_FROM) var dateFrom: Date,
    @ColumnInfo(name = DbConstants.DATE_TO) var dateTo: Date,
    @ColumnInfo(name = DbConstants.HEADER) var header: String,
    @ColumnInfo(name = DbConstants.TYPE) var categoryType: Int,
    @ColumnInfo(name = DbConstants.VIDEO_URL) var videoUrl: String?,
    @ColumnInfo(name = DbConstants.ORDERS) var order: Int,
    @ColumnInfo(name = DbConstants.DATE) var date: Date
) : Parcelable