package me.tumur.portfolio.repository.database.model.category

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants

@Entity(tableName = DbConstants.CATEGORY, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class CategoryModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerializedName(DbConstants.TYPE) @ColumnInfo(name = DbConstants.TYPE) var type: Int,
    @SerializedName(DbConstants.ICON) @ColumnInfo(name = DbConstants.ICON) var icon: String,
    @SerializedName(DbConstants.ICON_DESCRIPTION) @ColumnInfo(name = DbConstants.ICON_DESCRIPTION) var iconDescription: String,
    @SerializedName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
) : Parcelable