package bbct.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bbct.android.data.BaseballCardDatabase
import bbct.android.ui.navigation.BaseballCardDetailsDestination
import bbct.android.ui.navigation.BaseballCardFilterDestination
import bbct.android.ui.navigation.BaseballCardListDestination
import bbct.android.ui.theme.AppTheme

@Composable
fun App(db: BaseballCardDatabase) {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = BaseballCardListDestination.route,
        ) {
            composable(route = BaseballCardListDestination.route) {
                BaseballCardListDestination.screen(navController, db)
            }
            composable(route = BaseballCardDetailsDestination.route) {
                BaseballCardDetailsDestination.screen(navController, db)
            }
            composable(route = BaseballCardFilterDestination.route) {
                BaseballCardFilterDestination.screen(navController, db)
            }
        }
    }
}
