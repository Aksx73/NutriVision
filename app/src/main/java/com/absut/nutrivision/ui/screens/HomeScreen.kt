package com.absut.nutrivision.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.absut.nutrivision.R
import com.absut.nutrivision.model.NutritionRecord
import com.absut.nutrivision.ui.MainViewModel
import com.absut.nutrivision.ui.component.EmptyState
import com.absut.nutrivision.ui.theme.NutriVisionTheme
import io.ktor.util.sha1
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onImageCaptured: (Bitmap?, String?) -> Unit,
    onRecordClick: (NutritionRecord) -> Unit = {},
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    var tempFileUrl by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { imageTaken ->
            if (imageTaken) {
                val imageBitmap = createImageBitmap(context, tempFileUrl)
                onImageCaptured(imageBitmap, tempFileUrl?.toString())
            } else {
                tempFileUrl = null
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                tempFileUrl = createTempFileUrl(context)
                tempFileUrl?.let { cameraLauncher.launch(it) }
            }
        }

    val savedRecords by viewModel.savedRecords.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(innerPadding),
        ) {
            if (savedRecords.isEmpty()) {
                EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 16.dp,
                        bottom = 84.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = savedRecords,
                        key = { it.id }
                    ) { record ->
                        NutritionRecordItem(
                            record = record,
                            onClick = { onRecordClick(record) }
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                icon = { Icon(Icons.Default.CameraEnhance, "Capture image") },
                text = { Text("Capture photo") },
                onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            )
        }
    }
}

@Composable
fun NutritionRecordItem(
    record: NutritionRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column() {
            AsyncImage(
                model = record.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                placeholder = painterResource(R.drawable.placeholder_food),
                contentScale = ContentScale.Crop
            )
        }

        Box() {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.ic_fire_24),
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                    Text(
                        text = "${record.calories} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.size(4.dp))
                Text(
                    text = record.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.size(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Protein: ${record.protein}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Carbs: ${record.protein}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Fat: ${record.protein}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Fiber: ${record.fiber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Box(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

fun createImageBitmap(context: Context, tempFileUri: Uri?): Bitmap? {
    return tempFileUri?.let {
        val imageInputStream = context.contentResolver.openInputStream(it)
        val bitmap = BitmapFactory.decodeStream(imageInputStream)
        imageInputStream?.close()
        bitmap
    }
}

fun createTempFileUrl(context: Context): Uri? {
    val tempFile = File.createTempFile(
        "temp_image_file_",
        ".jpg",
        context.cacheDir
    )

    return FileProvider.getUriForFile(
        context,
        context.packageName + ".fileprovider",
        tempFile
    )
}


@Preview
@Composable
private fun Preview() {
    NutriVisionTheme() {
        HomeScreen(
            viewModel = hiltViewModel<MainViewModel>(),
            onImageCaptured = { _, _ -> }
        )
    }
}

@Preview
@Composable
private fun NutritionRecordItemPreview() {
    NutriVisionTheme() {
        NutritionRecordItem(
            record = NutritionRecord(
                id = 1,
                name = "Apple",
                type = "Fruit",
                servingSize = "100g",
                calories = 100,
                protein = 10,
                carbs = 10,
                imagePath = "",
                info = emptyList(),
                fat = 11,
                fiber = 10
            ),
            onClick = {}
        )
    }
}