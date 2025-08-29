package com.learn.notes.data.model

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: NoteDatabase? = null

    fun getDatabase(context: Context): NoteDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}