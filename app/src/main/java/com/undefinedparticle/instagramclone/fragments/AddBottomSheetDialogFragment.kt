package com.undefinedparticle.instagramclone.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.undefinedparticle.instagramclone.R
import com.undefinedparticle.instagramclone.activities.AddPostActivity
import com.undefinedparticle.instagramclone.activities.AddReelsActivity
import com.undefinedparticle.instagramclone.databinding.FragmentAddBottomSheetDialogBinding


class AddBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBottomSheetDialogBinding
    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(View(context))
        val root = dialog.findViewById<View>(R.id.design_bottom_sheet)
        behavior = BottomSheetBehavior.from(root)
        behavior.setPeekHeight(ViewGroup.LayoutParams.MATCH_PARENT, true)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bottom_sheet_dialog, container, false)
        binding.lifecycleOwner = this

        binding.addPost.setOnClickListener{

            startActivity(Intent(context, AddPostActivity::class.java))

            dialog?.dismiss()

        }

        binding.addReel.setOnClickListener{

            startActivity(Intent(context, AddReelsActivity::class.java))

            dialog?.dismiss()

        }

        return binding.root
    }


}