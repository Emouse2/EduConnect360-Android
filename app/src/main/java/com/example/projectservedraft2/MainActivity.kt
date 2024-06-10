package com.example.projectservedraft2

import SampleViewModel
import android.hardware.biometrics.BiometricPrompt.AuthenticationResult
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectServeDraft2Theme {
                var SSTudent by remember{ mutableStateOf(false) }
                var nameOfPersonUsingApp by remember { mutableStateOf("Emouse 2") }
                val navController = rememberNavController()
                BottomNavBar(navController)
                NavHost(
                    navController = navController,
                    startDestination = "StartPage",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("StartPage") {
                        StartPageView(navController)
                    }
                    composable("SignInStudentPage") {
                        SignInStudentPageView(navController)
                    }
                    composable("SignUpStudentPage") {
                        SignUpStudentPageView(navController, nameOfPersonUsingApp){ newState -> nameOfPersonUsingApp = newState }
                    }
                    composable("GroupPage") {
                        GroupPageView(navController, nameOfPersonUsingApp)
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

data class StoredData(val StoredData:String)
fun signIn(
    email: String,
    password: String,
    onResult:(AuthResult?, Exception?) -> Unit
) {
    Firebase.auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener{task ->
            onResult(if (task.isSuccessful) task.result else null, task.exception)
        }
}

fun signUp(
    email: String,
    password: String,
    onResult:(AuthResult?, Exception?) -> Unit
) {
    Firebase.auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener{task ->
            onResult(if (task.isSuccessful) task.result else null, task.exception)
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
    var item1IndicatorColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var item2IndicatorColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
    var item3IndicatorColor by remember { mutableStateOf(Color(0xFFFFFFFF)) }
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
                                item1IndicatorColor = Color(0xFFDDE2F9)
                            } else {
                                item1IconColor = Color(0xFF000000)
                                item1IndicatorColor = Color(0xFFf1edf7)
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
                                    Text(
                                        text = "Groups",
                                        color = item1IconColor
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = item1IndicatorColor
                        )
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
                                item2IndicatorColor = Color(0xFFDDE2F9)
                            } else {
                                item2IconColor = Color(0xFF000000)
                                item2IndicatorColor = Color(0xFFf1edf7)
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
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = item2IndicatorColor
                        )
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
                                item3IndicatorColor = Color(0xFFDDE2F9)
                            } else {
                                item3IconColor = Color(0xFF000000)
                                item3IndicatorColor = Color(0xFFf1edf7)
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
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = item3IndicatorColor
                        )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupPageView(navHostController: NavHostController, nameOfPersonUsingApp: String) {
    val currentTime = LocalTime.now()
    val greeting = remember { mutableStateOf(getGreetingMessage(currentTime)) }
    var groupsList by remember{ mutableStateOf(listOf<String>()) }

    // Recalculate greeting when the time changes
    LaunchedEffect(currentTime) {
        greeting.value = getGreetingMessage(currentTime)
    }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.sst_hexagon_logo),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.offset(x = -30.dp, y = -40.dp)
        )
    }
    Column (modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = greeting.value,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = "$nameOfPersonUsingApp",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.offset(y = -10.dp)
        )
        Text(
            text = "Groups",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.offset(y = -10.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "S2-01",
                modifier = Modifier.padding(20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.weight(1.0f))
        }
    }
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

@Composable
fun StartPageView(navHostController: NavHostController){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
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
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
            Row{
                Text(
                    text = "I am a...",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1.0f))
            }
            Button(
                onClick = {navHostController.navigate("")},
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900))
            ) {
                Text(
                    text = "SSTaff",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = ""
                )
            }
            Button(
                onClick = {navHostController.navigate("SignInStudentPage")},
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900))
            ) {
                Text(
                    text = "SSTudent",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Icon(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = ""
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInStudentPageView(navHostController: NavHostController){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signInText by remember {mutableStateOf("")}
    var signInError by remember {mutableStateOf<String?>(null)}
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign in (SSTudent")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.navigate("StartPage")}) {
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
                            signIn(email, password) { result, exception ->
                                if (result != null) {
                                    signInText = "Success! Going to home page..."
                                    navHostController.navigate("GroupPage")
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
                    color = Color.Blue,
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                        Text("Sign up (SSTudent)")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.navigate("SignInStudentPage")}) {
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
                        signUp(email, password) { result, exception ->
                            if (result != null) {
                                signUpText = "Success! Going to home page..."
                                navHostController.navigate("GroupPage")
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
fun SignInStaffPageView(navHostController: NavHostController){
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
                    IconButton(onClick = {navHostController.navigate("StartPage")}) {
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
                            signIn(email, password) { result, exception ->
                                if (result != null) {
                                    signInText = "Success! Going to home page..."
                                    navHostController.navigate("GroupPage")
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
                    color = Color.Blue,
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
fun SignUpStaffPageView(navHostController: NavHostController){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable {mutableStateOf("") }
    var signUpText by remember {mutableStateOf ("")}
    var signUpError by remember {mutableStateOf<String?>(null)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Sign up (SSTaff")
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.navigate("SignInStudentPage")}) {
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
                value = password,
                onValueChange = {password = it},
                placeholder= {Text("Enter your name")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    if (email != "" && password != "") {
                        signIn(email, password) { result, exception ->
                            if (result != null) {
                                signUpText = "Success! Going to home page..."
                                navHostController.navigate("GroupPage")
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
