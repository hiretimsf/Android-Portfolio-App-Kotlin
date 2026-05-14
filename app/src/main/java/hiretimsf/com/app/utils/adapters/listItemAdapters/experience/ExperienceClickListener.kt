package hiretimsf.com.app.utils.adapters.listItemAdapters.experience

import hiretimsf.com.app.repository.database.model.experience.ExperienceModel

class ExperienceClickListener(val clickListener: (item: ExperienceModel) -> Unit) {
    fun onClick(item: ExperienceModel) = clickListener(item)
}