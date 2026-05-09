package me.tumur.portfolio.repository.database.model.button

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import me.tumur.portfolio.utils.constants.DbConstants

@Entity(tableName = DbConstants.BUTTON, indices = [Index(value = [DbConstants.ID], unique = true)])
@Parcelize
data class ButtonModel(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(DbConstants.ID) @ColumnInfo(name = DbConstants.ID) var id: String,
    @SerializedName(DbConstants.OWNER_ID) @ColumnInfo(name = DbConstants.OWNER_ID) var ownerId: String,
    @SerializedName(DbConstants.TITLE) @ColumnInfo(name = DbConstants.TITLE) var title: String,
    @SerializedName(DbConstants.URL) @ColumnInfo(name = DbConstants.URL) var url: String,
    @SerializedName(DbConstants.TYPE) @ColumnInfo(name = DbConstants.TYPE) var type: String,
    @SerializedName(DbConstants.ORDER) @ColumnInfo(name = DbConstants.ORDERS) var order: Int
): Parcelable