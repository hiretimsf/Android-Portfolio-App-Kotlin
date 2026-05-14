package hiretimsf.com.app.screens.experience

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiretimsf.com.app.repository.database.dao.experience.ExperienceDao
import hiretimsf.com.app.repository.database.model.experience.ExperienceModel
import hiretimsf.com.app.utils.constants.DbConstants
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val experienceDao: ExperienceDao
) : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Selected item id */
    private val _selectedItem = MutableStateFlow<ExperienceModel?>(null)
    val selectedItemFlow: StateFlow<ExperienceModel?> = _selectedItem.asStateFlow()

    /** Portfolio pager data */
    private val config = PagingConfig(pageSize = 10, enablePlaceholders = true, initialLoadSize = 5)

    val data: Flow<PagingData<ExperienceModel>> = Pager(config) {
        experienceDao.getListItems(DbConstants.PERSON_ID)
    }.flow.cachedIn(viewModelScope)

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set selected item
     * */
    fun setSelectedItem(item: ExperienceModel?) {
        _selectedItem.value = item
    }
}
