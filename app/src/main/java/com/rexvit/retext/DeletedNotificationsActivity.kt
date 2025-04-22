package com.rexvit.retext

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rexvit.retext.databinding.ActivityDeletedNotificationsBinding

class DeletedNotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeletedNotificationsBinding
    private lateinit var adapter: DeletedNotificationsAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletedNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemInsets = insets.systemGestureInsets
            val topInset = systemInsets.top // Top insets like status bar or notch
            val bottomInset = systemInsets.bottom // Bottom insets like navigation bar

            // Adjust padding or margin as per insets
            binding.root.setPadding(0, topInset, 0, bottomInset)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val notifications = dbHelper.getDeletedNotifications()
        adapter = DeletedNotificationsAdapter(
            notifications.toMutableList(),
            dbHelper
        ) { deletedItem, position ->
            Snackbar.make(binding.deletedNotificationsRecyclerView, "Notification deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") { adapter.restoreItem(deletedItem, position) }
                .show()
        }

        binding.deletedNotificationsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deletedNotificationsRecyclerView.adapter = adapter

        ItemTouchHelper(adapter.swipeCallback).attachToRecyclerView(binding.deletedNotificationsRecyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_deleted_notifications, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                showClearAllConfirmation()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showClearAllConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear All Notifications")
            .setMessage("Are you sure you want to delete all saved notifications?")
            .setPositiveButton("Delete All") { _, _ ->
                dbHelper.deleteAllNotifications()
                adapter.updateData(emptyList())
                Snackbar.make(binding.root, "All notifications cleared", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}