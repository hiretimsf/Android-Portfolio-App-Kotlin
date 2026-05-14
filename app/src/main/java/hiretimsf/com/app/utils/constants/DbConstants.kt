package hiretimsf.com.app.utils.constants

object DbConstants {

    /** DATABASE CONSTANTS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    const val DATABASE_NAME = "database.db"
    const val PERSON_ID = "t8AFhsy1JQjxdjjmnYLK"

    /** TABLE CONSTANTS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    const val WELCOME="welcome"
    const val PROFILE = "profile"
    const val SOCIAL = "social"
    const val ABOUT = "about"
    const val APP = "app"
    const val PORTFOLIO = "portfolio"
    const val EXPERIENCE = "experience"
    const val TASKS = "tasks"
    const val BUTTON = "button"
    const val CATEGORY = "category"
    const val SCREENSHOT = "screenshot"
    const val FAVORITE = "favorite"
    const val RESOURCE = "resource"

    /** DATABASE POPULATION CONSTANTS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    const val WELCOME_JSON = "welcome.json"
    const val PROFILE_JSON = "profile.json"
    const val ABOUT_JSON = "about.json"
    const val SOCIAL_JSON = "social.json"
    const val BUTTON_JSON = "button.json"
    const val TASK_JSON = "task.json"
    const val PORTFOLIO_JSON = "portfolio.json"
    const val EXPERIENCE_JSON = "experience.json"
    const val CATEGORY_JSON = "category.json"
    const val SCREENSHOT_JSON = "screenshot.json"
    const val RESOURCE_JSON = "resource.json"
    const val APP_JSON = "app.json"

    /** COLUMNS CONSTANTS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    const val ID = "id"
    const val OWNER_ID = "owner_id"
    const val NAME = "name"
    const val TITLE = "title"
    const val SUB_TITLE = "sub_title"
    const val TEXT = "text"
    const val DATE = "date"
    const val URL = "url"
    const val LOGO = "logo"
    const val LOGO_DESCRIPTION = "logo_description"
    const val IMAGE = "image"
    const val COVER_IMAGE = "cover_image"
    const val IMAGE_DESCRIPTION = "image_description"
    const val ICON = "icon"
    const val ICON_DESCRIPTION = "icon_description"
    const val LOCATION = "location"
    const val INFO = "info"
    const val DATE_FROM = "date_from"
    const val DATE_TO = "date_to"
    const val GREETING = "greeting"
    const val EMAIL = "email"
    const val COMPANY = "company"
    const val HEADER = "header"
    const val TASK = "task"
    const val ORDER = "order"
    const val ORDERS = "orders"
    const val TYPE = "type"
    const val DESCRIPTION = "description"
    const val VIDEO_URL = "video_url"
    const val QUERY = "query"
    const val LINK_TO_SHARE = "link_to_share"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"

    /** QUERIES'S CONSTANTS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Welcome table's queries ----------------------------------------------------------------- */
    const val WELCOME_DELETE = "DELETE FROM $WELCOME"
    const val WELCOME_CHECK = "SELECT COUNT(*) FROM $WELCOME"
    const val WELCOME_GET_SINGLE_ITEM = "SELECT * FROM $WELCOME WHERE $ID=:$ID"
    const val WELCOME_GET_LIST_ITEMS = "SELECT * FROM $WELCOME ORDER BY $ORDERS"

    /** PROFILE QUERIES --------------------------------------------------------------------------------------------- */
    const val PROFILE_DELETE = "DELETE FROM $PROFILE"
    const val PROFILE_GET_SINGLE_ITEM = "SELECT * FROM $PROFILE WHERE $ID=:$ID"


    /** ABOUT QUERIES ----------------------------------------------------------------------------------------------- */
    const val ABOUT_DELETE = "DELETE FROM $ABOUT"
    const val ABOUT_GET_LIST_ITEMS = "SELECT * FROM $ABOUT WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"

    /** SOCIAL QUERIES ---------------------------------------------------------------------------------------------- */
    const val SOCIAL_DELETE = "DELETE FROM $SOCIAL"
    const val SOCIAL_GET_LIST_ITEMS = "SELECT * FROM $SOCIAL WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"

