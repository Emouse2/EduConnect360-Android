package com.example.projectservedraft2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.projectservedraft2.ui.theme.ProjectServeDraft2Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isSystemInDarkThemeBoolean: Boolean = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(isSystemInDarkThemeBoolean) }
            ProjectServeDraft2Theme (darkTheme = isDarkTheme) {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Black
                    )
                }
                var SSTudent by remember { mutableStateOf(true) }
                var unconfirmedEmail by remember {mutableStateOf("")}
                var nameOfPersonUsingApp by remember { mutableStateOf("") }
                var idOfPersonUsingApp by remember { mutableStateOf("") }
                val navController = rememberNavController()
                var emailVerified by remember {mutableStateOf(false)}
                if (SSTudent) {
                    emailVerified = true
                }

                NavHost(
                    navController = navController,
                    startDestination = "BottomNavBar",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(
                        route = "StartPage",
                        enterTransition = { fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) }
                    ) {
                        StartPageView(
                            navController,
                            updatedSSTudent = { newState -> SSTudent = newState }
                        )
                    }
                    composable(
                        route = "SignInStudentPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        SignInStudentPageView(
                            navController,
                            updatedNameOfPersonUsingApp = { newState ->
                                nameOfPersonUsingApp = newState
                            },
                            updatedIdOfPersonUsingApp = { newState ->
                                idOfPersonUsingApp = newState
                            }
                        )
                    }
                    composable(
                        route = "SignUpStudentPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        SignUpStudentPageView(
                            navController,
                            nameOfPersonUsingApp,
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState }
                        )
                    }
                    composable(
                        route = "SignInStaffPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        SignInStaffPageView(
                            navController,
                            updatedNameOfPersonUsingApp = { newState ->
                                nameOfPersonUsingApp = newState
                            },
                            updatedIdOfPersonUsingApp = { newState ->
                                idOfPersonUsingApp = newState
                            }
                        )
                    }
                    composable(
                        route = "SignUpStaffPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        SignUpStaffPageView(
                            navController,
                            nameOfPersonUsingApp,
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState },
                            updatedUnconfirmedEmail = {newState -> unconfirmedEmail = newState},
                            updatedEmailVerified = {newState -> emailVerified = newState}
                        )
                    }
                    composable(
                        route = "VerifyEmailPage",
                        enterTransition = { slideInVertically(initialOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeOut(tween(500)) }
                    ){
                        VerifyEmailPageView(navController, unconfirmedEmail, emailVerified)
                    }
                    composable(
                        route = "BottomNavBar",
                        enterTransition = { fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) }
                    ) {
                        BottomNavBar(
                            navController,
                            SSTudent,
                            isDarkTheme,
                            nameOfPersonUsingApp,
                            idOfPersonUsingApp,
                            promptManager,
                            updatedIsDarkTheme = { newState -> isDarkTheme = newState },
                            updatedNameOfPersonUsingApp = { newState ->
                                nameOfPersonUsingApp = newState
                            }
                        )
                    }
                    composable(
                        route = "GroupInsidePage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        GroupInsideView(navController)
                    }
                    composable(
                        route = "CreateNewGroupPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        CreateNewGroupPageView()
                    }
                    composable(
                        route = "preview/{imageUri}",
                        enterTransition = { slideInVertically(initialOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeOut(tween(500)) }
                    ) { backStackEntry ->
                        val imageUri = backStackEntry.arguments?.getString("imageUri")?.let { Uri.parse(it) }
                        imageUri?.let {
                            ChangePfpPageView(navController, it, idOfPersonUsingApp)
                        }
                    }
                    composable(
                        route = "ChangePasswordPage",
                        enterTransition = { slideInVertically(initialOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }, animationSpec = tween(500)) + fadeOut(tween(500)) }
                    ){
                        ChangePasswordPageView(idOfPersonUsingApp, navController)
                    }
                }
            }
        }
    }
}

data class StoredData(val StoredData:String)

fun getUsernameAndIdByEmail(
    email: String,
    onResult: (String?, String?, Exception?) -> Unit
) {
    val db = Firebase.firestore
    db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result
                if (!documents.isEmpty) {
                    val userDoc = documents.documents[0]
                    val fullName = userDoc.getString("fullname")
                    val userId = userDoc.getString("id")
                    onResult(userId, fullName, null)
                } else {
                    onResult(null, null, Exception("No user found with the given email"))
                }
            } else {
                onResult(null, null, task.exception)
            }
        }
}


