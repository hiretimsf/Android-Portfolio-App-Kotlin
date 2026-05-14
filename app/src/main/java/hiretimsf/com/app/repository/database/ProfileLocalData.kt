package hiretimsf.com.app.repository.database

import android.content.Context
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.profile.ProfileModel
import hiretimsf.com.app.repository.database.model.profile.SocialModel
import hiretimsf.com.app.utils.constants.DbConstants

internal fun Context.profileItems(): List<ProfileModel> {
    return listOf(
        ProfileModel(
            id = DbConstants.PERSON_ID,
            greeting = getString(R.string.profile_greeting),
            name = getString(R.string.name),
            title = getString(R.string.title),
            image = getString(R.string.profile_image_url),
            imageDescription = getString(R.string.profile_image_description),
            email = getString(R.string.contact_email),
            order = 1,
        ),
    )
}

internal fun Context.profileSocialItems(): List<SocialModel> {
    return listOf(
        profileSocialItem(
            id = PROFILE_SOCIAL_X_ID,
            name = getString(R.string.profile_social_x_label),
            url = getString(R.string.profile_social_x_url),
            order = 1,
        ),
        profileSocialItem(
            id = PROFILE_SOCIAL_GITHUB_ID,
            name = getString(R.string.profile_social_github_label),
            url = getString(R.string.profile_social_github_url),
            order = 2,
        ),
        profileSocialItem(
            id = PROFILE_SOCIAL_LINKEDIN_ID,
            name = getString(R.string.profile_social_linkedin_label),
            url = getString(R.string.profile_social_linkedin_url),
            order = 3,
        ),
        profileSocialItem(
            id = PROFILE_SOCIAL_FACEBOOK_ID,
            name = getString(R.string.profile_social_facebook_label),
            url = getString(R.string.profile_social_facebook_url),
            order = 4,
        ),
        profileSocialItem(
            id = PROFILE_SOCIAL_STRAVA_ID,
            name = getString(R.string.profile_social_strava_label),
            url = getString(R.string.profile_social_strava_url),
            order = 5,
        ),
    )
}

private fun profileSocialItem(
    id: String,
    name: String,
    url: String,
    order: Int,
): SocialModel {
    return SocialModel(
        id = id,
        ownerId = DbConstants.PERSON_ID,
        name = name,
        url = url,
        order = order,
    )
}

private const val PROFILE_SOCIAL_X_ID = "profile_social_x"
private const val PROFILE_SOCIAL_GITHUB_ID = "profile_social_github"
private const val PROFILE_SOCIAL_LINKEDIN_ID = "profile_social_linkedin"
private const val PROFILE_SOCIAL_FACEBOOK_ID = "profile_social_facebook"
private const val PROFILE_SOCIAL_STRAVA_ID = "profile_social_strava"
