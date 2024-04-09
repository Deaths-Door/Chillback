package com.deathsdoor.chillback.ui.shortcut

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.window.MenuScope
import androidx.lifecycle.viewModelScope
import com.deathsdoor.chillback.data.backgroundtasks.callImmediatelyOnApplicationFinish
import com.deathsdoor.chillback.ui.providers.LocalAppState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun MenuScope.ShortCutMenu(exitApplication: () -> Unit) {
    Menu(
        text = "Shortcuts",
        content = {
            // TODO : Create shortcuts for android and desktop and Add more shortcuts / quickactions

          //  var isExitDialogOpen by remember { mutableStateOf(false) }
            Item(
                text = "Exit",
                // TODO : I don't understand why it doesnt work
                //icon = rememberVectorPainter(Icons.Default.Close),
                // TODO : Allow Alt + F4 for windows??
                shortcut = KeyShortcut(
                    ctrl = true,
                    key = Key.Q
                ),

                onClick = { }//isExitDialogOpen = true }
            )

           /* // TODO : Add option to chcek if we need to run tasks
            if(!isExitDialogOpen) return@Menu

            val appState = LocalAppState.current
            AlertDialog(
                onDismissRequest = { isExitDialogOpen = false },
                title = { Text("Run Important Tasks before closing") },
                text = {
                    Text(
                        text = buildAnnotatedString {
                            val bold = SpanStyle(fontWeight = FontWeight.Bold)
                            withStyle(style = bold) {
                                append("Not allowing these tasks")
                            }

                            append("to be run will cause some of ")

                            withStyle(style = bold + SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append("your data to be lost!")
                            }

                            withStyle(style = bold) {
                                append("\nThis action is irreversible!")
                            }
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            appState.viewModelScope.launch {
                                callImmediatelyOnApplicationFinish(appState)
                                exitApplication()
                            }
                        },
                        content = {
                            Text("Run Tasks")
                        }
                    )
                },
                dismissButton = {
                    Button(
                        onClick = exitApplication,
                        content = {
                            Text("Exit")
                        }
                    )
                }
            )*/
        }
    )
}