    /** APP QUERIES ------------------------------------------------------------------------------------------------- */
    const val APP_DELETE = "DELETE FROM $APP"
    const val APP_GET_LIST_ITEMS = "SELECT * FROM $APP"

    /** PORTFOLIO QUERIES ------------------------------------------------------------------------------------------- */
    const val PORTFOLIO_DELETE = "DELETE FROM $PORTFOLIO"
    const val PORTFOLIO_GET_LIST_ITEMS = "SELECT * FROM $PORTFOLIO WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"
    const val PORTFOLIO_GET_SINGLE_ITEM = "SELECT * FROM $PORTFOLIO WHERE $ID=:$ID"
    const val GET_PORTFOLIO_BY_QUERY = "SELECT * FROM $PORTFOLIO WHERE $TITLE MATCH $QUERY"

    /** BUTTON QUERIES ---------------------------------------------------------------------------------------------- */
    const val BUTTON_DELETE = "DELETE FROM $BUTTON"
    const val BUTTON_GET_LIST_ITEMS = "SELECT * FROM $BUTTON WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"

    /** CATEGORY QUERIES -------------------------------------------------------------------------------------------- */
    const val CATEGORY_DELETE = "DELETE FROM $CATEGORY"
    const val CATEGORY_GET_LIST_ITEMS = "SELECT * FROM $CATEGORY WHERE $TYPE=:$TYPE ORDER BY $ORDERS"

    /** SCREENSHOT QUERIES ------------------------------------------------------------------------------------------ */
    const val SCREENSHOT_DELETE = "DELETE FROM $SCREENSHOT"
    const val SCREENSHOT_GET_LIST_ITEMS = "SELECT * FROM $SCREENSHOT WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"

    /** EXPERIENCE QUERIES ------------------------------------------------------------------------------------------ */
    const val EXPERIENCE_DELETE = "DELETE FROM $EXPERIENCE"
    const val EXPERIENCE_GET_LIST_ITEMS = "SELECT * FROM $EXPERIENCE WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"
    const val EXPERIENCE_GET_SINGLE_ITEM = "SELECT * FROM $EXPERIENCE WHERE $ID=:$ID"

    /** TASK QUERIES ------------------------------------------------------------------------------------------------ */
    const val TASK_DELETE = "DELETE FROM $TASKS"
    const val TASK_GET_LIST_ITEMS = "SELECT * FROM $TASKS WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"

    /** FAVORITE QUERIES -------------------------------------------------------------------------------------------- */
    const val FAVORITE_DELETE = "DELETE FROM $FAVORITE"
    const val FAVORITE_DELETE_SINGLE_ITEM = "DELETE FROM $FAVORITE WHERE $ID=:$ID"
    const val FAVORITE_GET_SINGLE_ITEM = "SELECT * FROM $FAVORITE WHERE $ID=:$ID"
    const val FAVORITE_GET_LIST_ITEMS = "SELECT * FROM $FAVORITE ORDER BY $DATE"
    const val FAVORITE_EXIST_SINGLE_ITEM = "SELECT COUNT($ID) FROM $FAVORITE WHERE $ID=:$ID"
    const val FAVORITE_GET_MAX_ORDER = "SELECT * FROM $FAVORITE ORDER BY $ORDERS DESC LIMIT 1"
    const val FAVORITE_CHECK = "SELECT COUNT($ID) FROM $FAVORITE"

    /** LOCATION QUERIES ------------------------------------------------------------------------------------------------ */
    const val LOCATION_DELETE = "DELETE FROM $LOCATION"
    const val LOCATION_GET_SINGLE_ITEM = "SELECT * FROM $LOCATION WHERE $OWNER_ID=:$ID"

    /** RESOURCE QUERIES ------------------------------------------------------------------------------------------------ */
    const val RESOURCE_DELETE = "DELETE FROM $RESOURCE"
    const val RESOURCE_GET_LIST_ITEMS = "SELECT * FROM $RESOURCE WHERE $OWNER_ID=:$ID ORDER BY $ORDERS"
    const val RESOURCE_CHECK = "SELECT COUNT($ID) FROM $RESOURCE WHERE $OWNER_ID=:$ID"
}