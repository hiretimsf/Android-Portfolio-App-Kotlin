package hiretimsf.com.app.utils.state

sealed class PreviewState

/** Show progress bar */
object ProgressBar : PreviewState()

/** Hide progress bar */
object PreviewImage : PreviewState()