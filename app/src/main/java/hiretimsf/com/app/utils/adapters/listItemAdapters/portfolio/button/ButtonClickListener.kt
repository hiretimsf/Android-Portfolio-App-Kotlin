package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.button

class ButtonClickListener(val clickListener: (url: String) -> Unit) {
    fun onClick(url: String) = clickListener(url)
}