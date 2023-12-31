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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        //!
        var catImages by remember { mutableStateOf<List<CatImage>>(runBlocking { getCatImages() }) }


        val scrollState = rememberScrollState()
        val focus = LocalFocusManager.current


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
                },
                onNewCatClick = {
                    runBlocking {
                        val newCatImages = getCatImages()
                        catImages = newCatImages
                    }
                },
                catImages = catImages

            )

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )

        }
    }

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
    onLogout: () -> Unit,
    onNewCatClick: () -> Unit,
    catImages: List<CatImage>
) {

    val imageUrl = vm.userData?.value?.imageUrl
//    val catImages = runBlocking { getCatImages() }

//    fun getCatImage(): CatImage {
//        val catImages = runBlocking { getCatImages() }
//        return catImages[0]
//    }

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

            Text(text = "Wstecz", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Zapis", modifier = Modifier.clickable { onSave.invoke() })

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
            Text(text = "Imie:", modifier = Modifier.width(100.dp))
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

            Text(text = "Numer:", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent
                )
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Wyloguj się", modifier = Modifier.clickable { onLogout.invoke() })

        }

        CommonDivider()

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Totalnie darmowy kotek!",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )
            CommonDivider()
            CatImageItem(catImage = catImages[0])
            Button(
                onClick = { onNewCatClick()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = "Miaaał", color = Color.Black)
            }
        }

    }

}

@Composable
fun ProfileImage(imageUrl: String?, vm: CAViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
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

            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
            {
                CommonImage(data = imageUrl)

            }
            Text(text = "Zmień zdjęcie profilowe")

        }

        val isLoading = vm.inProgress.value
        if (isLoading) CommonProgressSpinner()

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
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(300.dp),
    )
}
