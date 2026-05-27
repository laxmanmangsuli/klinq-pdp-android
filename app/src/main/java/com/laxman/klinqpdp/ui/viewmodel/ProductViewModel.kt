package com.laxman.klinqpdp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.laxman.klinqpdp.data.model.ColorVariant
import com.laxman.klinqpdp.data.model.ProductData
import com.laxman.klinqpdp.data.repository.ProductRepository
import com.laxman.klinqpdp.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productState =
        MutableStateFlow<UiState<ProductData>>(UiState.Loading)
    val productState: StateFlow<UiState<ProductData>> = _productState

    private val _selectedImages =
        MutableStateFlow<List<String>>(emptyList())
    val selectedImages: StateFlow<List<String>> = _selectedImages

    fun fetchProductDetails() {
        viewModelScope.launch {
            _productState.value = UiState.Loading

            try {
                val response = repository.getProductDetails()

                if (response.status == 200) {
                    _productState.value = UiState.Success(response.data)
                    _selectedImages.value = response.data.images
                } else {
                    _productState.value =
                        UiState.Error(response.message)
                }

            } catch (e: IOException) {
                _productState.value =
                    UiState.Error("No internet connection")
            } catch (e: HttpException) {
                _productState.value =
                    UiState.Error("Server error")
            } catch (e: Exception) {
                _productState.value =
                    UiState.Error("Something went wrong")
            }
        }
    }

    fun selectColorVariant(colorVariant: ColorVariant) {
        _selectedImages.value = colorVariant.images
    }
}