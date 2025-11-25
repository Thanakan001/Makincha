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
import com.thanakan.makincha.models.Product

class AddProductBottomSheet(private val product: Product) : BottomSheetDialogFragment() {

    // Callback ส่งข้อมูลกลับไป Fragment
    var onConfirmClick: ((sweetness: String, topping: String, amount: Int, note: String) -> Unit)? = null

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

        // แสดงรูปสินค้าและราคา
        if (product.imageResId != 0) imgProduct.setImageResource(product.imageResId)
        else imgProduct.setImageResource(R.drawable.coco_makincha)
        txtPrice.text = "฿ %.2f".format(product.price)

        // ปรับ spacing ให้ข้อความกระจาย
        fun TextView.applyTextStyle() {
            this.letterSpacing = 0.02f
            this.setLineSpacing(6f, 1f)
        }

        txtPrice.applyTextStyle()
        (0 until radioSweet.childCount).forEach {
            (radioSweet.getChildAt(it) as? RadioButton)?.applyTextStyle()
        }
        (0 until radioTopping.childCount).forEach {
            (radioTopping.getChildAt(it) as? RadioButton)?.applyTextStyle()
        }

        inputAmount.applyTextStyle()
        inputNote.applyTextStyle()

        // ปุ่มปิด popup
        btnClose.setOnClickListener { dismiss() }

        // ปุ่มยืนยัน
        btnConfirm.setOnClickListener {
            val sweetId = radioSweet.checkedRadioButtonId
            val toppingId = radioTopping.checkedRadioButtonId

            val sweet = if (sweetId != -1) view.findViewById<RadioButton>(sweetId).text.toString() else ""
            val topping = if (toppingId != -1) view.findViewById<RadioButton>(toppingId).text.toString() else ""
            val amount = inputAmount.text.toString().toIntOrNull() ?: 1
            val note = inputNote.text.toString()

            onConfirmClick?.invoke(sweet, topping, amount, note)
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
                behavior.isDraggable = false // popup คงที่ ไม่เลื่อนทั้ง popup
                sheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.75).toInt() // สูง 75% ของหน้าจอ
            }
        }
    }
}
