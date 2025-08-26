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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.learn.notes.ui.screens.AddNoteScreen
import com.learn.notes.ui.screens.NotesScreen
import com.learn.notes.ui.theme.NotesTheme
import com.learn.notes.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {

                val viewModel: NotesViewModel = viewModel()

                val navController = rememberNavController()

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
                                notesViewModel = viewModel,
                                navController = navController,
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
                                notesViewModel = viewModel,
                                navController = navController,
                                noteId = noteId.toLong(),
                            )
                        }
                    }
                }
            }
        }
    }
}