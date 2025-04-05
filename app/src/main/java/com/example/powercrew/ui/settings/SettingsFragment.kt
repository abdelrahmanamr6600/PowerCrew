package com.example.powercrew.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.powercrew.R
import com.example.powercrew.databinding.AboutUsBinding
import com.example.powercrew.databinding.FragmentSettingsBinding
import com.example.powercrew.databinding.ItemCustomDialogLayoutBinding
import com.example.powercrew.utils.LocaleHelper


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var itemCustomDialogLayoutBinding: ItemCustomDialogLayoutBinding
    private lateinit var aboutUsDialog: AboutUsBinding
    private val binding get() = _binding!!
    private lateinit var alert: AlertDialog
    private var englishRadioButtonState: Boolean = false
    private var arabicRadioButtonState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        itemCustomDialogLayoutBinding = ItemCustomDialogLayoutBinding.inflate(
            layoutInflater, binding.root, false
        )
        aboutUsDialog = AboutUsBinding.inflate(
            layoutInflater, binding.root, false
        )
        binding.cardLanguage.setOnClickListener{
            showDialog()
        }
        binding.cardAboutus.setOnClickListener {
            showAbout()
        }
        setListeners()
        
        return binding.root
    }

    private fun setListeners(){
        itemCustomDialogLayoutBinding.rbEnglish.setOnClickListener {
            if (englishRadioButtonState && !arabicRadioButtonState) {
                alert.dismiss()
            } else {
                changeAppLanguage(requireContext(),"en")
                englishRadioButtonState = true
                arabicRadioButtonState = false

                alert.dismiss()

            }
        }


        itemCustomDialogLayoutBinding.rbArabic.setOnClickListener {
            if (arabicRadioButtonState && !englishRadioButtonState) {
                alert.dismiss()
            } else {
           changeAppLanguage(requireContext(),"ar")
                englishRadioButtonState = false
                arabicRadioButtonState = true

                alert.dismiss()

            }
        }
    }
    private fun changeAppLanguage(context: Context, newLanguage: String) {
        LocaleHelper.setLocale(context, newLanguage)

        restartApp(context)
    }
    private fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        Runtime.getRuntime().exit(0) // إنهاء العملية القديمة
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        if (itemCustomDialogLayoutBinding.root.parent != null) {
            (itemCustomDialogLayoutBinding.root.parent as ViewGroup).removeView(
                itemCustomDialogLayoutBinding.root
            )
        }
        builder.setView(itemCustomDialogLayoutBinding.root)
        alert = builder.create()
        if (alert.window != null) {
            alert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alert.show()

    }

    private fun showAbout() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        if (aboutUsDialog.root.parent != null) {
            (aboutUsDialog.root.parent as ViewGroup).removeView(
                aboutUsDialog.root
            )
        }
        builder.setView(aboutUsDialog.root)
        alert = builder.create()
        if (alert.window != null) {
            alert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alert.show()

    }




}