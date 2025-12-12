package com.thanakan.makincha.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thanakan.makincha.R
import com.thanakan.makincha.models.CartItem
import com.thanakan.makincha.models.CartManager
import com.thanakan.makincha.models.Product

class AddProductBottomSheet(private val product: Product) : BottomSheetDialogFragment() {

    var onConfirmClick: ((product: Product, sweetness: String, topping: String, amount: Int, note: String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun getTheme(): Int = R.style.BottomSheetTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottomsheet_add_product, container, false)

        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val txtPrice = view.findViewById<TextView>(R.id.txt_price)
        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)
        val radioSweet = view.findViewById<RadioGroup>(R.id.radio_sweet)
        val radioTopping = view.findViewById<RadioGroup>(R.id.radio_topping)
        val inputAmount = view.findViewById<EditText>(R.id.input_amount)
        val inputNote = view.findViewById<EditText>(R.id.input_note)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)

        if (product.imageResId != 0) imgProduct.setImageResource(product.imageResId)
        else imgProduct.setImageResource(R.drawable.coco_makincha)
        txtPrice.text = "฿ %.2f".format(product.price)

        btnClose.setOnClickListener { dismiss() }

        btnConfirm.setOnClickListener {
            val sweetId = radioSweet.checkedRadioButtonId
            val toppingId = radioTopping.checkedRadioButtonId
            val amountText = inputAmount.text.toString().trim()

            if (sweetId == -1) {
                Toast.makeText(requireContext(), "กรุณาเลือกระดับความหวาน", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (toppingId == -1) {
                Toast.makeText(requireContext(), "กรุณาเลือกท็อปปิ้ง", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amountText.isEmpty() || amountText.toIntOrNull() == null || amountText.toInt() <= 0) {
                Toast.makeText(requireContext(), "กรุณากรอกจำนวนแก้วให้ถูกต้อง", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sweet = view.findViewById<RadioButton>(sweetId).text.toString()
            val toppingButton = view.findViewById<RadioButton>(toppingId)
            val topping = toppingButton.text.toString()
            val amount = amountText.toInt()
            val note = inputNote.text.toString()

            val toppingPrice = Regex("""\((\d+)\s*บาท\)""")
                .find(topping)?.groups?.get(1)?.value?.toDouble() ?: 0.0

            val totalPrice = product.price + toppingPrice

            val cartItem = CartItem(
                productName = product.name,
                price = totalPrice,
                sweetness = sweet,
                topping = topping,
                toppingPrice = toppingPrice,
                amount = amount,
                note = note,
                imageResId = product.imageResId
            )

            CartManager.addToCart(cartItem, replaceAmount = true)

            onConfirmClick?.invoke(product, sweet, topping, amount, note)
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
                sheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.75).toInt()
            }
        }
    }
}
