package com.fanhantech.bottomdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

import com.fanhantech.bottomdialog.utils.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.IllegalStateException

class BottomDialog : DialogFragment() {

        private var isAnimation = false
        private var mRootView: View? = null
        private var layoutId: Int? = null
        private var contentView: View? = null
        private var handleView: ((View) -> Unit)? = null
        private lateinit var manager: FragmentManager

        override fun onStart() {
                super.onStart()
                val window = dialog.window
                val params = window!!.attributes
                params.gravity = Gravity.BOTTOM
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                window.attributes = params
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                window.decorView.setOnTouchListener { _, event ->
//                        if (event.action == MotionEvent.ACTION_DOWN) {
//                                dismiss()
//                        }
//                        true
//                }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mRootView = when {
                        layoutId != null -> inflater.inflate(layoutId!!, container, false)
                        contentView != null -> contentView
                        else -> throw IllegalStateException("Not layoutId or view set")
                }
                AnimationUtils.slideToUp(mRootView!!)
                return mRootView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                handleView?.let {
                        it(view)
                }
        }

        fun show() {
                show(manager, "BottomDialog")
        }

        override fun dismiss() {
                if (isAnimation) {
                        return
                }
                isAnimation = true
                AnimationUtils.slideToDown(mRootView!!) {
                        isAnimation = false
                        super@BottomDialog.dismiss()
                }
        }

        companion object {
                fun create(fragmentManager: FragmentManager, layoutId: Int, handleView: ((View) -> Unit)? = null, cancelable: Boolean = true): BottomDialog {
                        val dialog = BottomDialog()
                        dialog.manager = fragmentManager
                        dialog.layoutId = layoutId
                        dialog.handleView = handleView
                        dialog.isCancelable = cancelable
                        return dialog
                }

                fun create(fragmentManager: FragmentManager, view: View, cancelable: Boolean = true): BottomDialog {
                        val dialog = BottomDialog()
                        dialog.manager = fragmentManager
                        dialog.contentView = view
                        dialog.isCancelable = cancelable
                        return dialog
                }
        }
}
