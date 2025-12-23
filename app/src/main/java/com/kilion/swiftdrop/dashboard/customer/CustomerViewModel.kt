package com.kilion.swiftdrop.dashboard.customer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.kilion.swiftdrop.data.local.UserPreferences
import com.kilion.swiftdrop.data.model.Item
import com.kilion.swiftdrop.data.model.Review
import com.kilion.swiftdrop.data.model.UserProfile
import com.kilion.swiftdrop.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<CustomerUiState>(CustomerUiState.Idle)
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()

    private val _cart = MutableStateFlow<List<Item>>(emptyList())
    val cart: StateFlow<List<Item>> = _cart.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allItems = MutableStateFlow<List<Item>>(emptyList())

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init {
        loadAllItems()
        viewModelScope.launch {
            _searchQuery.debounce(300).collect { query ->
                val filteredList = _allItems.value.filter {
                    it.name?.contains(query, ignoreCase = true) == true ||
                            it.description?.contains(query, ignoreCase = true) == true
                }
                _uiState.value = CustomerUiState.Success(filteredList)
            }
        }
    }

    fun loadAllItems() {
        viewModelScope.launch {
            _uiState.value = CustomerUiState.Loading
            try {
                val items = customerRepository.getAllItems()
                _allItems.value = items
                _uiState.value = CustomerUiState.Success(items)
            } catch (e: Exception) {
                _uiState.value = CustomerUiState.Error("Failed to load items: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(item: Item) {
        _cart.value = _cart.value + item
    }

    fun placeOrder(dropoffAddress: String) {
        viewModelScope.launch {
            _uiState.value = CustomerUiState.Loading
            try {
                val order = customerRepository.placeOrder(_cart.value, dropoffAddress)
                _uiState.value = CustomerUiState.OrderPlaced(order.id)
                _cart.value = emptyList() // Clear the cart
            } catch (e: Exception) {
                _uiState.value = CustomerUiState.Error("Failed to place order: ${e.message}")
            }
        }
    }

    fun loadMyOrders() {
        viewModelScope.launch {
            _uiState.value = CustomerUiState.Loading
            try {
                val orders = customerRepository.getMyOrders()
                _uiState.value = CustomerUiState.OrdersLoaded(orders)
            } catch (e: Exception) {
                _uiState.value = CustomerUiState.Error("Failed to load orders: ${e.message}")
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            userPreferences.authToken.firstOrNull()?.let {
                val jwt = JWT(it)
                val name = jwt.getClaim("name").asString() ?: "N/A"
                val phone = jwt.getClaim("phone").asString() ?: "N/A"
                val role = jwt.getClaim("role").asString() ?: "N/A"
                _userProfile.value = UserProfile(name, phone, role)
            }
        }
    }

    fun submitReview(orderId: String, itemId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            _uiState.value = CustomerUiState.Loading
            try {
                customerRepository.submitReview(orderId, itemId, rating, comment)
                _uiState.value = CustomerUiState.ReviewSubmitted
            } catch (e: Exception) {
                _uiState.value = CustomerUiState.Error("Failed to submit review: ${e.message}")
            }
        }
    }

    fun loadReviewsForItem(itemId: String) {
        viewModelScope.launch {
            try {
                val reviews = customerRepository.getReviewsForItem(itemId)
                _reviews.value = reviews
            } catch (e: Exception) {
                // Handle error appropriately
                Log.e("CustomerViewModel", "Failed to load reviews for item $itemId", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearAuthToken()
        }
    }
}