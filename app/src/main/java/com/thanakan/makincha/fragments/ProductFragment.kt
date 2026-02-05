package com.thanakan.makincha.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.thanakan.makincha.R
import com.thanakan.makincha.adapters.ProductAdapter
import com.thanakan.makincha.api.ApiClient
import com.thanakan.makincha.databinding.FragmentProductBinding
import com.thanakan.makincha.models.Product
import com.thanakan.makincha.ui.AddProductBottomSheet
import kotlinx.coroutines.launch

class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private val categories = listOf("เครื่องดื่ม", "โปรโมชั่น")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductBinding.bind(view)

        setupRecyclerView()
        setupTabs()

        // ✅ เล่น Animation ส่วนหัวตอนเข้าหน้าจอครั้งแรก
        startEntranceAnimation()

        loadProducts(categories.first())
    }

    private fun startEntranceAnimation() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fall_down)
        binding.appBarLayout.startAnimation(animation)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        binding.productRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.productRecyclerView.adapter = productAdapter

        productAdapter.onProductAddClick = { product ->
            val bottomSheet = AddProductBottomSheet(product)
            bottomSheet.show(childFragmentManager, "AddProductBottomSheet")
        }
    }

    private fun setupTabs() {
        binding.productTabs.removeAllTabs()
        categories.forEach {
            binding.productTabs.addTab(binding.productTabs.newTab().setText(it))
        }

        binding.productTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                loadProducts(tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadProducts(category: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = ApiClient.api.getProducts(category)

                if (response.isSuccessful && response.body()?.status == "success") {
                    val products = response.body()?.data ?: emptyList()

                    if (products.isEmpty()) {
                        showEmptyState()
                    } else {
                        productAdapter.updateData(products)
                        showProductList()
                        // ✅ สั่งให้ RecyclerView เล่น Animation รายการสินค้าไหลลงมา
                        binding.productRecyclerView.scheduleLayoutAnimation()
                    }
                } else {
                    showErrorState("โหลดข้อมูลล้มเหลว")
                }
            } catch (e: Exception) {
                showErrorState("เชื่อมต่อไม่ได้: ${e.localizedMessage}")
            }
        }
    }

    private fun showEmptyState() {
        binding.productRecyclerView.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE
        binding.emptyStateText.text = "ไม่มีสินค้าในหมวดนี้"

        // ใส่ Animation ให้ตัวหนังสือค่อยๆ มา
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_fall_down)
        binding.emptyStateContainer.startAnimation(animation)
    }

    private fun showProductList() {
        binding.emptyStateContainer.visibility = View.GONE
        binding.productRecyclerView.visibility = View.VISIBLE
    }

    private fun showErrorState(message: String) {
        binding.productRecyclerView.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE
        binding.emptyStateText.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}