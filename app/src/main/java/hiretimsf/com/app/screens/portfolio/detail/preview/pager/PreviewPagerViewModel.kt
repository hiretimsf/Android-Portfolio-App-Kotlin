package hiretimsf.com.app.screens.portfolio.detail.preview.pager

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiretimsf.com.app.repository.database.model.screenshot.ScreenShotModel
import hiretimsf.com.app.utils.state.PreviewState
import hiretimsf.com.app.utils.state.ProgressBar

class PreviewPagerViewModel : ViewModel() {

    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Position */
    private val _position = MutableStateFlow(0)
    val positionFlow: StateFlow<Int> = _position.asStateFlow()
    val position: Int get() = _position.value

    /** Screenshot */
    private val _data = MutableStateFlow<ScreenShotModel?>(null)
    val dataFlow: StateFlow<ScreenShotModel?> = _data.asStateFlow()
    val data: ScreenShotModel? get() = _data.value

    /** State */
    private val _state = MutableStateFlow<PreviewState>(ProgressBar)
    val stateFlow: StateFlow<PreviewState> = _state.asStateFlow()
    val state: PreviewState get() = _state.value

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Set position
     * */
    fun setPosition(position: Int) {
        _position.value = position
    }

    /**
     * Set data
     * */
    fun setData(screenshot: ScreenShotModel) {
        _data.value = screenshot
    }

    /**
     * Set state
     * */
    fun setState(state: PreviewState) {
        _state.value = state
    }
}
