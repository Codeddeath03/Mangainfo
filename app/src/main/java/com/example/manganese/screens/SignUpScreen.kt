package com.example.manganese.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manganese.R
import com.example.manganese.components.HeaderText
import com.example.manganese.components.LoginTextField

@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit,
    onSignUpClick:  (String, String, String)->Unit
) {
    val(userName,setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val(userEmail,setEmail) = rememberSaveable {
        mutableStateOf("")
    }
    val(password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val(confirm_password,setconfirm_password) = rememberSaveable {
        mutableStateOf("")
    }

    val isFieldsEntered = userName.isNotBlank() && password.isNotBlank() && confirm_password.isNotBlank() && userEmail.isNotBlank()
    var checkPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        HeaderText("Sign Up", modifier = Modifier, fontSize = 40)
        Spacer(modifier = Modifier.height(50.dp))
        //  Text("Username")
        LoginTextField(
            value = userName,
            onValueChange = setUsername,
            fieldName = "Username",
            leadingIcon= Icons.Default.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            value = userEmail,
            onValueChange = setEmail,
            fieldName = "Email",
            leadingIcon= Icons.Default.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            fieldName = "Password",
            leadingIcon= Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoginTextField(
            value = confirm_password,
            onValueChange = setconfirm_password,
            fieldName = "Confirm Password",
            leadingIcon= Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(checkPassword) {
            Text("Password does not match")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                checkPassword= password != confirm_password
                if(!checkPassword){
                    onSignUpClick(userEmail,password,userName)
                }
           // Toast.makeText(context,"Sign Up Successful", Toast.LENGTH_SHORT).show()
        },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsEntered
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LoginOptions(
            onIconClick = {index ->
                when(index){
                    0->{
                        //github login
                        Toast.makeText(context,"Github Login", Toast.LENGTH_SHORT).show()
                    }
                    1->{
                        Toast.makeText(context,"Google Login", Toast.LENGTH_SHORT).show()
                    }

                }

            },
            onSignInClick = onSignInClick,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }

}



@Composable
fun LoginOptions(
    onIconClick:(Index:Int)-> Unit,
    onSignInClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Already have an Account?",fontSize = 18.sp)
        TextButton(onClick = onSignInClick) {
            Text("Sign In",fontSize = 18.sp)
        }
    }
}

@Preview
@Composable
private fun PrevSignUpOptions() {
    SignUpScreen(
        onSignInClick = {"boop"},
        onSignUpClick = {su,s,z -> "kazak"}
    )

}