package com.absut.nutrivision.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ConnectingAirports
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.absut.nutrivision.R
import com.absut.nutrivision.ui.theme.NutriVisionTheme
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.absut.nutrivision.ui.AppViewState
import com.absut.nutrivision.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    modifier: Modifier = Modifier,
    onGetNutrition: (Bitmap?) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var tempFileUrl by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { imageTaken ->
            if (imageTaken) {
                val imageBitmap = createImageBitmap(context, tempFileUrl)
                viewModel.onImageCaptured(imageBitmap, tempFileUrl?.toString())
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

    val launchCamera: () -> Unit = {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    BackHandler {
        onClose()
    }

    LaunchedEffect(viewState.error) {
        viewState.error?.let { errorMsg ->
            snackbarHostState.showSnackbar(
                message = errorMsg,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            viewModel.clearError()
        }
    }

    PreviewScreenContent(
        viewState = viewState,
        onClose = onClose,
        onGetNutrition = { bitmap -> onGetNutrition(bitmap) },
        onChangePhoto = launchCamera,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreenContent(
    modifier: Modifier = Modifier,
    viewState: AppViewState,
    onClose: () -> Unit,
    onGetNutrition: (Bitmap?) -> Unit,
    onChangePhoto: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, "Navigate back")
                    }
                }
            )
        },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (viewState.imagePath != null) {
                /*Image(
                    bitmap = viewState.bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onChangePhoto() }
                )*/
                AsyncImage(
                    model = viewState.imagePath,
                    contentDescription = "Food image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_food),
                    error = painterResource(id = R.drawable.placeholder_food),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onChangePhoto() }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_food),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onChangePhoto() }
                )
            }
            Spacer(Modifier.size(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info, contentDescription = null,
                    Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Text(
                    text = "Your captured image will be shared with our AI model for analysis. For the most accurate results, please ensure the image clearly displays only food items. By proceeding, you consent to this image being processed to generate nutritional information.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Ready to uncover the nutrition in your meal?",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.size(12.dp))
            Button(
                onClick = {
                    if (!viewState.loading) {
                        onGetNutrition(viewState.bitmap)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(64.dp)

            ) {
                if (viewState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        contentDescription = null,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search_ai),
                    )
                }
                Spacer(Modifier.size(8.dp))
                Text(text = if (viewState.loading) "Generating.." else "Get Nutrition")
            }

        }
    }
}

@Preview
@Composable
private fun PreviewScreenContentDefault() {
    NutriVisionTheme {
        PreviewScreenContent(
            viewState = AppViewState(loading = false, bitmap = null),
            onClose = {}, onGetNutrition = {}, onChangePhoto = {},snackbarHostState = SnackbarHostState()
        )
    }
}

@Preview
@Composable
private fun PreviewScreenContentLoaded() {
    NutriVisionTheme {
        // Create a dummy bitmap for preview
        val dummyBitmap = remember { Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) }
        PreviewScreenContent(
            viewState = AppViewState(loading = false, bitmap = dummyBitmap),
            onClose = {}, onGetNutrition = {}, onChangePhoto = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}


@Preview
@Composable
private fun PreviewScreenContentLoading() {
    NutriVisionTheme {
        // Create a dummy bitmap for preview
        val dummyBitmap = remember { Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) }
        PreviewScreenContent(
            viewState = AppViewState(loading = true, bitmap = dummyBitmap),
            onClose = {}, onGetNutrition = {}, onChangePhoto = {},snackbarHostState = SnackbarHostState()
        )
    }
}