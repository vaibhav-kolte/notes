package com.learn.notes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.learn.notes.data.model.Note
import com.learn.notes.ui.screens.AddNoteScreen
import com.learn.notes.ui.screens.NotesScreen
import com.learn.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                val navController = rememberNavController()
                var notes by remember { mutableStateOf(emptyList<Note>()) }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            navController.navigate("add_note")
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "note_list",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("note_list") {
                            NotesScreen(
                                navController = navController,
                                notes = notes,
                                onDeleteNote = { noteToDelete ->
                                    notes = notes.filter { it.id != noteToDelete.id }
                                }
                            )
                        }
                        composable(
                            route = "add_note?noteId={noteId}",
                            arguments = listOf(
                                navArgument("noteId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1
                            AddNoteScreen(
                                navController = navController,
                                notes = notes,
                                noteId = noteId.toLong(),
                                onAddNote = { note ->
                                    notes = notes + note
                                },
                                onUpdateNote = { updated ->
                                    notes = notes.map { note ->
                                        if (note.id == updated.id) updated else note
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}