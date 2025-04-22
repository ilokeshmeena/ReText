package com.rexvit.retext

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ReText.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_SELECTED_APPS = "selected_apps"
        private const val COL_PACKAGE_NAME = "package_name"

        private const val TABLE_DELETED_NOTIFICATIONS = "deleted_notifications"
        private const val COL_ID = "id"
        private const val COL_APP_NAME = "app_name"
        private const val COL_TITLE = "title"
        private const val COL_TEXT = "text"
        private const val COL_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_SELECTED_APPS (
                $COL_PACKAGE_NAME TEXT PRIMARY KEY
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_DELETED_NOTIFICATIONS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PACKAGE_NAME TEXT,
                $COL_APP_NAME TEXT,
                $COL_TITLE TEXT,
                $COL_TEXT TEXT,
                $COL_TIMESTAMP INTEGER
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SELECTED_APPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DELETED_NOTIFICATIONS")
        onCreate(db)
    }

    fun saveSelectedApps(packageNames: List<String>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(TABLE_SELECTED_APPS, null, null)

            packageNames.forEach { packageName ->
                val values = ContentValues().apply {
                    put(COL_PACKAGE_NAME, packageName)
                }
                db.insert(TABLE_SELECTED_APPS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun isAppSelected(packageName: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SELECTED_APPS,
            arrayOf(COL_PACKAGE_NAME),
            "$COL_PACKAGE_NAME = ?",
            arrayOf(packageName),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAllSelectedApps(): Set<String> {
        val selectedApps = mutableSetOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_SELECTED_APPS,
            arrayOf(COL_PACKAGE_NAME),
            null, null, null, null, null
        )

        while (cursor.moveToNext()) {
            selectedApps.add(cursor.getString(0))
        }
        cursor.close()
        return selectedApps
    }

    fun saveDeletedNotification(notification: DeletedNotification) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PACKAGE_NAME, notification.packageName)
            put(COL_APP_NAME, notification.appName)
            put(COL_TITLE, notification.title)
            put(COL_TEXT, notification.text)
            put(COL_TIMESTAMP, notification.timestamp)
        }
        db.insert(TABLE_DELETED_NOTIFICATIONS, null, values)
    }

    fun getDeletedNotifications(): List<DeletedNotification> {
        val notifications = mutableListOf<DeletedNotification>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DELETED_NOTIFICATIONS,
            arrayOf(COL_PACKAGE_NAME, COL_APP_NAME, COL_TITLE, COL_TEXT, COL_TIMESTAMP),
            null, null, null, null,
            "$COL_TIMESTAMP DESC"
        )

        while (cursor.moveToNext()) {
            notifications.add(
                DeletedNotification(
                    packageName = cursor.getString(0),
                    appName = cursor.getString(1),
                    title = cursor.getString(2),
                    text = cursor.getString(3),
                    timestamp = cursor.getLong(4)
                )
            )
        }
        cursor.close()
        return notifications
    }

    fun deleteNotification(notification: DeletedNotification): Boolean {
        val db = writableDatabase
        return db.delete(
            TABLE_DELETED_NOTIFICATIONS,
            "$COL_PACKAGE_NAME = ? AND $COL_TIMESTAMP = ?",
            arrayOf(notification.packageName, notification.timestamp.toString())
        ) > 0
    }

    fun deleteAllNotifications(): Int {
        val db = writableDatabase
        return db.delete(TABLE_DELETED_NOTIFICATIONS, null, null)
    }

    fun clearDatabase() {
        val db = writableDatabase
        db.delete(TABLE_SELECTED_APPS, null, null)
        db.delete(TABLE_DELETED_NOTIFICATIONS, null, null)
    }
}