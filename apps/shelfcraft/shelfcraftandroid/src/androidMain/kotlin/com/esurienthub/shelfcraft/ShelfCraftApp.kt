package com.esurienthub.shelfcraft

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.esurienthub.shelfcraft.features.InventoryScreen
import com.esurienthub.shelfcraft.features.AddInventoryScreen
import com.esurienthub.shelfcraft.features.DetailInventoryScreen
import com.esurienthub.shelfcraft.navigation.AddInventoryRoute
import com.esurienthub.shelfcraft.navigation.DetailInventoryRoute
import com.esurienthub.shelfcraft.navigation.InventoryRoute
import com.esurienthub.shelfcraft.ui.theme.ShelfCraftTheme

@Composable
fun ShelfCraftApp() {
	ShelfCraftTheme {
		val navController = rememberNavController()

		NavHost(navController = navController, startDestination = InventoryRoute) {
			composable<InventoryRoute> {
				BackHandler {
					navController.navigateUp()
				}
				InventoryScreen(
					navigateToAddInventory = {
						navController.navigate(AddInventoryRoute)
					},
					navigateToDetailInventory = { id ->
						navController.navigate(DetailInventoryRoute(id))
					}
				)
			}

			composable<DetailInventoryRoute> { backStackEntry ->
				val route = backStackEntry.toRoute<DetailInventoryRoute>()

				BackHandler {
					navController.navigate(InventoryRoute)
				}

				DetailInventoryScreen(
					id = route.id,
					navigateToInventory = { navController.navigate(InventoryRoute) }
				)
			}

			composable<AddInventoryRoute> {
				BackHandler {
					navController.navigate(InventoryRoute)
				}

				AddInventoryScreen(
					navigateToInventory = { navController.navigate(InventoryRoute) },
					onCreated = { id ->
						navController.navigate(DetailInventoryRoute(id))
					}
				)
			}
		}
	}
}
