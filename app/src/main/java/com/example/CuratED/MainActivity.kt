package com.example.CuratED

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.CuratED.Authentication.SignInStaffPageView
import com.example.CuratED.Authentication.SignInStudentPageView
import com.example.CuratED.Authentication.SignUpStaffPageView
import com.example.CuratED.Authentication.SignUpStudentPageView
import com.example.CuratED.BackEnd.AppDataClass
import com.example.CuratED.BackEnd.OtherData.getGreetingMessage
import com.example.CuratED.BackEnd.ViewModels.AuthViewModel
import com.example.CuratED.Views.Authentication.VerifyEmailPageView
import com.example.CuratED.Views.BottomNavBar.BottomNavBar
import com.example.CuratED.Views.BottomNavBar.GroupPage.CreateNewGroupPageView
import com.example.CuratED.Views.BottomNavBar.GroupPage.GroupInsideView
import com.example.CuratED.Views.BottomNavBar.SettingsPage.ChangePasswordPageView
import com.example.CuratED.Views.BottomNavBar.SettingsPage.ChangePfpPageView
import com.example.CuratED.Views.MiniViews.AlertDialog1
import com.example.CuratED.Views.OtherViews.ImageLargeView
import com.example.CuratED.ui.theme.ProjectServeDraft2Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                var SSTudent by remember { mutableStateOf(false) }
                var unconfirmedEmail by remember {mutableStateOf("")}
                var nameOfPersonUsingApp by remember { mutableStateOf("") }
                var idOfPersonUsingApp by remember { mutableStateOf("") }
                val navController = rememberNavController()
                var emailVerified by remember {mutableStateOf(false)}
                if (SSTudent == true) {
                    emailVerified = true
                }

                val context = LocalContext.current
                val savedAppThemeStringFlow = AppDataClass.readString(context, "savedAppTheme").collectAsState(initial = "")
                val savedAppTheme by savedAppThemeStringFlow
                if (savedAppTheme == "" || savedAppTheme == "System default") {
                    isDarkTheme = isSystemInDarkThemeBoolean
                } else { if(savedAppTheme == "Light") {
                    isDarkTheme = false
                } else{
                    isDarkTheme = true
                }}

                var imageEnlargedCurrently by remember{mutableStateOf("")}
                var groupSeeingCurrently by remember{mutableStateOf("")}

                NavHost(
                    navController = navController,
                    startDestination = "StartPage",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(
                        route = "StartPage",
                        enterTransition = { fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) }
                    ) {
                        StartPageView(
                            navController,
                            context,
                            updatedSSTudent = { newState -> SSTudent = newState },
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState },
                            updatedIdOfPersonUsingApp = { newState -> idOfPersonUsingApp = newState }
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
                            context,
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
                            context,
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState },
                            updatedIdOfPersonUsingApp = {newState -> idOfPersonUsingApp = newState}
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
                            context,
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
                            context,
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState },
                            updatedUnconfirmedEmail = {newState -> unconfirmedEmail = newState},
                            updatedEmailVerified = {newState -> emailVerified = newState},
                            updatedIdOfPersonUsingApp = {newState -> idOfPersonUsingApp = newState}
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
                            context,
                            savedAppTheme,
                            updatedIsDarkTheme = { newState -> isDarkTheme = newState },
                            updatedNameOfPersonUsingApp = { newState -> nameOfPersonUsingApp = newState },
                            updatedSSTudent = {newState -> SSTudent = newState},
                            updatedImageEnlargedCurrently = {newState -> imageEnlargedCurrently = newState},
                            updatedGroupSeeingCurrently = {newState -> groupSeeingCurrently = newState}
                        )
                    }
                    composable(
                        route = "GroupInsidePage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        GroupInsideView(
                            navController,
                            groupSeeingCurrently,
                            updatedImageEnlargedCurrently = {newState -> imageEnlargedCurrently = newState},
                            SSTudent,
                            idOfPersonUsingApp
                        )
                    }
                    composable(
                        route = "CreateNewGroupPage",
                        enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500)) },
                        exitTransition = { fadeOut(tween(500)) },
                        popEnterTransition = { fadeIn(tween(500)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500)) }
                    ) {
                        CreateNewGroupPageView(navController)
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
                    composable(
                        route = "ImageLarge",
                        enterTransition = { fadeIn(tween(500)) },
                        exitTransition = { fadeOut(tween(500)) }
                    ){
                        ImageLargeView(
                            imageEnlargedCurrently,
                            navController,
                            context
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StartPageView(
    navHostController: NavHostController,
    context: Context,
    updatedSSTudent: (Boolean) -> Unit,
    updatedNameOfPersonUsingApp: (String) -> Unit,
    updatedIdOfPersonUsingApp: (String) -> Unit
){
    val snackbarHostState = remember{ SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val loggedInBooleanFlow = AppDataClass.readBoolean(context, "loggedIn").collectAsState(initial = false)
    val loggedIn by loggedInBooleanFlow
    val SSTudentFlow = AppDataClass.readBoolean(context, "loggedI").collectAsState(initial = false)
    val SSTudent by SSTudentFlow
    val savedEmailStringFlow = AppDataClass.readString(context, "email").collectAsState(initial = "")
    val savedEmail by savedEmailStringFlow
    val savedPasswordStringFlow = AppDataClass.readString(context, "password").collectAsState(initial = "")
    val savedPassword by savedPasswordStringFlow
    Scaffold (
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ){ innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)){}
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
    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            Log.d("printed stuff", "logged in is true")
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Logging in..."
                )
            }

            if (savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {
                AuthViewModel.signIn(savedEmail, savedPassword) { result, name, userId, exception ->
                    if (result != null) {
                        updatedNameOfPersonUsingApp(name)
                        updatedIdOfPersonUsingApp(userId)
                        scope.launch {
                            updatedSSTudent(SSTudent)
                            AppDataClass.storeBoolean(context, true, "loggedIn")
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                        navHostController.navigate("BottomNavBar")
                    } else {
                        scope.launch {
                            AppDataClass.storeBoolean(context, false, "loggedIn")
                            snackbarHostState.showSnackbar(
                                message = "Login failed",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        } else {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }
}