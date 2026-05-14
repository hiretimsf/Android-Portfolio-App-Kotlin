package hiretimsf.com.app.screens.welcome.pager

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiretimsf.com.app.repository.database.model.welcome.WelcomeModel

class WelcomePagerViewModel : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Pager' position */
    private val _position = MutableStateFlow(0)
    val positionFlow: StateFlow<Int> = _position.asStateFlow()
    val position: Int get() = _position.value

    /** Screen's data */
    private val _data = MutableStateFlow<WelcomeModel?>(null)
    val dataFlow: StateFlow<WelcomeModel?> = _data.asStateFlow()
    val data: WelcomeModel? get() = _data.value

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set pager's position
     * */
    fun setPosition(position: Int){
        _position.value = position
    }

    /**
     * Set screen's data
     * */
    fun setData(model: WelcomeModel){
        _data.value = model
    }

}
