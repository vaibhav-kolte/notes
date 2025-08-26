package com.learn.notes.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.learn.notes.data.model.Note

class NotesViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    fun addNote(note: Note) {
        _notes.add(note)
    }

    fun updateNote(note: Note) {
        val index = _notes.indexOfFirst { it.id == note.id }
        if (index != -1) _notes[index] = note
    }

    fun deleteNote(id: Long) {
        _notes.removeAll { it.id == id }
    }
}