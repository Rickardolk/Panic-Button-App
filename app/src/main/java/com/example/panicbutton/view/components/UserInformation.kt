package com.example.panicbutton.view.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.panicbutton.R
import com.example.panicbutton.viewModel.DashboardUserViewModel
import com.example.panicbutton.viewModel.DetailRekapViewModel
import com.example.panicbutton.viewModel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun UserInformation(
    modifier: Modifier = Modifier,
    dashboardUserViewModel: DashboardUserViewModel = viewModel(),
    userProfileViewModel: UserProfileViewModel = viewModel(),
    context: Context
) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val houseNumber by dashboardUserViewModel.houseNumber
    val userData by userProfileViewModel.userData.collectAsState(emptyMap())
    val phoneNumber = userData["phoneNumber"] ?: ""
    val note = userData["note"] ?: ""

    LaunchedEffect(currentUser) {
        currentUser?.let {
            dashboardUserViewModel.fetchUserData(it.uid)
            userProfileViewModel.fetchUserData(it.uid)
        }
    }

    Surface(
        modifier
            .padding(horizontal = 24.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    value = phoneNumber,
                    onValueChange = { userProfileViewModel.updatePhoneNumber(it)},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phonenumber),
                            contentDescription = "ic_phonenumber",
                            tint = colorResource(id = R.color.font2)
                        )
                    },
                    placeholder = {
                        Text(
                            text = phoneNumber.ifEmpty { "Masukan No Hp Anda" },
                            fontSize = 13.sp
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.font),
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(id = R.color.font3),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    value = note,
                    onValueChange = {userProfileViewModel.updateNote(it)},
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_note),
                            contentDescription = "ic_note",
                            tint = colorResource(id = R.color.font2)
                        )
                    },
                    placeholder = {
                        Text(
                            text = note.ifEmpty { "Masukan keterangan tentang rumah anda" },
                            fontSize = 13.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.font),
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                )
            }
        }
    }

    Button(
        onClick = {
            userProfileViewModel.savePhoneNumberAndNote(houseNumber,phoneNumber, note, context)
        },
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.background_button_userInformationScreen)
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 24.dp)
    ) {
        Text(
            text = "Simpan",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun UserInformationForAdmin(
    modifier: Modifier = Modifier,
    detailRekapViewModel: DetailRekapViewModel = viewModel(),
    houseNumber: String
) {
    val userData by detailRekapViewModel.userData.collectAsState()
    val phoneNumber = userData["phoneNumber"] ?: ""
    val note = userData["note"] ?: ""

    LaunchedEffect(houseNumber) {
        while (true) {
            detailRekapViewModel.fetchPhoneNumberAndNote(houseNumber)
            delay(2000)
        }
    }

    Surface(
        modifier
            .padding(horizontal = 24.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    value = phoneNumber,
                    onValueChange = { },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phonenumber),
                            contentDescription = "ic_phonenumber",
                            tint = colorResource(id = R.color.font2)
                        )
                    },
                    placeholder = {
                        Text(
                            text = phoneNumber.ifEmpty { "Nomor Telepon pengguna" },
                            fontSize = 13.sp
                        )
                    },
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(id = R.color.font3),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    value = note,
                    onValueChange = { },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_note),
                            contentDescription = "ic_note",
                            tint = colorResource(id = R.color.font2)
                        )
                    },
                    placeholder = {
                        Text(
                            text = note.ifEmpty { "Keterangan rumah" },
                            fontSize = 13.sp
                        )
                    },
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    )
                )
            }
        }
    }
}


