package hiretimsf.com.app.utils.state

sealed class ScreenState

/** Splash Screen */
object SplashScreen: ScreenState()

/** Welcome Screen */
object WelcomeScreen : ScreenState()

/** Main Screen */
object MainScreen : ScreenState()

/** Loader Screen */
object LoaderScreen : ScreenState()

