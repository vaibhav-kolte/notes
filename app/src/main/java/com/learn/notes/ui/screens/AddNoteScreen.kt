package com.learn.notes.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.learn.notes.data.model.Note
import com.learn.notes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    notesViewModel: NotesViewModel,
    navController: NavController,
    noteId: Long = -1L,
) {

    val context = LocalContext.current
    val todayCalendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var existingNote by remember { mutableStateOf<Note?>(null) }
    LaunchedEffect(noteId) {
        existingNote = notesViewModel.getNoteById(noteId)
    }
    val notes by notesViewModel.notes.collectAsState()


    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(dateFormat.format(todayCalendar.time)) }

    // Populate fields if editing existing note
    LaunchedEffect(existingNote) {
        existingNote?.let { note ->
            title = TextFieldValue(note.title)
            description = TextFieldValue(note.content)
            // convert timestamp (Long) -> formatted date string
            date = dateFormat.format(Date(note.timestamp))
        }
    }

    fun openDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val selectedDate = dateFormat.format(selectedCalendar.time)
                if (!selectedCalendar.before(todayCalendar)) {
                    date = selectedDate
                }
            },
            todayCalendar.get(Calendar.YEAR),
            todayCalendar.get(Calendar.MONTH),
            todayCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = todayCalendar.timeInMillis
        datePickerDialog.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId != -1L) "Edit Note" else "Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("note_list") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                singleLine = false,
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { openDatePicker() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (title.text.isNotBlank() && description.text.isNotBlank()) {
                            val parsedDate = try {
                                dateFormat.parse(date)?.time ?: System.currentTimeMillis()
                            } catch (e: Exception) {
                                System.currentTimeMillis()
                            }

                            val note = Note(
                                id = existingNote?.id ?: (notes.size + 1L),
                                title = title.text,
                                content = description.text,
                                timestamp = parsedDate
                            )

                            if (existingNote != null) {
                                notesViewModel.updateNote(note)
                            } else {
                                notesViewModel.addNote(note)
                            }
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (existingNote != null) "Update" else "Save")
                }
            }
        }
    }
}