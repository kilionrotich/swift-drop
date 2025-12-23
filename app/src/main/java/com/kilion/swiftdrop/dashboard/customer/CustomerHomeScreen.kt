package com.kilion.swiftdrop.dashboard.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kilion.swiftdrop.R
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.ui.components.SwiftDropTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: CustomerViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SwiftDrop") },
                actions = {
                    IconButton(onClick = { navController.navigate("order_history") }) {
                        Icon(Icons.Default.History, contentDescription = "Order History")
                    }
                    IconButton(onClick = { navController.navigate("customer_profile") }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("checkout") }) {
                BadgedBox(badge = { Badge { Text("${cart.size}") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping Cart")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            SwiftDropTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = "Search Items"
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is CustomerUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is CustomerUiState.Success -> {
                        if (state.items.isEmpty()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cart_placeholder),
                                    contentDescription = "Cart Placeholder",
                                    modifier = Modifier.fillMaxSize(0.4f),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No items available at the moment.",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        } else {
                            ItemsList(
                                items = state.items,
                                onAddToCartClick = { viewModel.addToCart(it) },
                                onItemClick = { navController.navigate("item_detail/${it.id}") }
                            )
                        }
                    }
                    is CustomerUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    is CustomerUiState.Idle -> {
                        // Show a loading indicator by default
                        CircularProgressIndicator()
                    }

                    is CustomerUiState.OrderPlaced -> TODO()
                    is CustomerUiState.OrdersLoaded -> TODO()
                    is CustomerUiState.ReviewSubmitted -> TODO()
                    is CustomerUiState.ReviewsLoaded -> TODO()
                }
            }
        }
    }
}

@Composable
fun ItemsList(items: List<Item>, onAddToCartClick: (Item) -> Unit, onItemClick: (Item) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemCard(item = item, onAddToCartClick = { onAddToCartClick(item) }, onItemClick = { onItemClick(item) })
        }
    }
}

@Composable
fun ItemCard(item: Item, onAddToCartClick: () -> Unit, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onItemClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name ?: "Unnamed Item", style = MaterialTheme.typography.titleLarge)
            if (!item.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price: $${item.price}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onAddToCartClick) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to Cart")
                }
            }
        }
    }
}
