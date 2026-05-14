package hiretimsf.com.app.utils.adapters.listItemAdapters.favorite

import hiretimsf.com.app.repository.database.model.favorite.FavoriteModel

class FavoriteClickListener(val clickListener: (item: FavoriteModel, delete: Boolean) -> Unit) {
    fun onClick(item: FavoriteModel, delete: Boolean) = clickListener(item, delete)
}