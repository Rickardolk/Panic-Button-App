package com.example.panicbutton.view.screens

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.panicbutton.R
import com.example.panicbutton.view.components.LogOutUser
import com.example.panicbutton.view.components.ToggleSwitch
import com.example.panicbutton.view.components.UserHistory
import com.example.panicbutton.viewModel.DashboardUserViewModel
import com.example.panicbutton.viewModel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun DashboardUserScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dashboardUserViewModel: DashboardUserViewModel = viewModel(),
    userProfileViewModel: UserProfileViewModel = viewModel(),
    context: Context
) {

    val dataRecord by dashboardUserViewModel.userHistory.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName by dashboardUserViewModel.userName
    val houseNumber by dashboardUserViewModel.houseNumber
    val emptyProfile = R.drawable.ic_empty_profile
    val user by userProfileViewModel.userImage.collectAsState()
    val profileImageUrl = if (user?.imageProfile.isNullOrEmpty()) emptyProfile else user?.imageProfile

    val isLoading = userName.isEmpty() || houseNumber.isEmpty()


    LaunchedEffect(currentUser) {
        currentUser?.let {
            dashboardUserViewModel.fetchUserData(it.uid)
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            dashboardUserViewModel.fetchUserHistory()
            delay(1000)
        }
    }
    
    LaunchedEffect(houseNumber) {
        userProfileViewModel.fetchUserImage(houseNumber)
    }

    BackHandler {
        (context as? Activity)?.finish()
    }

    Box(
        modifier
            .background(color = colorResource(id = R.color.background))
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier
                .background(color = colorResource(id = R.color.primary))
                .height(180.dp)
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Panic Button",
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(start = 24.dp, top = 28.dp),
                text = "Selamat Datang",
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Column(
            modifier
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 140.dp)
                    .height(96.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    contentColor = Color.White,
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier
                        .padding(start = 22.dp, end = 22.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier
                                .wrapContentWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(36.dp)
                                    .border(
                                        width = 2.dp,
                                        color = colorResource(id = R.color.font),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .clickable { navController.navigate("user_profile") },
                                painter = rememberImagePainter(data = profileImageUrl),
                                contentDescription = "profile_image",
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = userName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(id = R.color.font)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Nomor rumah anda:",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.font2)
                            )
                            Text(
                                text = houseNumber,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.font2)
                            )
                        }
                    }
                    LogOutUser(
                        context = context,
                        navController = navController
                    )
                }
            }
            Column(
                modifier
                    .padding(horizontal = 24.dp)
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.primary),
                        RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_warning),
                        contentDescription = "ic_warning",
                        tint = colorResource(id = R.color.background)
                    )
                    Text(
                        text = "Peringatan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Text(
                    text = "Gunakan tombol hanya untuk keadaan darurat atau gangguan lainnya",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }

            ToggleSwitch(dashboardUserViewModel,context)

            Column(
                modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(start = 24.dp),
                    text = "Riwayat",
                    color = colorResource(id = R.color.font),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn { // data akan muncul di sini
                    items(dataRecord){ record->
                        UserHistory(
                            record = record
                        )
                    }
                }
            }
        }
    }
}
