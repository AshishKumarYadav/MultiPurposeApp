package com.ashish.qrscanner.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.ashish.qrscanner.R
import com.ashish.qrscanner.databinding.ActivityMainBinding
import com.ashish.qrscanner.databinding.FragmentScannerBinding
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.snackbar.Snackbar


class ScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: FragmentScannerBinding
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScannerBinding.inflate(inflater)
        layout = binding.root
        return layout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScannerFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScannerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this.remove();
                requireActivity().onBackPressed();
            }
        })
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                val url = it.text
                showSnackbar(
                    layout,
                    it.text,
                    Snackbar.LENGTH_INDEFINITE,
                    if (Patterns.WEB_URL.matcher(url.toLowerCase()).matches()) getString(R.string.view)
                    else getString(R.string.ok)
                ) {
                    if (Patterns.WEB_URL.matcher(url.toLowerCase()).matches()){
                        val urlIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )
                        startActivity(urlIntent)
                    }
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (Patterns.WEB_URL.matcher(msg.toLowerCase()).matches()){

        }
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(layout)
            }.show()
        } else {
            snackbar.show()
        }
    }


}