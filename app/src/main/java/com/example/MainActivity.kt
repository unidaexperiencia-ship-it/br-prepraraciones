package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.OrderCheckoutBottomSheet
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.RbBottomNav
import com.example.ui.components.RbTopBar
import com.example.ui.screens.BusinessScreen
import com.example.ui.screens.OpportunityScreen
import com.example.ui.screens.OrderHistorySheet
import com.example.ui.screens.PromotionsScreen
import com.example.ui.screens.RecommendationsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RbViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RbApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RbApp(
    viewModel: RbViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val checkoutSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val historySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val cartTotalItems = uiState.cart.values.sum()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RbTopBar(
                cartCount = cartTotalItems,
                onCartClick = { viewModel.openCheckoutSheet() },
                onHistoryClick = { viewModel.toggleOrderHistory(true) }
            )
        },
        bottomBar = {
            RbBottomNav(
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = uiState.selectedTab,
                label = "ScreenTransition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> PromotionsScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    1 -> RecommendationsScreen(
                        viewModel = viewModel
                    )
                    2 -> OpportunityScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                    3 -> BusinessScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }

        // Checkout Bottom Sheet
        if (uiState.isCheckoutSheetOpen) {
            OrderCheckoutBottomSheet(
                viewModel = viewModel,
                uiState = uiState,
                sheetState = checkoutSheetState,
                onDismiss = { viewModel.closeCheckoutSheet() }
            )
        }

        // Order History Sheet
        if (uiState.isOrderHistoryOpen) {
            OrderHistorySheet(
                viewModel = viewModel,
                sheetState = historySheetState,
                onDismiss = { viewModel.toggleOrderHistory(false) }
            )
        }

        // Order Placed Success Dialog
        if (uiState.showOrderSuccessDialog && uiState.lastPlacedOrder != null) {
            OrderSuccessDialog(
                order = uiState.lastPlacedOrder!!,
                viewModel = viewModel,
                onDismiss = { viewModel.dismissOrderSuccessDialog() }
            )
        }
    }
}
