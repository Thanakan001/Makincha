package com.thanakan.makincha.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.thanakan.makincha.R
import com.thanakan.makincha.databinding.FragmentProductBinding
import com.thanakan.makincha.adapters.ProductAdapter
import com.thanakan.makincha.models.CartManager
import com.thanakan.makincha.models.Product
import com.thanakan.makincha.ui.AddProductBottomSheet
import com.thanakan.makincha.ui.CartFragment
import android.view.View.GONE
import android.view.View.VISIBLE

class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val categories = listOf("เครื่องดื่ม", "โปรโมชั่น")
    private lateinit var productAdapter: ProductAdapter
    private val allProducts = getDummyProductData()

    private var cartFragment: CartFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductBinding.bind(view)

        setupToolbar()
        setupCategoriesTabs()
        setupRecyclerView()
        setupCartButton()
        updateCartFab()
        loadCategoryProducts(categories.first())
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        binding.searchBar.setOnClickListener { /* TODO: search */ }
    }

    private fun setupCategoriesTabs() {
        categories.forEach { categoryName ->
            binding.productTabs.addTab(binding.productTabs.newTab().setText(categoryName))
        }

        binding.productTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                loadCategoryProducts(tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        binding.productRecyclerView.adapter = productAdapter
        binding.productRecyclerView.layoutManager = LinearLayoutManager(context)

        productAdapter.onProductAddClick = { product ->
            val bottomSheet = AddProductBottomSheet(product)
            bottomSheet.onConfirmClick = { p, sweet, topping, amount, note ->
                updateCartFab()
            }
            bottomSheet.show(childFragmentManager, "AddProductBottomSheet")
        }
    }

    private fun setupCartButton() {
        binding.fabCart.setOnClickListener {
            if (cartFragment == null) cartFragment = CartFragment()
            cartFragment?.show(childFragmentManager, "CartFragment")
        }
    }

    private fun updateCartFab() {
        val count = CartManager.getTotalItemCount()
        binding.fabCart.text = "ตะกร้า ($count รายการ)"
    }

    private fun loadCategoryProducts(category: String) {
        if (category == "เครื่องดื่ม") {
            val drinkProducts = allProducts.filter { it.category == "เครื่องดื่ม" }
            productAdapter.updateData(drinkProducts)
            binding.emptyStateContainer.visibility = GONE
            binding.productRecyclerView.visibility = VISIBLE
        } else {
            productAdapter.updateData(emptyList())
            binding.productRecyclerView.visibility = GONE
            binding.emptyStateContainer.visibility = VISIBLE
            binding.emptyStateText.text = "ไม่มีโปรโมชั่นในตอนนี้"
        }
    }

    private fun getDummyProductData(): List<Product> {
        return listOf(
            Product(1,"โกโก้",35.0,"เข้มข้น หวานฉ่ำ","","เครื่องดื่ม",R.drawable.coco_makincha),
            Product(2,"นมชมพู",30.0,"หอมนม สดชื่น หวานฉ่ำ","","เครื่องดื่ม",R.drawable.nomchompoo),
            Product(3,"นมเผือก",30.0,"หวานมัน สนชื่น  สดใส","","เครื่องดื่ม",R.drawable.nompurk),
            Product(4,"เอสเปรสโซ่",30.0,"ปลุกความเป็นชายในตัวคุณ","","เครื่องดื่ม",R.drawable.esspeccso),
            Product(5,"แอปเปิ้ลโซดา",30.0,"ซ่าได้ทุกที่ ซ่าถึงใจ","","เครื่องดื่ม",R.drawable.apple_soda),
            Product(6,"บลูฮาวายโซดา",30.0,"ซ่าถึงใจเด็กไทยชอบทุกคน","","เครื่องดื่ม",R.drawable.bluehawai_soda),
            Product(7, "ลิ้นจี่โซดา", 30.0, "ซ่าจ้าดดดดด", "", "เครื่องดื่ม", R.drawable.linchee_soda),
            Product(8,"สตอเบอร์รี่โซดา",30.0,"ซ่าถึงใจ","","เครื่องดื่ม", R.drawable.stroberry_soda),
            Product(9, "ชาเขียว",30.0,"หวานมันอร่อย","","เครื่องดื่ม",R.drawable.greentenn),
            Product(10, "นมสด",30.0,"หวานมันอร่อย","","เครื่องดื่ม",R.drawable.nomsod),
            Product(11, "ชานมใต้หวัน",30.0,"หวานมันอร่อย","","เครื่องดื่ม",R.drawable.esspeccso),
            Product(12, "นมสดคาราเมล",30.0,"หวานมันอร่อย","","เครื่องดื่ม",R.drawable.nomsod)





        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
