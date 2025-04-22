package com.rexvit.retext.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexvit.retext.R
import com.rexvit.retext.data.database.DatabaseHelper
import com.rexvit.retext.databinding.ActivityMainBinding
import com.rexvit.retext.ui.adapter.AppSelectionAdapter
import com.rexvit.retext.utility.AppUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appAdapter: AppSelectionAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        if (!isNotificationServiceEnabled()) {
            showNotificationAccessDialog()
        }

        setupRecyclerView()
        setupButtons()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemInsets = insets.systemGestureInsets
            val topInset = systemInsets.top // Top insets like status bar or notch
            val bottomInset = systemInsets.bottom // Bottom insets like navigation bar

            // Adjust padding or margin as per insets
            binding.root.setPadding(0, topInset, 0, bottomInset)
            insets
        }

    }

    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        Thread {
            val apps = AppUtils.getInstalledApps(this)
            runOnUiThread {
                appAdapter = AppSelectionAdapter(apps.toMutableList()) { deletedItem, position ->
                    Snackbar.make(binding.appListRecyclerView, "${deletedItem.name} removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO") { appAdapter.restoreItem(deletedItem, position) }
                        .show()
                }
                appAdapter.updateSelectedApps(dbHelper.getAllSelectedApps())

                binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.appListRecyclerView.adapter = appAdapter

                ItemTouchHelper(appAdapter.swipeCallback).attachToRecyclerView(binding.appListRecyclerView)
                binding.progressBar.visibility = View.GONE
            }
        }.start()
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener {
            dbHelper.saveSelectedApps(appAdapter.getSelectedApps())
            Snackbar.make(binding.root, "App selection saved", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnViewDeleted.setOnClickListener {
            startActivity(Intent(this, DeletedNotificationsActivity::class.java))
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val enabledListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        return enabledListeners?.contains(packageName) == true
    }

    private fun showNotificationAccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Access Required")
            .setMessage("ReText needs notification access to monitor deleted notifications.")
            .setPositiveButton("Grant Access") { _, _ ->
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}