fun signIn(
    email: String,
    password: String,
    onResult: (AuthResult?, String, String, Exception?) -> Unit
) {
    Firebase.auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getUsernameAndIdByEmail(email) { userId, name, exception ->
                    if (exception != null) {
                        onResult(task.result, "", "", exception)
                    } else {
                        val userName = name ?: "Default User"
                        val userId = userId ?: "No ID"
                        onResult(task.result, userName, userId, null)
                    }
                }
            } else {
                // Authentication failed, pass the exception
                onResult(null, "", "", task.exception)
            }
        }
}


fun signUp(
    email: String,
    password: String,
    name: String,
    updatedEmailVerified: (Boolean) -> Unit,
    onResult: (AuthResult?, Exception?) -> Unit
) {
    val auth = Firebase.auth
    val db = Firebase.firestore

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    it.sendEmailVerification()
                        .addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    var isVerified = false
                                    while (!isVerified) {
                                        delay(5000) // Check every 5 seconds
                                        user.reload().addOnCompleteListener { reloadTask ->
                                            if (reloadTask.isSuccessful) {
                                                isVerified = user.isEmailVerified
                                            }
                                        }
                                    }
                                    withContext(Dispatchers.Main) {
                                        updatedEmailVerified(true)
                                        db.collection("users").document(user.uid)
                                            .update("emailVerified", true)
                                    }
                                }
                            } else {
                                onResult(null, emailTask.exception)
                            }
                        }
                }
                user?.let {
                    val userData = hashMapOf(
                        "fullname" to name,
                        "email" to email,
                        "id" to it.uid,
                        "password" to password
                    )

                    db.collection("users").document(it.uid)
                        .set(userData)
                        .addOnCompleteListener { firestoreTask ->
                            onResult(if (firestoreTask.isSuccessful) task.result else null, firestoreTask.exception)
                        }
                }
            } else {
                onResult(null, task.exception)
            }
        }
}

