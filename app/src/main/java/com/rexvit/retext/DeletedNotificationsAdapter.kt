package com.rexvit.retext

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class DeletedNotificationsAdapter(
    private var notifications: MutableList<DeletedNotification>,
    private val dbHelper: DatabaseHelper,
    private val onItemDeleted: (DeletedNotification, Int) -> Unit
) : RecyclerView.Adapter<DeletedNotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: com.google.android.material.card.MaterialCardView = view.findViewById(R.id.cardView)
        val appName: TextView = view.findViewById(R.id.appName)
        val notificationTitle: TextView = view.findViewById(R.id.notificationTitle)
        val notificationText: TextView = view.findViewById(R.id.notificationText)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deleted_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.appName.text = notification.appName
        holder.notificationTitle.text = notification.title
        holder.notificationText.text = notification.text

        val dateFormat = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
        holder.timestamp.text = dateFormat.format(java.util.Date(notification.timestamp))
    }

    override fun getItemCount() = notifications.size

    fun deleteItem(position: Int) {
        val deletedItem = notifications[position]
        dbHelper.deleteNotification(deletedItem)
        notifications.removeAt(position)
        notifyItemRemoved(position)
        onItemDeleted(deletedItem, position)
    }

    fun restoreItem(item: DeletedNotification, position: Int) {
        dbHelper.saveDeletedNotification(item)
        notifications.add(position, item)
        notifyItemInserted(position)
    }

    fun updateData(newNotifications: List<DeletedNotification>) {
        notifications.clear()
        notifications.addAll(newNotifications)
        notifyDataSetChanged()
    }

    val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            deleteItem(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(recyclerView.context, R.color.danger))
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(recyclerView.context, R.color.accent_blue))
                .addSwipeRightActionIcon(R.drawable.ic_archive)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}