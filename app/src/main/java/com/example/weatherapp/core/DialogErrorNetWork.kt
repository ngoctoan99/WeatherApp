package com.example.weatherapp.core

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R

class DialogErrorNetWork : DialogFragment() {

    private lateinit var llConfirm: LinearLayout
    private lateinit var tvMessage: TextView
    private lateinit var rootViewDialogErrorNetWork: RelativeLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = layoutInflater.inflate(R.layout.dialog_error_network, null)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        tvMessage = root.findViewById(R.id.tvMessage)
        llConfirm = root.findViewById(R.id.llConfirm)
        rootViewDialogErrorNetWork = root.findViewById(R.id.rootViewDialogErrorNetWork)
        rootViewDialogErrorNetWork.setOnClickListener { dismiss() }
        llConfirm.setOnClickListener {
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

}