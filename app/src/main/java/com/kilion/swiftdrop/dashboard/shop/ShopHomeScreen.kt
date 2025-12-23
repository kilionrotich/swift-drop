package com.kilion.swiftdrop.dashboard.shop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kilion.swiftdrop.R
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.ui.theme.MintLace
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHomeScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var itemToDelete by remember { mutableStateOf<Item?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ShopDrawerContent(navController = navController, closeDrawer = { scope.launch { drawerState.close() } })
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Shop Dashboard") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("add_item") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MintLace)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                val isLoading = uiState is ShopUiState.Loading
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))

                AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.fillMaxSize(0.5f)
                    )
                }

                AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    when (val state = uiState) {
                        is ShopUiState.SuccessItems -> {
                            if (state.items.isEmpty()) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_shop_placeholder),
                                        contentDescription = "Shop Placeholder",
                                        modifier = Modifier.fillMaxSize(0.4f),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = """Your shop is empty.\nTap the '+' button to add your first item!""",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            } else {
                                ShopItemsList(
                                    items = state.items,
                                    onEditClick = { navController.navigate("edit_item/${it.id}") },
                                    onDeleteClick = { itemToDelete = it }
                                )
                            }
                        }
                        is ShopUiState.Error -> {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        else -> {}
                    }
                }

                itemToDelete?.let {
                    DeleteConfirmationDialog(
                        item = it,
                        onConfirm = {
                            viewModel.deleteItem(it.id)
                            itemToDelete = null
                        },
                        onDismiss = { itemToDelete = null }
                    )
                }
            }
        }
    }
}

@Composable
fun ShopDrawerContent(navController: NavController, closeDrawer: () -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Welcome, Shop Owner!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            NavigationDrawerItem(
                label = { Text("Incoming Orders") },
                selected = false,
                onClick = { navController.navigate("shop_orders"); closeDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Order History") },
                selected = false,
                onClick = { navController.navigate("shop_order_history"); closeDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Analytics") },
                selected = false,
                onClick = { navController.navigate("shop_analytics"); closeDrawer() }
            )
            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = false,
                onClick = { navController.navigate("shop_profile"); closeDrawer() }
            )
        }
    }
}

@Composable
fun ShopItemsList(items: List<Item>, onEditClick: (Item) -> Unit, onDeleteClick: (Item) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ShopItemCard(item = item, onEditClick = { onEditClick(item) }, onDeleteClick = { onDeleteClick(item) })
        }
    }
}

@Composable
fun ShopItemCard(item: Item, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name ?: "Unnamed Item", style = MaterialTheme.typography.titleLarge)
            if (!item.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Price: $${item.price}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Item")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Item")
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    item: Item,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Item") },
        text = { Text(text = "Are you sure you want to delete '${item.name ?: "Unnamed Item"}'?") },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
