package me.tumur.portfolio.repository.database.model.favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants
import java.util.*

@Parcelize
data class FavoriteModel(
    var id: String,
    var ownerId: String,
    var title: String,
    var subTitle: String,
    var logo: String,
    var logoDescription: String,
    var coverImage: String,
    var imageDescription: String,
    var text: String,
    var info: String,
    var dateFrom: Date,
    var dateTo: Date,
    var header: String,
    var categoryType: Int,
    var videoUrl: String?,
    var order: Int,
    var date: Date
) : Parcelable
