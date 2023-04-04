package com.ashish.qrscanner

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ashish.qrscanner.databinding.ActivityMainBinding
import com.ashish.qrscanner.views.NewsFragment
import com.ashish.qrscanner.views.ScannerFragment
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity: ", "onCreate Called")

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        layout = binding.root
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity: ", "onResume Called "+supportFragmentManager.backStackEntryCount)
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity: ", "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity: ", "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity: ", "onDestroy Called")
    }
    private fun initView() {
        binding.scanBtn.setOnClickListener {
            onClickRequestPermission(layout)
        }
        binding.newsBtn.setOnClickListener {
            goToNewsFragment()
        }
    }
    private fun goToScannerFragment(){
        val yourFragment = ScannerFragment()
        replaceFragment(yourFragment,"SCAN_FRAGMENT")
    }
    private fun goToNewsFragment(){
        val newsFragment = NewsFragment()
        replaceFragment(newsFragment,"NEWS_FRAGMENT")
    }
    private fun replaceFragment(fragmentName: Fragment, tagName:String) {
        binding.mainView.visibility = View.GONE
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragmentName,tagName).addToBackStack(null).commit()
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("MainActivity: ", "Permission Granted")
                goToScannerFragment()
            } else {
                Log.i("MainActivity: ", "Permission Denied")
                showSnackbar(
                    layout,
                    getString(R.string.permission_needed),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", packageName, null)
                    })
                }
            }
        }

    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                goToScannerFragment()
                showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> {
                showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(layout)
            }.show()
        } else {
            snackbar.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mode -> {
                item.isChecked = !item.isChecked
                Log.d("MainActivity_: ", "item Called "+item.isChecked)
                setUIMode(item,item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setUIMode(item: MenuItem, isChecked: Boolean) {
        Log.d("MainActivity: ", "setUIMode Called "+isChecked)
        if (isChecked) {
            item.setIcon(R.drawable.night)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            item.setIcon(R.drawable.day)

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.title   = getString(R.string.app_name)
        if (supportFragmentManager.backStackEntryCount==0) {
            binding.mainView.visibility = View.VISIBLE
        }
        Log.d("MainActivity: ", "onBackPressed Called")
    }
}