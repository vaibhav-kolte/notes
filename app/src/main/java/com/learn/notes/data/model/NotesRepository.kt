package com.learn.notes.data.model

import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {

    // Expose notes as Flow from Room (auto-updates when DB changes)
    val notesFlow: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(id: Long) {
        noteDao.deleteById(id)
    }

    suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)
    }
}
