package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio.screenshot

import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel

class ScreenShotClickListener(val clickListener: (model: ScreenShotModel) -> Unit) {
    fun onClick(model: ScreenShotModel) = clickListener(model)
}