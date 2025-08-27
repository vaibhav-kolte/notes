package com.learn.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.notes.data.model.Note
import com.learn.notes.data.model.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class NotesViewModel(private val repository: NotesRepository = NotesRepository()) : ViewModel() {

    val notes: StateFlow<List<Note>> = repository.notesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getAllNote() = repository.getAllNotes()

    fun addNote(note: Note) = repository.addNote(note)

    fun updateNote(note: Note) = repository.updateNote(note)

    fun deleteNote(id: Long) = repository.deleteNote(id)

    fun getNoteById(id:Long) = repository.getNoteById(id)
}