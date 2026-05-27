package com.laxman.klinqpdp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.laxman.klinqpdp.data.model.ProductData
import com.laxman.klinqpdp.data.remote.RetrofitClient
import com.laxman.klinqpdp.data.repository.ProductRepository
import com.laxman.klinqpdp.databinding.ActivityProductDetailBinding
import com.laxman.klinqpdp.ui.adapter.ColorVariantAdapter
import com.laxman.klinqpdp.ui.adapter.ProductImageAdapter
import com.laxman.klinqpdp.ui.viewmodel.ProductViewModel
import com.laxman.klinqpdp.ui.viewmodel.ProductViewModelFactory
import com.laxman.klinqpdp.utils.UiState
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    private lateinit var imageAdapter: ProductImageAdapter
    private lateinit var colorAdapter: ColorVariantAdapter

    private var quantity = 1
    private var productUrl = ""
    private var maxQuantity = 1
    private var isDescriptionVisible = true

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(
            ProductRepository(
                RetrofitClient.apiService
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        setupClickListeners()
        observeViewModel()

        viewModel.fetchProductDetails()

    }

    private fun setupAdapters() {
        imageAdapter = ProductImageAdapter()
        binding.viewPagerImages.offscreenPageLimit = 3
        binding.viewPagerImages.adapter = imageAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerImages)

        colorAdapter = ColorVariantAdapter { colorVariant ->
            binding.tvSelectedColor.text = "Color: ${colorVariant.value}"
            viewModel.selectColorVariant(colorVariant)
        }

        binding.rvColorVariants.apply {
            layoutManager = LinearLayoutManager(
                this@ProductDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = colorAdapter
        }
    }

    private fun setupClickListeners() {

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }

        binding.btnPlus.setOnClickListener {
            if (quantity < maxQuantity) {
                quantity++
                binding.tvQuantity.text = quantity.toString()
            }
        }

        binding.btnAddToBag.setOnClickListener {
            Toast.makeText(
                this,
                "Added $quantity item(s) to bag",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnShare.setOnClickListener {
            if (productUrl.isNotEmpty()) {
                shareProduct()
            }
        }
        binding.layoutProductInfoHeader.setOnClickListener {
            isDescriptionVisible = !isDescriptionVisible

            binding.tvDescription.isVisible = isDescriptionVisible

            binding.ivToggleDescription.animate()
                .rotation(if (isDescriptionVisible) 0f else 180f)
                .setDuration(200)
                .start()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.productState.collect { state ->
                        when (state) {
                            is UiState.Loading -> {
                                binding.progressBar.isVisible = true
                            }

                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                bindProductData(state.data)
                            }

                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                Toast.makeText(
                                    this@ProductDetailActivity,
                                    state.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }

                launch {
                    viewModel.selectedImages.collect { images ->
                        if (images.isNotEmpty()) {
                            imageAdapter.submitImageList(images)
                        }
                    }
                }
            }
        }
    }

    private fun bindProductData(product: ProductData) {
        maxQuantity = product.remainingQty.coerceAtLeast(1)
        binding.tvPayment.text = HtmlCompat.fromHtml(
            "or 4 interest-free payments<br>0.88 KWD <u>Learn More</u>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.tvToolbarTitle.text = product.name
        binding.tvBrand.text = product.brandName.uppercase()
        binding.tvProductName.text = product.name
        binding.tvPrice.text =
            String.format("%.2f KWD", product.price.toDouble())
        binding.tvSku.text = "SKU: ${product.sku}"

        productUrl = product.webUrl
        maxQuantity = product.remainingQty

        binding.tvDescription.text =
            HtmlCompat.fromHtml(
                product.description ?: "",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        product.configurableOption.firstOrNull()?.let {
            colorAdapter.submitColorList(it.attributes)
        }
    }

    private fun shareProduct() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check this product: $productUrl"
            )
        }
        startActivity(Intent.createChooser(intent, "Share Product"))
    }
}