fun Color.darker(factor: Float): Color {
    return lerp(this, Color.Black, factor)
}
fun Color.lighter(factor: Float): Color {
    return lerp(this, Color.White, factor)
}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavBar(
    navHostController: NavHostController,
    SSTudent: Boolean,
    isDarkTheme: Boolean,
    nameOfPersonUsingApp: String,
    idOfPersonUsingApp: String,
    promptManager: BiometricPromptManager,
    updatedIsDarkTheme: (Boolean) -> Unit,
    updatedNameOfPersonUsingApp: (String) -> Unit
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val nestedNavController = rememberNavController()
    val nestedNavBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    var isSelected1 by remember{ mutableStateOf(true)}
    var isSelected2 by remember{ mutableStateOf(false)}
    var isSelected3 by remember{ mutableStateOf(false)}
    var isSelected4 by remember{ mutableStateOf(false)}

    Scaffold(
        bottomBar = {
                NavigationBar (
                    containerColor = containerColor,
                    tonalElevation = 2.dp
                ){
                    if(navBackStackEntry?.destination?.route == "GroupPage"){
                        isSelected1 = true
                        isSelected2 = false
                        isSelected3 = false
                        isSelected4 = false
                    }
                    NavigationBarItem(
                        selected = isSelected1,
                        onClick = {
                            nestedNavController.navigate("GroupPage")
                            isSelected1 = true
                            isSelected2 = false
                            isSelected3 = false
                            isSelected4 = false
                        },
                        icon = {
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
                                        contentDescription = ""
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2778D7),
                            selectedTextColor = Color(0xFF2778D7),
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        label = {Text(
                            text = "Groups",
                            fontWeight = if (isSelected1) {
                                FontWeight.Bold
                            } else{
                                FontWeight.Normal
                            }
                        )}
                    )
                    NavigationBarItem(
                        selected = isSelected2,
                        onClick = {
                            nestedNavController.navigate("GalleryPage")
                            isSelected1 = false
                            isSelected2 = true
                            isSelected3 = false
                            isSelected4 = false
                        },
                        icon = {
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
                                        contentDescription = ""
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2778D7),
                            selectedTextColor = Color(0xFF2778D7),
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        label = {Text(
                            text = "Gallery",
                            fontWeight = if (isSelected2) {
                                FontWeight.Bold
                            } else{
                                FontWeight.Normal
                            }
                        )}
                    )
                    NavigationBarItem(
                        selected = isSelected3,
                        onClick = {
                            nestedNavController.navigate("SettingsPage")
                            isSelected1 = false
                            isSelected2 = false
                            isSelected3 = true
                            isSelected4 = false
                        },
                        icon = {
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
                                        contentDescription = ""
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2778D7),
                            selectedTextColor = Color(0xFF2778D7),
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        label = {Text(
                            text = "Settings",
                            fontWeight = if (isSelected3) {
                                FontWeight.Bold
                            } else{
                                FontWeight.Normal
                            }
                        )}
                    )
                    if (!SSTudent) {
                        NavigationBarItem(
                            selected = isSelected4,
                            onClick = {
                                nestedNavController.navigate("PhotobookPage")
                                isSelected1 = false
                                isSelected2 = false
                                isSelected3 = false
                                isSelected4 = true
                            },
                            icon = {
                                Box(
                                    contentAlignment = Alignment.Center
                                ){
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.photo_fill_on_rectangle_fill),
                                            modifier = Modifier.size(20.dp),
                                            contentDescription = ""
                                        )
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF2778D7),
                                selectedTextColor = Color(0xFF2778D7),
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground
                            ),
                            label = {Text(
                                text = "Photobook",
                                fontWeight = if (isSelected4) {
                                    FontWeight.Bold
                                } else{
                                    FontWeight.Normal
                                }
                            )}
                        )
                    }
                }
        },
        floatingActionButton = {
            if (!SSTudent && nestedNavBackStackEntry?.destination?.route == "GroupPage") {
                FloatingActionButton(
                    onClick = { navHostController.navigate("CreateNewGroupPage") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
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
        NavHost(
            navController = nestedNavController,
            startDestination = "GroupPage",
            modifier = Modifier.fillMaxSize()
        ) {
            val noEnterTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
                EnterTransition.None
            }
            val noExitTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
                ExitTransition.None
            }
            composable(
                route = "GroupPage",
                enterTransition = noEnterTransition,
                exitTransition = noExitTransition,
                popEnterTransition = noEnterTransition,
                popExitTransition = noExitTransition
            ) {
                GroupPageView(navHostController, nameOfPersonUsingApp)
            }
             composable(
                 route = "GalleryPage",
                 enterTransition = noEnterTransition,
                 exitTransition = noExitTransition,
                 popEnterTransition = noEnterTransition,
                 popExitTransition = noExitTransition
            ) {
                GalleryPageView()
            }
            composable(
                route = "SettingsPage",
                enterTransition = noEnterTransition,
                exitTransition = noExitTransition,
                popEnterTransition = noEnterTransition,
                popExitTransition = noExitTransition
            ) {
                SettingsPageView(
                    navHostController,
                    SSTudent,
                    nameOfPersonUsingApp,
                    idOfPersonUsingApp,
                    promptManager,
                    updatedIsDarkTheme2 = {newState -> updatedIsDarkTheme(newState)},
                    updatedNameOfPersonUsingApp2 = { newState -> updatedNameOfPersonUsingApp(newState)}
                )
            }
            composable(
                route = "PhotobookPage",
                enterTransition = noEnterTransition,
                exitTransition = noExitTransition,
                popEnterTransition = noEnterTransition,
                popExitTransition = noExitTransition
            ) {
                PhotobookPageView()
            }
        }
    }
}

@Composable
fun AlertDialog1(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: @Composable () -> Unit,
    confirmText: String,
    icon: ImageVector
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            dialogText()
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun getGreetingMessage(currentTime: LocalTime): String {
    val morningStart = LocalTime.of(6, 0)
    val afternoonStart = LocalTime.of(12, 0)
    val eveningStart = LocalTime.of(18, 0)

    val greeting = when {
        currentTime.isAfter(morningStart) && currentTime.isBefore(LocalTime.of(12, 0)) -> "Good morning"
        currentTime.isAfter(afternoonStart) && currentTime.isBefore(eveningStart) -> "Good afternoon"
        else -> "Good evening"
    }
    return "$greeting,"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupPageView(navHostController: NavHostController, nameOfPersonUsingApp: String) {
    val currentTime = LocalTime.now()
    val greeting = remember { mutableStateOf(getGreetingMessage(currentTime)) }
    var groupsList by remember{ mutableStateOf(listOf<String>()) }

    LaunchedEffect(currentTime) {
        greeting.value = getGreetingMessage(currentTime)
    }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.sst_hexagon_logo),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.offset(x = -30.dp, y = -40.dp),
            alpha = 0.5f
        )
    }
    Column (
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = greeting.value,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = nameOfPersonUsingApp,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.offset(y = -10.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Groups",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.offset(y = -10.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { navHostController.navigate("GroupInsidePage") },
            shape = RoundedCornerShape(16.dp),
            colors = buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Text(
                text = "S2-01",
                modifier = Modifier.padding(20.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.weight(1.0f))
        }
    }
}


