package com.example.projectservedraft2

import SampleViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.projectservedraft2.ui.theme.ProjectServeDraft2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectServeDraft2Theme {
                val navController = rememberNavController()
                BottomNavBar(navController)
                NavHost(
                    navController = navController,
                    startDestination = "GroupPage",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("GroupPage") {
                        GroupPageView(navController)
                    }
                    composable("GalleryPage") {
                        GalleryPageView()
                    }
                    composable("SettingsPage") {
                        SettingsPageView()
                    }
                    composable("GroupInsidePage") {
                        GroupInsideView(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(navHostController: NavHostController) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    var isSelected1 by remember{ mutableStateOf(true)}
    var isSelected2 by remember{ mutableStateOf(false)}
    var isSelected3 by remember{ mutableStateOf(false)}

    var item1IconColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var item2IconColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var item3IconColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    Scaffold(
        bottomBar = {
            if (
                navBackStackEntry?.destination?.route == "GroupPage" ||
                navBackStackEntry?.destination?.route == "GalleryPage" ||
                navBackStackEntry?.destination?.route == "SettingsPage"
                ) {
                BottomAppBar() {
                    NavigationBarItem(
                        selected = true,
                        onClick = {
                            navHostController.navigate("GroupPage")
                            isSelected1 = true
                            isSelected2 = false
                            isSelected3 = false
                        },
                        icon = {
                            if (isSelected1) {
                                item1IconColor = Color(0xFF2778D7)
                            } else {
                                item1IconColor = Color(0xFF000000)
                            }
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.person_3_fill),
                                        modifier = Modifier.size(20.dp),
                                        tint = item1IconColor,
                                        contentDescription = ""
                                    )
                                    Text("Groups")
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = {
                            navHostController.navigate("GalleryPage")
                            isSelected1 = false
                            isSelected2 = true
                            isSelected3 = false
                        },
                        icon = {
                            if (isSelected2) {
                                item2IconColor = Color(0xFF2778D7)
                            } else {
                                item2IconColor = Color(0xFF000000)
                            }
                            Box(
                                contentAlignment = Alignment.Center
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Icon(
                                        painter = painterResource(id = R.drawable.camera_fill),
                                        modifier = Modifier.size(20.dp),
                                        tint = item2IconColor,
                                        contentDescription = ""
                                    )
                                    Text("Gallery")
                                }
                            }
                        }
                    )
                    NavigationBarItem(
                        selected = true,
                        onClick = {
                            navHostController.navigate("SettingsPage")
                            isSelected1 = false
                            isSelected2 = false
                            isSelected3 = true
                        },
                        icon = {
                            if (isSelected3) {
                                item3IconColor = Color(0xFF2778D7)
                            } else {
                                item3IconColor = Color(0xFF000000)
                            }
                            Box(
                                contentAlignment = Alignment.Center
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Icon(
                                        painter = painterResource(id = R.drawable.gearshape_fill),
                                        modifier = Modifier.size(20.dp),
                                        tint = item3IconColor,
                                        contentDescription = ""
                                    )
                                    Text("Settings")
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        }
    }
}

@Composable
fun GroupPageView(navHostController: NavHostController) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = "This is page 1"
    )
}

@Composable
fun GalleryPageView() {
    Column{
        Text("hello this is page 2")
    }
}

@Composable
fun SettingsPageView() {
    Column{
        Text("hello this is page 3")
    }
}

@Composable
fun GroupInsideView(navHostController: NavHostController) {
    Column{
        Text("hello this is page 3")
    }
}
