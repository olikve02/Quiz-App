package com.example.quizapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quizapp.data.DataSource
import com.example.quizapp.model.QuizImage
import com.example.quizapp.ui.theme.QuizAppTheme
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.draw.paint
import com.example.quizapp.model.QuizImageStore

class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GalleryLayout(modifier =Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun GalleryLayout(modifier : Modifier = Modifier) {


    //Load initial images
    if(QuizImageStore.items.isEmpty()){
        QuizImageStore.add(QuizImage(R.drawable.gigachad,"Giga Chad"))
        QuizImageStore.add(QuizImage(R.drawable.slay,"Slay"))
        QuizImageStore.add(QuizImage(R.drawable.gutta, "Vibes"))
    }

    var imageToRemove by remember { mutableStateOf<QuizImage?>(null) }
    var showRemoveImageDialog by remember { mutableStateOf(false) }
    var showAddImageDialog by remember {mutableStateOf(false ) }
    var newImageName by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf<Uri?>(null) }


    //Dialogs
    if(showRemoveImageDialog){
        AlertDialog(
            title = { Text(text = "Remove Image") },
            onDismissRequest = {},
            confirmButton = {
               Button(onClick = {
                   QuizImageStore.remove(imageToRemove!!)
                   showRemoveImageDialog = false
               }) {
                   Text("Yes")
               }
            },
            dismissButton = {Button(onClick = {
                showRemoveImageDialog = false
            }) {
                Text("No")
            }}
        )
    }

    if(showAddImageDialog){
        AlertDialog(
            title = { TextField(
                value = newImageName,
                onValueChange = { newImageName = it },
                label = { Text("New Image Name") }
            ) },
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    showAddImageDialog = false
                    QuizImageStore.add(QuizImage(name = newImageName, imageUri = newImageUri.toString()))

                }){
                    Text("Add")
                }
            },
        )
    }

    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {

            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            newImageUri = uri
            showAddImageDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GalleryGrid(
            onAddImage = {},
            onRemoveImage = { clickedImage ->
                showRemoveImageDialog = true
                imageToRemove = clickedImage
            },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(1f))

        ButtonRow(
            modifier,
            {
                picker.launch(arrayOf("image/*"))
        })
    }
}
@Composable
fun GalleryGrid(modifier: Modifier = Modifier, onAddImage: () -> Unit, onRemoveImage: (clickedImage: QuizImage) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items (QuizImageStore.items) { image ->
            QuizImageItem(image, onImageClick = { onRemoveImage(image) })
        }
    }
}


@Composable
fun QuizImageItem(quizImage: QuizImage, modifier: Modifier = Modifier, onImageClick: () -> Unit){

        Column(
            modifier = modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .clickable(onClick = onImageClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                quizImage.imageRes != null -> {
                    Image(
                        painter = painterResource(quizImage.imageRes),
                        contentDescription = null,
                        modifier
                            .size(100.dp)
                            .padding(top = 12.dp)
                    )
                }

                quizImage.imageUri != null -> {
                    AsyncImage(
                        model = quizImage.imageUri.let(Uri::parse),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(12.dp)
                    )
                }
            }

            Text(
                text = quizImage.name,
            )
        }
    }

@Composable
fun ButtonRow(modifier: Modifier = Modifier, onAddImage: () -> Unit){
    Row(
        modifier = Modifier.padding(12.dp)
        ,) {
        Button(
            onClick = onAddImage,
            modifier = Modifier
                .width(150.dp)
        ) {
            Text("Add Photo")
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppTheme {
        GalleryLayout()

    }
}