@Composable
fun GalleryPageView() {
    var galleryPhotosList by remember{ mutableStateOf(List(20) { "Item $it" }) }
    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row{
            Text(
                text = "Gallery",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.offset(y = (-10).dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1.0f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Column{
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.height(585.dp)
            ) {
                items(galleryPhotosList) { galleryPhoto ->
                    Column(
                        modifier = Modifier
                            .size(120.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .verticalScroll(rememberScrollState())
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(text = galleryPhoto, color = Color.White)
                        Spacer(modifier = Modifier.weight(1.0f))
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SettingsPageView(
    navHostController: NavHostController,
    SSTudent: Boolean,
    nameOfPersonUsingApp: String,
    idOfPersonUsingApp: String,
    promptManager: BiometricPromptManager,
    updatedIsDarkTheme2: (Boolean) -> Unit,
    updatedNameOfPersonUsingApp2: (String) -> Unit
) {
    var currentTheme by remember {mutableStateOf("System default")}
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    var userEmail by remember { mutableStateOf<String?>(null) }
    var userPassword by remember { mutableStateOf<String?>(null) }
    var userPfpUrl by remember{mutableStateOf<String?>("")}

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )
    LaunchedEffect(idOfPersonUsingApp) {
        if (!idOfPersonUsingApp.isNullOrEmpty()) {
            usersCollection.document(idOfPersonUsingApp).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userEmail = document.getString("email")
                        userPassword = document.getString("password")
                        userPfpUrl = document.getString("pfpUrl")
                    } else {
                        userEmail = ""
                        userPassword = ""
                        userPfpUrl = ""
                    }
                }
                .addOnFailureListener { exception ->
                    userEmail = ""
                    userPassword = ""
                    userPfpUrl = ""
                }
        } else {
            userEmail = ""
            userPassword = ""
        }
    }
    val isSystemInDarkThemeBoolean: Boolean = isSystemInDarkTheme()
    var openAppThemeAlertDialog by remember {mutableStateOf(false)}
    var openAlertDialog1 by remember { mutableStateOf(false) }
    var appNotifications by remember { mutableStateOf(true) }

    var showingPassword by remember {mutableStateOf(false)}

    val biometricResult by promptManager.promptResults.collectAsState(
        initial = null
    )
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity result: $it")
        }
    )
    LaunchedEffect(biometricResult) {
        if(biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
            if(Build.VERSION.SDK_INT >= 30) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }
        }
    }

    biometricResult.let { result ->
        when(result){
            is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                showingPassword = false
            }
            BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                showingPassword = false
            }
            BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                showingPassword = false
            }
            BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                showingPassword = true
            }
            BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                showingPassword = false
            }
            BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                showingPassword = false
            }
            null -> {
                showingPassword = false
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ){
        Image(
            painter = painterResource(id = R.drawable.sst_hexagon_logo),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.offset(x = 30.dp, y = (-40).dp),
            alpha = 0.5f
        )
    }
    Column(modifier = Modifier.padding(10.dp)){
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.offset(y = (-10).dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    BoxWithConstraints(
        modifier = Modifier
            .padding(10.dp)
            .offset(y = 80.dp)
    ) {
        val maxHeight = 588.dp
        Box(
            modifier = Modifier
                .height(maxHeight)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                Text(
                    text = "APP SETTINGS",
                    modifier = Modifier.padding(5.dp),
                    color = Color(0xFF808080),
                    fontWeight = FontWeight.Bold
                )
                ElevatedCard(elevation = CardDefaults.cardElevation(3.dp)) {
                    ListItem(
                        headlineContent = { Text("App Theme") },
                        modifier = Modifier.clickable { openAppThemeAlertDialog = true },
                        trailingContent = { Text(text = currentTheme, color = Color(0xFF808080)) }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text("Notifications") },
                        modifier = Modifier.clickable { appNotifications = !appNotifications },
                        trailingContent = { Switch(checked = appNotifications, onCheckedChange = { appNotifications = it }) }
                    )
                }
                Text(
                    text = "ACCOUNT SETTINGS",
                    modifier = Modifier.padding(5.dp),
                    color = Color(0xFF808080),
                    fontWeight = FontWeight.Bold
                )
                ElevatedCard(elevation = CardDefaults.cardElevation(3.dp)) {
                    imageUri?.let {
                        navHostController.navigate("preview/${Uri.encode(it.toString())}")
                    }
                    ListItem(
                        headlineContent = { Text("Profile Picture") },
                        leadingContent = {
                            if (userPfpUrl?.isEmpty() == true || userPfpUrl == null){
                                userPfpUrl = "https://firebasestorage.googleapis.com/v0/b/curated-7ac4b.appspot.com/o/profilePictures%2FDefault%20profile.png?alt=media&token=8215576d-6adf-4642-96dd-292bddc498d4"
                            }
                            Image(
                                painter = rememberImagePainter(userPfpUrl),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        },
                        modifier = Modifier.clickable {
                            launcher.launch("image/*")
                        }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text("Username") },
                        trailingContent = { Text(nameOfPersonUsingApp)}
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text("SST Email") },
                        trailingContent = { Text("$userEmail")}
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text("Password") },
                        modifier = Modifier.clickable {
                            if(!showingPassword) {
                                promptManager.showBiometricPrompt(
                                    title = "Biometric",
                                    description = "View your password using your biometric credential or device PIN"
                                )
                            }
                                                      },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Text(
                                    if (showingPassword){
                                        "$userPassword"
                                    } else {
                                        val asteriskString = "*".repeat("$userPassword".length)
                                        asteriskString
                                    }
                                )
                                Spacer(Modifier.width(10.dp))
                                if (showingPassword) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.eye_fill),
                                        contentDescription = "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.eye_slash_fill),
                                        contentDescription = "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text(text = if (SSTudent) "Account type: SSTudent" else "Account type: SSTaff") }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text(text = "Change Password", color = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { navHostController.navigate("ChangePasswordPage") }
                    )
                }
                Text(
                    text = "OTHER",
                    modifier = Modifier.padding(5.dp),
                    color = Color(0xFF808080),
                    fontWeight = FontWeight.Bold
                )
                ElevatedCard(elevation = CardDefaults.cardElevation(6.dp)) {
                    ListItem(
                        headlineContent = { Text(text = "Log Out", color = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { openAlertDialog1 = true }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text(text = "Delete Account", color = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { }
                    )
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
    when {openAlertDialog1 -> {
        var passwordInput by remember{mutableStateOf("")}
        var errorMessage by remember{mutableStateOf("")}
        AlertDialog1(
            onDismissRequest = { openAlertDialog1 = false },
            onConfirmation = {
                if (passwordInput == userPassword) {
                    openAlertDialog1 = false
                    navHostController.navigate("StartPage")
                    updatedNameOfPersonUsingApp2("")
                    Firebase.auth.signOut()
                } else {
                    errorMessage = "Incorrect password."
                }
                             },
            dialogTitle = "Log Out",
            dialogText = {
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        "Are you sure you want to log out?",
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Please enter your password to confirm that you want to Log Out.",
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                    TextField(
                        value = passwordInput,
                        onValueChange = {passwordInput = it},
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Row(modifier = Modifier.padding(10.dp)){
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.weight(1.0f))
                    }
                }
                         },
            confirmText = "Log Out",
            icon = Icons.Default.Info
        )
    } }
    var systemDefaultSelected by remember{ mutableStateOf(true) }
    var lightSelected by remember{ mutableStateOf(false) }
    var darkSelected by remember{ mutableStateOf(false) }
    if (currentTheme == "System default") {
        systemDefaultSelected = true
        lightSelected = false
        darkSelected = false
    } else{if (currentTheme == "Light") {
        systemDefaultSelected = false
        lightSelected = true
        darkSelected = false
    } else{
        systemDefaultSelected = false
        lightSelected = false
        darkSelected = true
    }}
    when {openAppThemeAlertDialog ->
        AlertDialog(
            title = {Text("Choose theme")},
            text = {
                Column{
                    ListItem(
                        headlineContent = {
                            Row (verticalAlignment = Alignment.CenterVertically){
                                RadioButton(selected = systemDefaultSelected, onClick = {
                                    systemDefaultSelected = true
                                    lightSelected = false
                                    darkSelected = false
                                })
                                Text("System default")
                            }
                                          },
                        modifier = Modifier.clickable{
                            systemDefaultSelected = true
                            lightSelected = false
                            darkSelected = false
                        }
                    )
                    ListItem(
                        headlineContent = {
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = lightSelected, onClick = {
                                    systemDefaultSelected = false
                                    lightSelected = true
                                    darkSelected = false
                                })
                                Text("Light")
                            }
                                          },
                        modifier = Modifier.clickable{
                            systemDefaultSelected = false
                            lightSelected = true
                            darkSelected = false
                        }
                    )
                    ListItem(
                        headlineContent = {
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = darkSelected, onClick = {
                                    systemDefaultSelected = false
                                    lightSelected = false
                                    darkSelected = true
                                })
                                Text("Dark")
                            }
                                          },
                        modifier = Modifier.clickable{
                            systemDefaultSelected = false
                            lightSelected = false
                            darkSelected = true
                        }
                    )
                }
                   },
            onDismissRequest = {openAppThemeAlertDialog = false},
            confirmButton = { TextButton(onClick = {
                openAppThemeAlertDialog = false
                if (systemDefaultSelected){
                    currentTheme = "System default"
                    updatedIsDarkTheme2(isSystemInDarkThemeBoolean)
                }else{if(lightSelected){
                    currentTheme = "Light"
                    updatedIsDarkTheme2(false)
                }else{
                    currentTheme = "Dark"
                    updatedIsDarkTheme2(true)
                }}
            }) {
                Text("Confirm")
            }},
            dismissButton = { TextButton(onClick = {
                openAppThemeAlertDialog = false
            }){
                Text("Cancel")
            }}
        )
    }
}


