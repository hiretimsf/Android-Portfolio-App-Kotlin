package hiretimsf.com.app.utils.privacy

import android.content.Context
import androidx.core.content.edit
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import hiretimsf.com.app.utils.constants.Constants
import java.util.UUID

object DataDeletionIdentifier {
    private const val KEY_DATA_DELETION_ID = "data_deletion_id"
    private const val PREFIX = "htsf"

    fun get(context: Context): String {
        val preferences = context.getSharedPreferences(Constants.APP, Context.MODE_PRIVATE)
        val existingId = preferences.getString(KEY_DATA_DELETION_ID, null)

        if (!existingId.isNullOrBlank()) {
            return existingId
        }

        val newId = "$PREFIX-${UUID.randomUUID()}"
        preferences.edit {
            putString(KEY_DATA_DELETION_ID, newId)
        }
        return newId
    }

    fun applyToFirebase(context: Context) {
        val deletionId = get(context)
        FirebaseAnalytics.getInstance(context).setUserId(deletionId)
        FirebaseCrashlytics.getInstance().setUserId(deletionId)
    }
}
