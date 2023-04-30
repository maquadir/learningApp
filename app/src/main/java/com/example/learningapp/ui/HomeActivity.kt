package com.example.learningapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.learningapp.R
import com.example.learningapp.clearPreferences
import com.example.learningapp.clearTokenPreferences
import com.example.learningapp.model.Student
import com.example.learningapp.network.ApiService
import com.example.learningapp.repository.Repository
import com.example.learningapp.setChildLoggedIn
import com.example.learningapp.viewmodel.HomeViewModel
import com.example.learningapp.viewmodel.MainViewModelFactory
import com.example.learningapp.viewmodel.User

class HomeActivity : AppCompatActivity() {

    private val apiService = ApiService.instance
    private val repository = Repository(apiService)
    private val viewModel: HomeViewModel by viewModels {
        MainViewModelFactory(repository)
    }

    var title = ""
    private var isListVisible = true
    private var studentList = emptyList<String>()
    var user = User.PARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            //get data from bundle
            val bundle = intent.extras
            if (bundle != null) {
                user = bundle.getSerializable("user") as User

                when (user) {
                    User.PARENT -> {

                        title = getString(R.string.signedin_as_parent)

                        //retrieve list of student ids and display as list
                        studentList =
                            bundle.getStringArrayList("students") ?: emptyList<String>()
                        isListVisible = true
                    }

                    User.STUDENT -> {
                        title = getString(R.string.signedin_as_student)
                        isListVisible = false
                    }
                }
            }

            //display UI
            HomeScreen(title, isListVisible, studentList)
        }

        //save child data
        viewModel.state.observe(this) { it ->
            saveData(it)
        }

    }

    private fun saveData(user: Student) {
        setChildLoggedIn(user.user_id, user.auth_token)
    }

    //setup UI
    @Composable
    fun HomeScreen(title: String, isListVisible: Boolean, studentList: List<String>) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = title, color = Color.Gray)
            StudentList(isListVisible, studentList)
            LogOutButton()
        }
    }

    @Composable
    fun LogOutButton(
    ) {
        Button(
            onClick = {
                when (user) {
                    User.PARENT -> {
                        clearTokenPreferences("TOKEN", "parent_token")
                    }

                    User.STUDENT -> {
                        clearTokenPreferences("TOKEN", "student_token")
                        clearPreferences("LOGGED_IN_CHILD")
                    }
                }
                finish()
            }
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                color = Color.White
            )
        }
    }

    //setup student id list
    @Composable
    fun StudentList(isListVisible: Boolean, studentList: List<String>) {
        //display list of student ids only for parent
        if (isListVisible) {
            val students = remember { studentList }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    students
                ) {
                    Student(studentId = it)
                }
            }
        }
    }

    //setup student id
    @Composable
    fun Student(studentId: String) {
        val isLoginEnabled = remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
            Text(text = studentId, color = Color.Gray)
            Switch(checked = isLoginEnabled.value, onCheckedChange = {
                isLoginEnabled.value = it
                if (isLoginEnabled.value) viewModel.parentChildLogin(studentId)
                else clearPreferences("LOGGED_IN_CHILD")
            })
        }
    }
}