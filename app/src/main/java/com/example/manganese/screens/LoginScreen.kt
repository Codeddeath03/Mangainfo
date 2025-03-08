package com.example.manganese.screens


import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manganese.R
import com.example.manganese.components.HeaderText
import com.example.manganese.components.LoginTextField


@Composable
fun LoginScreen(
    onClickLogin:(String,String)->Unit,
    onSignUpClick:()->Unit
) {
    val(userName,setUsername) = rememberSaveable  {
        mutableStateOf("")
    }
    val(password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val(check,setCheck) = rememberSaveable {
        mutableStateOf(false)
    }
    val isFieldsEntered = userName.isNotBlank() && password.isNotBlank()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        HeaderText("Login", modifier = Modifier)
        Spacer(modifier = Modifier.height(50.dp))
      //  Text("Username")
        LoginTextField(
            value = userName,
            onValueChange = setUsername,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(checked = check, onCheckedChange = setCheck)
                Text("Remember me")
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onClickLogin(userName, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled= isFieldsEntered

        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        moreLoginOptions(
            onIconClick = {index ->
                when(index){
                    0->{
                        //github login
                        Toast.makeText(context,"Github Login",Toast.LENGTH_SHORT).show()
                    }
                    1->{
                        Toast.makeText(context,"Google Login",Toast.LENGTH_SHORT).show()
                    }

                }

            },
            onSignUpClick = onSignUpClick,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }

}



@Composable
fun moreLoginOptions(
    onIconClick:(Index:Int)-> Unit,
    onSignUpClick:()->Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Or Sign in with", fontSize = 18.sp)
        val iconList = listOf(
            R.drawable.icons_github,
            R.drawable.icon_google,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            iconList.forEachIndexed { index, i ->
                Icon(
                    painter = painterResource(id = i),
                    contentDescription = null,
                    modifier=Modifier
                        .size(43.dp)
                        .clickable {
                            onIconClick(index)
                        }
                        .clip(CircleShape)
                )

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Don't have an Account?",fontSize = 18.sp)
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up",fontSize = 18.sp)
            }
        }


    }
}