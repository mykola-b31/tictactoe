package ua.cn.stu.tictactoe.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ua.cn.stu.tictactoe.AppTheme
import ua.cn.stu.tictactoe.R
import ua.cn.stu.tictactoe.ui.login.LoginFragment

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        id = R.id.splash_compose_view
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        setContent {
            AppTheme {
                SplashScreen(
                    onFinished = {
                        parentFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.fragment_container, LoginFragment())
                        }
                    }
                )
            }
        }
    }
}