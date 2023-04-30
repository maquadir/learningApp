package com.example.learningapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.learningapp.R
import com.example.learningapp.getLoggedInChildData
import com.example.learningapp.model.BodyRequest
import com.example.learningapp.model.Parent
import com.example.learningapp.model.Student
import com.example.learningapp.network.ApiService
import com.example.learningapp.repository.Repository
import com.example.learningapp.setToken
import com.example.learningapp.viewmodel.MainViewModel
import com.example.learningapp.viewmodel.MainViewModelFactory
import com.example.learningapp.viewmodel.User

class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.instance
    private val repository = Repository(apiService)
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }

        //display Home Screen
        viewModel.state.observe(this) { it ->
            displayHomeScreen(it)
        }
    }

    private fun displayHomeScreen(user: Any) {

        val bundle = Bundle()
        val intent = Intent(this, HomeActivity::class.java)

        when (user) {
            is Parent -> {

                //save token of parent
                setToken("parent_token", user.auth_token)

                //send list of student ids
                bundle.putStringArrayList("students", ArrayList(user.student_ids))
                bundle.putSerializable("user", User.PARENT)
            }

            is Student -> {

                //save token of student
                setToken("student_token", user.auth_token)

                bundle.putSerializable("user", User.STUDENT)

            }
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    //setup UI
    @Composable
    fun MainScreen() {
        val usernameValue = remember { mutableStateOf("example-parent") }
        val passwordValue = remember { mutableStateOf("1234") }
        val isFieldsVisible = remember { mutableStateOf(true) }
        val isStudent = remember { mutableStateOf(false) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            SwitchHeader(usernameValue, passwordValue, isFieldsVisible, isStudent)
            UserCredentials(usernameValue, passwordValue, isFieldsVisible)
            SignInButton(usernameValue, passwordValue, isFieldsVisible, isStudent)
        }
    }

    //switch between parent and student
    @Composable
    fun SwitchHeader(
        usernameValue: MutableState<String>,
        passwordValue: MutableState<String>,
        isFieldsVisible: MutableState<Boolean>,
        isStudent: MutableState<Boolean>
    ) {
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.size(200.dp, 30.dp)
        ) {
            Text(
                text = stringResource(R.string.parent),
                color = androidx.compose.ui.graphics.Color.Gray
            )
            Switch(checked = isStudent.value, onCheckedChange = {
                isStudent.value = it

                //set credentials
                usernameValue.value =
                    getString(if (isStudent.value) R.string.student_username else R.string.parent_username)
                passwordValue.value =
                    getString(if (isStudent.value) R.string.student_password else R.string.parent_password)

                //allow child to login without username and password if parent has logged in the child
                //here we will hide the username and password text fields by setting a boolean
                when {
                    isStudent.value -> {
                        val childData = context.getLoggedInChildData()
                        if (childData.studentId.isNotEmpty() && childData.authToken.isNotEmpty()) {
                            isFieldsVisible.value = false
                        }
                    }

                    else -> {
                        isFieldsVisible.value = true
                    }
                }
            })
            Text(
                text = stringResource(id = R.string.student),
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }

    //setup text fields for user input
    @Composable
    fun UserCredentials(
        usernameValue: MutableState<String>,
        passwordValue: MutableState<String>,
        isFieldsVisible: MutableState<Boolean>
    ) {
        if (isFieldsVisible.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.size(height = 200.dp, width = 200.dp)
            ) {
                UserNameTextField(usernameValue)
                PasswordTextField(passwordValue)
            }
        }
    }

    //setup username textfield
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserNameTextField(usernameValue: MutableState<String>) {
        TextField(
            value = usernameValue.value,
            onValueChange = {
                usernameValue.value = it
            },
            label = {
                Text(text = stringResource(id = R.string.enter_username))
            }
        )
    }

    //setup password textfield
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PasswordTextField(passwordValue: MutableState<String>) {

        TextField(
            value = passwordValue.value,
            onValueChange = {
                passwordValue.value = it
            },
            label = {
                Text(text = stringResource(id = R.string.enter_password))
            }
        )
    }

    //setup log in button
    @Composable
    fun SignInButton(
        usernameValue: MutableState<String>,
        passwordValue: MutableState<String>,
        isFieldsVisible: MutableState<Boolean>,
        isStudent: MutableState<Boolean>
    ) {
        val context = LocalContext.current
        Button(
            onClick = {
                val childData = context.getLoggedInChildData()
                val body: BodyRequest =
                    BodyRequest(
                        usernameValue.value,
                        passwordValue.value
                    )
                if (isFieldsVisible.value)
                    viewModel.login(isStudent.value, body)
                else
                    viewModel.childLoginUsingToken(childData.studentId, childData.authToken)

            }
        ) {
            Text(
                text = stringResource(id = if (isFieldsVisible.value) R.string.sign_in else R.string.signed_in),
                color = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}

