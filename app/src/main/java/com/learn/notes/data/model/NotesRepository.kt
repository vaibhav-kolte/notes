package com.learn.notes.data.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotesRepository {

    // In-memory notes storage
    private val notesList = mutableListOf<Note>()

    // Expose notes as Flow (good for Compose & ViewModel observation)
    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())
    val notesFlow: StateFlow<List<Note>> = _notesFlow.asStateFlow()

    fun getAllNotes(): List<Note> = _notesFlow.value

    fun addNote(note: Note) {
        notesList.add(note)
        _notesFlow.value = notesList.toList() // emit new state
    }

    fun updateNote(note: Note) {
        val index = notesList.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notesList[index] = note
            _notesFlow.value = notesList.toList()
        }
    }

    fun deleteNote(id: Long) {
        notesList.removeAll { it.id == id }
        _notesFlow.value = notesList.toList()
    }

    fun getNoteById(id: Long): Note? {
        return notesList.find { it.id == id }
    }
}