package com.example.whatsthere.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.whatsthere.CAViewModel
import com.example.whatsthere.CommonDivider
import com.example.whatsthere.CommonImage
import com.example.whatsthere.CommonProgressSpinner
import com.example.whatsthere.DestinationScreen
import com.example.whatsthere.data.CatImage
import com.example.whatsthere.navigateTo
import kotlinx.coroutines.runBlocking

@Composable
fun ProfileScreen(navController: NavController, vm: CAViewModel) {

    val inProgress = vm.inProgress.value
    if (inProgress)
        CommonProgressSpinner()
    else {

        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var number by rememberSaveable { mutableStateOf(userData?.number ?: "") }

        val scrollState = rememberScrollState()
        val focus = LocalFocusManager.current

        val catImages = runBlocking { getCatImages() }

        Column {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onSave = {
                    focus.clearFocus(force = true)
                    vm.updateProfileData(name, number)
                },
                onBack = {
                    focus.clearFocus(force = true)
                    navigateTo(navController, DestinationScreen.ChatList.route)
                },
                onLogout = {
                    vm.onLogout()
                    navigateTo(navController, DestinationScreen.Login.route)
                }

            )

            LazyColumn {
                items(catImages) { catImage ->
                    CatImageItem(catImage)
                }
            }

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )

        }
    }

}

suspend fun getCatImages(): List<CatImage> {
    return CatApiClient.catApiService.getCatImages(limit = 1)
}
@Composable
fun CatImageItem(catImage: CatImage) {
    val painter = rememberImagePainter(data = catImage.url)

    Image(
        painter = painter,
        contentDescription = "Cat Image",
        modifier = Modifier.fillMaxWidth().height(200.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: CAViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {

    val imageUrl = vm.userData?.value?.imageUrl

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })

        }

        CommonDivider()

        ProfileImage(imageUrl, vm)

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name:", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Number:", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )

        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable { onLogout.invoke() })

        }


    }

}

@Composable
fun ProfileImage(imageUrl: String?, vm: CAViewModel) {
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        uri: Uri? ->
        uri?.let{
            vm.uploadProfileImage(uri)
        }
    }
    
    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {

                           launcher.launch("image/*")

                },
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Card(shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp))
            {
                CommonImage(data = imageUrl)
                
            }
            Text(text = "Change profile picture")

        }

        val isLoading = vm.inProgress.value
        if (isLoading) CommonProgressSpinner()

    }
}