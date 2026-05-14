package hiretimsf.com.app.utils.adapters.listItemAdapters.portfolio

import hiretimsf.com.app.repository.database.model.portfolio.PortfolioModel

class PortfolioClickListener(val clickListener: (item: PortfolioModel) -> Unit) {
    fun onClick(item: PortfolioModel) = clickListener(item)
}