package com.ken.aimockinterview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ken.aimockinterview.ui.theme.lightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader(
    isHomeScreen: Boolean,
    onClick: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = lightBlue
        ),
        title = {
            Text(
                "Tech Prep AI",
                color = Color.White,
                modifier = modifier.wrapContentWidth(Alignment.Start)

            )
        },
        actions = {
            if (isHomeScreen) {
                IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                    Icon(
                        Icons.Default.MoreVert,
                        "",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false },
                    modifier = modifier
                        .background(Color.White)
                        .padding(horizontal = 10.dp),
                    content = {
                        DropdownMenuItem(
                            onClick = {
                                mDisplayMenu = false
                                onProfile()
                            },
                            text = {
                                Text("Profile")
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                )
                            }
                        )

                        DropdownMenuItem(
                            onClick = {
                                mDisplayMenu = false
                                onClick()
                            },
                            text = {
                                Text("Logout")
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                )
                            }
                        )

                    }
                )

                /*   Icon(
                       Icons.AutoMirrored.Filled.Logout,
                       tint = Color.White,
                       contentDescription = null,
                       modifier = modifier
                           .wrapContentWidth(align = Alignment.End)
                           .padding(end = 20.dp)
                           .clickable(onClick = { onClick() })
                   )*/
            }
        }
    )
}
