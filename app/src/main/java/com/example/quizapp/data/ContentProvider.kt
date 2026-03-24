package com.example.quizapp.data


import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.quizapp.model.QuizImage
import kotlinx.coroutines.runBlocking

class QuizContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.quizapp.provider"
        const val PATH_ENTRIES = "entries"

        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_ENTRIES")

        private const val ENTRIES = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_ENTRIES, ENTRIES)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) != ENTRIES) {
            throw IllegalArgumentException("Unknown URI: $uri")
        }

        val context = context ?: return null
        val dao = AppDatabase.getDatabase(context).quizImageDao()

        val entries: List<QuizImage> = runBlocking {
            dao.getAll()
        }

        val cursor = MatrixCursor(arrayOf("name", "URI"))

        entries.forEach { entry ->
            cursor.addRow(arrayOf(entry.name, entry.imageUri))
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            ENTRIES -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_ENTRIES"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Insert not supported")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Delete not supported")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException("Update not supported")
    }
}