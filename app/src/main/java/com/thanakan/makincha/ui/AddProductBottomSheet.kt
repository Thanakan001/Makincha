package com.thanakan.makincha.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thanakan.makincha.R // ✅ ตรวจสอบว่ามีบรรทัดนี้เพื่อแก้ Unresolved reference 'R'
import com.thanakan.makincha.models.CartItem
import com.thanakan.makincha.models.CartManager
import com.thanakan.makincha.models.Product

class AddProductBottomSheet(
    private val product: Product
) : BottomSheetDialogFragment() {

    var onConfirmClick: ((
        product: Product,
        sweetness: String,
        topping: String,
        amount: Int,
        note: String
    ) -> Unit)? = null

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

        /* ===== โหลดรูปสินค้า ===== */
        val imageName = product.image_url.orEmpty()

        if (imageName.isNotBlank()) {
            Glide.with(this)
                .load("http://10.0.2.2/makincha_api/images/$imageName")
                .placeholder(R.drawable.coco_makincha)
                .error(R.drawable.coco_makincha)
                .into(imgProduct)
        } else {
            imgProduct.setImageResource(R.drawable.coco_makincha)
        }

        txtPrice.text = "฿ %.2f".format(product.price)

        btnClose.setOnClickListener { dismiss() }

        btnConfirm.setOnClickListener {

            val sweetId = radioSweet.checkedRadioButtonId
            val toppingId = radioTopping.checkedRadioButtonId
            val amountText = inputAmount.text.toString().trim()

            if (sweetId == -1) {
                toast("กรุณาเลือกระดับความหวาน")
                return@setOnClickListener
            }

            if (toppingId == -1) {
                toast("กรุณาเลือกท็อปปิ้ง")
                return@setOnClickListener
            }

            val amount = amountText.toIntOrNull()
            if (amount == null || amount <= 0) {
                toast("กรุณากรอกจำนวนแก้วให้ถูกต้อง")
                return@setOnClickListener
            }

            val sweetness =
                view.findViewById<RadioButton>(sweetId).text.toString()
            val toppingText =
                view.findViewById<RadioButton>(toppingId).text.toString()
            val note = inputNote.text.toString()

            val toppingPrice = Regex("""\((\d+)\s*บาท\)""")
                .find(toppingText)
                ?.groups?.get(1)?.value?.toDouble() ?: 0.0

            // ✅ แก้ไข: เปลี่ยน orderId จาก null เป็น 0 (เพราะ CartItem ใหม่รับ Int ที่ไม่ใช่ null)
            val cartItem = CartItem(
                orderId = 0,
                productId = product.id,
                productName = product.name,
                price = product.price,
                sweetness = sweetness,
                topping = toppingText,
                toppingPrice = toppingPrice,
                amount = amount,
                note = note,
                imageUrl = product.image_url ?: "",
                status = "รอดำเนินการ"
            )

            CartManager.addToCart(cartItem, replaceAmount = true)

            onConfirmClick?.invoke(
                product,
                sweetness,
                toppingText,
                amount,
                note
            )

            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)
            ?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            ?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
                sheet.layoutParams.height =
                    (resources.displayMetrics.heightPixels * 0.75).toInt()
            }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}