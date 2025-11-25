package com.thanakan.makincha.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.thanakan.makincha.R
import com.thanakan.makincha.databinding.FragmentProductBinding
import com.thanakan.makincha.adapters.ProductAdapter
import com.thanakan.makincha.models.Product
import com.thanakan.makincha.ui.AddProductBottomSheet
import android.view.View.GONE
import android.view.View.VISIBLE

class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val categories = listOf("เครื่องดื่ม", "โปรโมชั่น")

    private lateinit var productAdapter: ProductAdapter

    private val allProducts = getDummyProductData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductBinding.bind(view)

        setupToolbar()
        setupCategoriesTabs()
        setupRecyclerView()
        setupCartButton()

        loadCategoryProducts(categories.first())
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.searchBar.setOnClickListener {
        }
    }

    private fun setupCategoriesTabs() {
        categories.forEach { categoryName ->
            binding.productTabs.addTab(binding.productTabs.newTab().setText(categoryName))
        }

        binding.productTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = tab?.text.toString()
                loadCategoryProducts(category)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        binding.productRecyclerView.adapter = productAdapter
        binding.productRecyclerView.layoutManager = LinearLayoutManager(context)

        // ★ เปิด popup เมื่อกดปุ่ม +
        productAdapter.onProductAddClick = { product ->
            val bottomSheet = AddProductBottomSheet(product)
            bottomSheet.show(childFragmentManager, "AddProductBottomSheet")
        }
    }

    private fun setupCartButton() {
        binding.fabCart.setOnClickListener {
        }
    }

    private fun loadCategoryProducts(category: String) {
        if (category == "เครื่องดื่ม") {
            val drinkProducts = allProducts.filter { it.category == "เครื่องดื่ม" }
            productAdapter.updateData(drinkProducts)

            binding.emptyStateContainer.visibility = GONE
            binding.productRecyclerView.visibility = VISIBLE

        } else if (category == "โปรโมชั่น") {
            productAdapter.updateData(emptyList())

            binding.productRecyclerView.visibility = GONE
            binding.emptyStateContainer.visibility = VISIBLE

            binding.emptyStateText.text = "ไม่มีโปรโมชั่นในตอนนี้"
        }
    }

    private fun getDummyProductData(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "โกโก้",
                price = 35.00,
                description = "เข้มข้น หวานฉ่ำ",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.coco_makincha
            ),
            Product(
                id = 2,
                name = "นมชมพู",
                price = 30.00,
                description = "หอมนม สดชื่น หวานฉ่ำ",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.nomcumpo_makincha
            ),
            Product(
                id = 3,
                name = "นมเผือก",
                price = 30.00,
                description = "หวานมัน สนชื่น  สดใส",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.nompurk_makincha
            ),
            Product(
                id = 4,
                name = "เอสเปรสโซ่",
                price = 30.00,
                description = "ปลุกความเป็นชายในตัวคุณ",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.espesso_makincha
            ),
            Product(
                id = 5,
                name = "แอปเปิ้ลโซดา",
                price = 30.00,
                description = "ซ่าได้ทุกที่ ซ่าถึงใจ",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.applesoda_makincha
            ),
            Product(
                id = 6,
                name = "บลูฮาวายโซดา",
                price = 30.00,
                description = "ซ่าถึงใจเด็กไทยชอบทุกคน",
                imageUrl = "",
                category = "เครื่องดื่ม",
                imageResId = R.drawable.bluesoda_makincha
            ),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
