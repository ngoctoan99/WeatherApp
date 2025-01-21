package com.example.weatherapp.core

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R

class DialogChangeLocation constructor( private var actionEditLocationInterface  :ActionEditLocationInterface): DialogFragment() {

    private lateinit var btnClose: Button
    private lateinit var btnEdit: Button
    private lateinit var edtLocation: EditText
    private lateinit var rootViewDialogActionChangeLocation: RelativeLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = layoutInflater.inflate(R.layout.dialog_edit_location, null)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        edtLocation = root.findViewById(R.id.edtLocation)
        btnClose = root.findViewById(R.id.btnClose)
        btnEdit = root.findViewById(R.id.btnEdit)
        rootViewDialogActionChangeLocation = root.findViewById(R.id.rootViewDialogActionChangeLocation)
        btnEdit.setOnClickListener {
            actionEditLocationInterface.click(edtLocation.text.trim().toString())
            dismiss()
        }
        btnClose.setOnClickListener{
            dismiss()
        }
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return dialog
    }


    interface ActionEditLocationInterface {
        fun click(location: String)
    }
}