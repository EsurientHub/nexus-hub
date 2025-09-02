import androidx.compose.ui.window.ComposeUIViewController
import com.esurienthub.nexus.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
