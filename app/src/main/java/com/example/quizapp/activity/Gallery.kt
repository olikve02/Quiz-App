package com.example.quizapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quizapp.model.QuizImage
import com.example.quizapp.ui.theme.QuizAppTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.testTag
import com.example.quizapp.data.AppDatabase
import com.example.quizapp.data.QuizImageDao
import kotlinx.coroutines.launch


class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.quizImageDao()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GalleryLayout(modifier =Modifier.padding(innerPadding), dao)
                }
            }
        }
    }
}
@Composable
fun GalleryLayout(modifier : Modifier = Modifier, dao: QuizImageDao) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var imageToRemove by remember { mutableStateOf<QuizImage?>(null) }
    var showRemoveImageDialog by remember { mutableStateOf(false) }
    var showAddImageDialog by remember {mutableStateOf(false ) }
    var newImageName by remember { mutableStateOf("") }
    var newImageUri by remember { mutableStateOf<Uri?>(null) }
    var images by remember { mutableStateOf<List<QuizImage>>(emptyList<QuizImage>()) }


    //Load initial images and convert drawable to uri
    LaunchedEffect(Unit){

        images = dao.getAll()
    }



    //Dialogs
    if(showRemoveImageDialog){
        AlertDialog(
            title = { Text(text = "Remove Image") },
            onDismissRequest = {},
            confirmButton = {
               Button(
                   onClick = {
                   scope.launch {
                       dao.delete(imageToRemove!!)
                       showRemoveImageDialog = false
                       images = dao.getAll()
                   }
               },
                   modifier = Modifier.testTag("confirm_delete_button")
                   ) {
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
                label = { Text("New Image Name") },
                modifier = Modifier.testTag("name_input")
            ) },
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        showAddImageDialog = false
                        dao.insert(QuizImage(name = newImageName, imageUri = newImageUri.toString()))
                        images = dao.getAll()
                    }
                }, modifier = Modifier.testTag("confirm_add_button")){
                    Text("Add")
                }
            },
        )
    }



    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            }

            newImageUri = uri
            showAddImageDialog = true
        }
    }
    //Layout
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Count: ${images.size}",
            modifier = Modifier.testTag("image_count")
        )
        GalleryGrid(
            onAddImage = {},
            onRemoveImage = { clickedImage ->
                showRemoveImageDialog = true
                imageToRemove = clickedImage
            },
            modifier = Modifier,
            images = images
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
fun GalleryGrid(modifier: Modifier = Modifier, onAddImage: () -> Unit, onRemoveImage: (clickedImage: QuizImage) -> Unit, images: List<QuizImage>) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items (images) { image ->
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
                .clickable(onClick = onImageClick)
                .testTag("image_item_${quizImage.name}"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



                    AsyncImage(
                        model = quizImage.imageUri.let(Uri::parse),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(12.dp)
                    )



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
                .testTag("add_photo_button")
        ) {
            Text("Add Photo")
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppTheme {
       // GalleryLayout()

    }
}