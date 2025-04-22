package com.rexvit.retext

import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class AppSelectionAdapter(
    private var apps: MutableList<AppInfo>,
    private val onItemDeleted: (AppInfo, Int) -> Unit
) : RecyclerView.Adapter<AppSelectionAdapter.AppViewHolder>() {

    private val selectedApps = mutableSetOf<String>()

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: com.google.android.material.card.MaterialCardView = view.findViewById(R.id.cardView)
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val appName: TextView = view.findViewById(R.id.appName)
        val appCheckbox: CheckBox = view.findViewById(R.id.appCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_selection, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        Log.d("lokesh-meena", "pos : $position")
        val app = apps[position]
        holder.appIcon.setImageDrawable(app.icon)
        holder.appName.text = app.name
        holder.appCheckbox.isChecked = selectedApps.contains(app.packageName)

        holder.appCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedApps.add(app.packageName)
            } else {
                selectedApps.remove(app.packageName)
            }
        }

        holder.cardView.setOnClickListener {
            holder.appCheckbox.toggle()
        }
    }

    override fun getItemCount() = apps.size

    fun getSelectedApps(): List<String> = selectedApps.toList()

    fun deleteItem(position: Int) {
        val deletedItem = apps[position]
        apps.removeAt(position)
        notifyItemRemoved(position)
        onItemDeleted(deletedItem, position)
    }

    fun restoreItem(item: AppInfo, position: Int) {
        apps.add(position, item)
        notifyItemInserted(position)
    }

    val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    fun updateSelectedApps(selectedPackages: Set<String>) {
        selectedApps.clear()
        selectedApps.addAll(selectedPackages)
        notifyDataSetChanged()
    }
}