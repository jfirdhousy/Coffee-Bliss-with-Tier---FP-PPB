package com.example.coffeebliss.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeebliss.ui.components.BottomTab
import com.example.coffeebliss.ui.screens.AddMemberScreen
import com.example.coffeebliss.ui.screens.AddTransactionScreen
import com.example.coffeebliss.ui.screens.HomeScreen
import com.example.coffeebliss.ui.screens.MemberCardScreen
import com.example.coffeebliss.ui.screens.MembersScreen
import com.example.coffeebliss.ui.screens.ProfileScreen
import com.example.coffeebliss.ui.screens.RewardScreen
import com.example.coffeebliss.ui.screens.SplashScreen
import com.example.coffeebliss.ui.screens.TransactionsScreen
import com.example.coffeebliss.ui.viewmodel.CoffeeBlissViewModel

/** Every screen has a unique route name. Using constants avoids typos. */
object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val CARD = "card"
    const val TRANSACTIONS = "transactions"
    const val ADD_TRANSACTION = "add_transaction"
    const val REWARDS = "rewards"
    const val PROFILE = "profile"
    const val MEMBERS = "members"
    const val ADD_MEMBER = "add_member"
}

/**
 * The whole app's navigation graph. It creates ONE shared ViewModel and connects every
 * screen, passing simple lambdas so the screens themselves don't need to know about
 * navigation details.
 */
@Composable
fun CoffeeBlissApp() {
    val navController = rememberNavController()
    // One ViewModel shared by all screens, so the active member is remembered everywhere.
    val viewModel: CoffeeBlissViewModel = viewModel(factory = CoffeeBlissViewModel.Factory)

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Routes.HOME) {
                        // Remove the splash from the back stack so Back doesn't return to it.
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onSelectTab = navController::navigateToTab,
                onOpenCard = { navController.navigate(Routes.CARD) },
                onOpenTransactions = { navController.navigateToTab(BottomTab.TRANSACTIONS) },
                onOpenRewards = { navController.navigateToTab(BottomTab.REWARDS) },
                onOpenProfile = { navController.navigateToTab(BottomTab.PROFILE) },
                onAddMember = { navController.navigate(Routes.ADD_MEMBER) }
            )
        }

        composable(Routes.CARD) {
            MemberCardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TRANSACTIONS) {
            TransactionsScreen(
                viewModel = viewModel,
                onSelectTab = navController::navigateToTab,
                onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) }
            )
        }

        composable(Routes.ADD_TRANSACTION) {
            AddTransactionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.REWARDS) {
            RewardScreen(
                viewModel = viewModel,
                onSelectTab = navController::navigateToTab
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onSelectTab = navController::navigateToTab,
                onSwitchMember = { navController.navigate(Routes.MEMBERS) }
            )
        }

        composable(Routes.MEMBERS) {
            MembersScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onAddMember = { navController.navigate(Routes.ADD_MEMBER) },
                onMemberSelected = { navController.navigateToTab(BottomTab.HOME) }
            )
        }

        composable(Routes.ADD_MEMBER) {
            AddMemberScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.navigateToTab(BottomTab.HOME) }
            )
        }
    }
}

/**
 * Moves to one of the four bottom-bar tabs. Home is treated as the single "root" tab, so
 * switching tabs keeps the back stack short and tidy instead of piling screens up.
 */
private fun NavHostController.navigateToTab(tab: BottomTab) {
    val route = when (tab) {
        BottomTab.HOME -> Routes.HOME
        BottomTab.TRANSACTIONS -> Routes.TRANSACTIONS
        BottomTab.REWARDS -> Routes.REWARDS
        BottomTab.PROFILE -> Routes.PROFILE
    }
    navigate(route) {
        // Keep Home as the single root of the tabs and never stack a duplicate of a
        // screen. (We deliberately avoid saveState/restoreState here — for an app this
        // small it adds no benefit and is a common source of navigation crashes.)
        popUpTo(Routes.HOME)
        launchSingleTop = true
    }
}