@Composable
fun PhotobookPageView(){
    Column{
        Text("photobok")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInsideView(navHostController: NavHostController) {
    var groupName by remember{ mutableStateOf("S2-01")}
    val imageUrlThing = "https://firebasestorage.googleapis.com/v0/b/curated-7ac4b.appspot.com/o/posts%2Fclassroom%20image.jpg?alt=media&token=ec570f86-9424-43cb-ae6d-9ee4b2ac18f3"
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("")},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ){
                    Spacer(Modifier.weight(1.0f))
                    Text(
                        text = groupName,
                        modifier = Modifier.padding(20.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1.0f))
                }
                Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                    ElevatedCard(elevation = CardDefaults.cardElevation(3.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.78f)
                        ){
                            Image(
                                painter = rememberAsyncImagePainter(imageUrlThing),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                        Divider()
                        ListItem(
                            headlineContent = { Text("Comment...") },
                            modifier = Modifier.clickable {  },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(R.drawable.bubble_right),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreateNewGroupPageView() {
    Text("Create a new group")
}


@Composable
fun StartPageView(navHostController: NavHostController, updatedSSTudent: (Boolean) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to CuratED",
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Row{
                Text(
                    text = "I am a...",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1.0f))
            }
            Button(
                onClick = {
                    navHostController.navigate("SignInStaffPage")
                    updatedSSTudent(false)
                          },
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = buttonColors(containerColor = Color(0xFFFF9900))
            ) {
                Text(
                    text = "SSTaff",
                    modifier = Modifier.padding(5.dp, 15.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            Button(
                onClick = {
                    navHostController.navigate("SignInStudentPage")
                    updatedSSTudent(true)
                          },
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = buttonColors(containerColor = Color(0xFFFF9900))
            ) {
                Text(
                    text = "SSTudent",
                    modifier = Modifier.padding(5.dp, 15.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInStudentPageView(navHostController: NavHostController, updatedNameOfPersonUsingApp: (String) -> Unit, updatedIdOfPersonUsingApp: (String) -> Unit){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signInText by remember {mutableStateOf("")}
    var signInError by remember {mutableStateOf<String?>(null)}
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign in (SSTudent)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sst_logo),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Email address",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = email,
                    onValueChange = {email = it},
                    placeholder= {Text("your_name@s20XX.ssts.edu.sg")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Text(
                    text = "Password",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = password,
                    onValueChange = {password = it},
                    placeholder= {Text("Enter your password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        if (email != "" && password != "") {
                            signIn(email, password) { result, name, userId, exception ->
                                if (result != null) {
                                    signInText = "Success! Going to home page..."
                                    updatedNameOfPersonUsingApp(name)
                                    updatedIdOfPersonUsingApp(userId)
                                    navHostController.navigate("BottomNavBar")
                                } else {
                                    signInError = exception?.message
                                }
                            }
                        } else {
                            signInText = "No email or password provided."
                        }
                    }) {
                        Text("Sign in")
                    }
                }
                Text(
                    text = "Don't have an account? Click here to sign up.",
                    color = Color(0xFF2778D7),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { navHostController.navigate("SignUpStudentPage") }
                        .padding(10.dp)
                )
                Text(signInError ?: signInText)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpStudentPageView(navHostController: NavHostController, nameOfPersonUsingApp: String, updatedNameOfPersonUsingApp: (String) -> Unit){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signUpText by remember {mutableStateOf ("")}
    var signUpError by remember {mutableStateOf<String?>(null)}
    var emailVerified by remember {mutableStateOf (true)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text("Sign up (SSTudent)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sst_logo),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "Email address",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = email,
                onValueChange = {email = it},
                placeholder= {Text("your_name@s20XX.ssts.edu.sg")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Text(
                text = "Password",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = password,
                onValueChange = {password = it},
                placeholder= {Text("Enter your password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Text(
                text = "Name",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = nameOfPersonUsingApp,
                onValueChange = {updatedNameOfPersonUsingApp(it)},
                placeholder= {Text("Enter your name")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    if (email != "" && password != "") {
                        signUp(email, password, nameOfPersonUsingApp, updatedEmailVerified = {newState -> emailVerified = newState}) { result, exception ->
                            if (result != null) {
                                signUpText = "Success! Going to home page..."
                                navHostController.navigate("BottomNavBar")
                            } else {
                                signUpError = exception?.message
                            }
                        }
                    } else {
                        signUpText = "No email or password provided."
                    }
                }) {
                    Text("Sign Up")
                }
            }
            Text(signUpError ?: signUpText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInStaffPageView(navHostController: NavHostController, updatedNameOfPersonUsingApp: (String) -> Unit, updatedIdOfPersonUsingApp: (String) -> Unit){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signInText by remember {mutableStateOf("")}
    var signInError by remember {mutableStateOf<String?>(null)}
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign in (SSTaff)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sst_logo),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Email address",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = email,
                    onValueChange = {email = it},
                    placeholder= {Text("your_name@sst.edu.sg")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Text(
                    text = "Password",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = password,
                    onValueChange = {password = it},
                    placeholder = {Text("Enter your password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = {
                        if (email != "" && password != "") {
                            if (email.contains("@sst.edu.sg")) {
                                signIn(email, password) { result, name, userId, exception ->
                                    if (result != null) {
                                        signInText = "Success! Going to home page..."
                                        updatedNameOfPersonUsingApp(name)
                                        navHostController.navigate("BottomNavBar")
                                    } else {
                                        signInError = exception?.message
                                    }
                                }
                            } else {
                                signInText = "Invalid SSTaff email"
                            }
                        } else {
                            signInText = "No email or password provided."
                        }
                    }) {
                        Text("Sign in")
                    }
                }
                Text(
                    text = "Don't have an account? Click here to sign up.",
                    color = Color(0xFF2778D7),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { navHostController.navigate("SignUpStaffPage") }
                        .padding(10.dp)
                )
                Text(signInError ?: signInText)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpStaffPageView(
    navHostController: NavHostController,
    nameOfPersonUsingApp: String,
    updatedNameOfPersonUsingApp: (String) -> Unit,
    updatedUnconfirmedEmail: (String) -> Unit,
    updatedEmailVerified: (Boolean) -> Unit
){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signUpText by remember {mutableStateOf ("")}
    var signUpError by remember {mutableStateOf<String?>(null)}
    updatedEmailVerified(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign up (SSTaff)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sst_logo),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "Email address",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = email,
                onValueChange = {email = it},
                placeholder = {Text("your_name@sst.edu.sg")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Text(
                text = "Password",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = password,
                onValueChange = {password = it},
                placeholder= {Text("Enter your password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Text(
                text = "Name",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            TextField(
                value = nameOfPersonUsingApp,
                onValueChange = {updatedNameOfPersonUsingApp(it)},
                placeholder= {Text("Enter your name")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    if (email != "" && password != "") {
                        if (email.contains("@sst.edu.sg")) {
                            signUp(email, password, nameOfPersonUsingApp, updatedEmailVerified = updatedEmailVerified) { result, exception ->
                            if (result != null) {
                                signUpText = ""
                                updatedUnconfirmedEmail(email)
                                navHostController.navigate("VerifyEmailPage")
                            } else {
                                signUpError = exception?.message
                            } }
                        } else {
                            signUpText = "Invalid SSTaff email"
                        }
                    } else {
                        signUpText = "No email or password provided."
                    }
                }) {
                    Text("Sign Up")
                }
            }
            Text(signUpError ?: signUpText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailPageView(
    navHostController: NavHostController,
    userEmail: String,
    emailVerified: Boolean
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Verify email (SSTaff)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.sst_logo),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "In order to use a SSTaff account, you need to verify your email address.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            Text(
                text = "A verification link has been sent to $userEmail, please use it to verify your email address.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            Text(
                text = "Note: After verifying, it may take a while to process.",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
        }
    }
    when{emailVerified -> {
        navHostController.navigate("BottomNavBar")
    }}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePfpPageView(
    navHostController: NavHostController,
    imageUri: Uri,
    idOfPersonUsingApp: String
) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("profilePictures/${UUID.randomUUID()}.jpg")
    var downloadUrl by remember { mutableStateOf<String?>(null) }
    val db = Firebase.firestore

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {Text("Preview")},
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.navigate("BottomNavBar") }
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "")
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = rememberImagePainter(imageUri),
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp, 200.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Button(
                onClick = {
                    imageRef.putFile(imageUri)
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val url = uri.toString()
                                downloadUrl = url
                                val userDocumentRef = db.collection("users").document(idOfPersonUsingApp)
                                userDocumentRef.update("pfpUrl", url)
                                    .addOnSuccessListener {
                                        navHostController.popBackStack()
                                    }
                                    .addOnFailureListener { e ->
                                        e.printStackTrace()
                                    }
                            }
                        }
                          },
                modifier = Modifier
                    .padding(20.dp)
                    .height(60.dp)
                    .fillMaxWidth()
            ){
                Text("Save", fontSize = 20.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordPageView(
    idOfPersonUsingApp: String,
    navHostController: NavHostController
){
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    var userPassword by remember{ mutableStateOf<String?>("")}
    LaunchedEffect(idOfPersonUsingApp) {
        if (!idOfPersonUsingApp.isNullOrEmpty()) {
            usersCollection.document(idOfPersonUsingApp).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        userPassword = document.getString("password")
                    } else {
                        userPassword = ""
                    }
                }
                .addOnFailureListener { exception ->
                    userPassword = ""
                }
        } else {
            userPassword = ""
        }
    }
    var oldPasswordTextField by remember{mutableStateOf("")}
    var newPasswordTextField by remember{mutableStateOf("")}
    var confirmNewPasswordTextField by remember{mutableStateOf("")}
    var errorMessage by remember{mutableStateOf("")}
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(
                        onClick = {navHostController.popBackStack()}
                    ){
                        Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "")
                    }
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.sst_logo),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Enter old password",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = oldPasswordTextField,
                    onValueChange = {oldPasswordTextField = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    placeholder = {Text("Enter old password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Text(
                    text = "Enter new password",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = newPasswordTextField,
                    onValueChange = {newPasswordTextField = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    placeholder = {Text("Enter new password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Text(
                    text = "Confirm new password",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp
                )
                TextField(
                    value = confirmNewPasswordTextField,
                    onValueChange = {confirmNewPasswordTextField = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    placeholder = {Text("Enter new password again")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(
                        onClick = {
                            if (oldPasswordTextField == userPassword) {
                                if (confirmNewPasswordTextField == newPasswordTextField){
                                    user!!.updatePassword(newPasswordTextField)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(TAG, "User password updated.")
                                                usersCollection.document(idOfPersonUsingApp).update("password", newPasswordTextField)
                                                    .addOnSuccessListener {
                                                        navHostController.popBackStack()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        e.printStackTrace()
                                                    }
                                            }
                                        }
                                } else {
                                    errorMessage = "Passwords do not match"
                                }
                            } else {
                                errorMessage = "Incorrect password entered"
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
