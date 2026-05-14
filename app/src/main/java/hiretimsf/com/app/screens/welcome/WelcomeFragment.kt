package hiretimsf.com.app.screens.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

/**
 * A Fragment boundary that hosts the Compose onboarding screen.
 */
@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private val viewModel by lazy { ViewModelProvider(this)[WelcomeViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val currentItem = viewModel.currentItem
            if (currentItem > 0) {
                viewModel.setScrollToItem(currentItem - 1)
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                WelcomeComposeScreen(
                    viewModel = viewModel,
                    onFinished = {
                        findNavController().navigate(WelcomeFragmentDirections.actionToProfileScreen())
                    },
                )
            }
        }
    }
}
