package hiretimsf.com.app.utils.state

sealed class ToastState

/** Saved */
object ToastSaved : ToastState()

/** Unsaved */
object ToastUnsaved : ToastState()

/** Show */
object ToastShow : ToastState()

/** Showed */
object ToastEmpty : ToastState()