package com.example.panicbutton.view.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.panicbutton.R
import com.example.panicbutton.viewModel.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier : Modifier = Modifier,
    navController: NavHostController,
    registerViewModel: RegisterViewModel = viewModel(),
    context: Context,

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember {mutableStateOf("")}
    var houseNumber by remember {mutableStateOf("")}

    Column(
        modifier
            .background(color = colorResource(id = R.color.primary))
            .fillMaxSize()
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 60.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "ic_logo",
                modifier = Modifier.size(160.dp)
            )
        }
        Box(
            modifier
                .background(color = colorResource(id = R.color.primary))
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier
                    .height(600.dp)
                    .background(
                        color = Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp)
            ){
                Column(
                    modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.font)
                    )
                    Spacer(modifier = Modifier.height(44.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {name = it},
                        label = { Text(text = "Nama") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "ic person",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.font),
                            focusedLabelColor = colorResource(id = R.color.font),
                            focusedLeadingIconColor = colorResource(id = R.color.font),
                            unfocusedLeadingIconColor = colorResource(id = R.color.defauld),
                            cursorColor = colorResource(id = R.color.font)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = houseNumber,
                        onValueChange = {houseNumber = it},
                        label = { Text(text = "Nomor Rumah") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "ic houseNumber",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.font),
                            focusedLabelColor = colorResource(id = R.color.font),
                            focusedLeadingIconColor = colorResource(id = R.color.font),
                            unfocusedLeadingIconColor = colorResource(id = R.color.defauld),
                            cursorColor = colorResource(id = R.color.font)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {email = it},
                        label = { Text(text = "Email") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "ic home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.font),
                            focusedLabelColor = colorResource(id = R.color.font),
                            focusedLeadingIconColor = colorResource(id = R.color.font),
                            unfocusedLeadingIconColor = colorResource(id = R.color.defauld),
                            cursorColor = colorResource(id = R.color.font)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {password = it},
                        label = { Text(text = " Password") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "ic home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.font),
                            focusedLabelColor = colorResource(id = R.color.font),
                            focusedLeadingIconColor = colorResource(id = R.color.font),
                            unfocusedLeadingIconColor = colorResource(id = R.color.defauld),
                            cursorColor = colorResource(id = R.color.font)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            registerViewModel.register(name, houseNumber, email, password, navController, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                        ,
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.font),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Sign Up")
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Sudah memiliki akun?"
                        )
                        Text(
                            modifier = Modifier
                                .clickable { navController.navigate("login") },
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.font)
                        )
                    }
                }
            }
        }
    }
}