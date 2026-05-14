package hiretimsf.com.app.repository.database

import android.content.Context
import hiretimsf.com.app.R
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel

internal fun Context.welcomeItems(): List<WelcomeModel> {
    return listOf(
        WelcomeModel(
            id = WELCOME_PAGE_HELLO_ID,
            title = getString(R.string.welcome_page_hello_title),
            subTitle = getString(R.string.welcome_page_hello_subtitle),
            text = getString(R.string.welcome_page_hello_text),
            imageDescription = getString(R.string.welcome_page_hello_image_description),
            order = 1,
        ),
        WelcomeModel(
            id = WELCOME_PAGE_BACKGROUND_ID,
            title = getString(R.string.welcome_page_background_title),
            subTitle = getString(R.string.welcome_page_background_subtitle),
            text = getString(R.string.welcome_page_background_text),
            imageDescription = getString(R.string.welcome_page_background_image_description),
            order = 2,
        ),
        WelcomeModel(
            id = WELCOME_PAGE_PASSION_ID,
            title = getString(R.string.welcome_page_passion_title),
            subTitle = getString(R.string.welcome_page_passion_subtitle),
            text = getString(R.string.welcome_page_passion_text),
            imageDescription = getString(R.string.welcome_page_passion_image_description),
            order = 3,
        ),
    )
}

private const val WELCOME_PAGE_HELLO_ID = "hFaAjmnhkoKg9xMshrQf"
private const val WELCOME_PAGE_BACKGROUND_ID = "Pzh7MCH9Obpb4c0CFyOU"
private const val WELCOME_PAGE_PASSION_ID = "hTca83O7HCaPIVuLDgaf"
