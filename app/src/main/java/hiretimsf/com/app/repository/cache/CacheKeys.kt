package hiretimsf.com.app.repository.cache

object CacheKeys {
    const val ABOUT = "about"
    const val BLOG_POSTS = "blog_posts"
    const val PORTFOLIO = "portfolio"
    const val PORTFOLIO_BUTTONS = "portfolio_buttons"
    const val PORTFOLIO_CATEGORIES = "portfolio_categories"
    const val PORTFOLIO_SCREENSHOTS = "portfolio_screenshots"

    fun blogPost(slug: String): String = "blog_post:$slug"
}
