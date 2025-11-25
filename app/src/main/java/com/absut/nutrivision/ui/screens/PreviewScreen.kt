package com.absut.nutrivision.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ConnectingAirports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.absut.nutrivision.R
import com.absut.nutrivision.ui.theme.NutriVisionTheme
import androidx.core.net.toUri
import com.absut.nutrivision.ui.AppViewState
import com.absut.nutrivision.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    modifier: Modifier = Modifier,
    onGetNutrition: (Bitmap?) -> Unit,
    onClose: () -> Unit,
    onChangePhoto: () -> Unit,
    onRemovePhoto: () -> Unit,
    viewState: AppViewState
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (viewState.bitmap != null) {
                Image(
                    bitmap = viewState.bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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
                )
            }
            Spacer(Modifier.size(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                FilledTonalButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    onClick = onChangePhoto
                ) { Text("Change Photo") }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = onRemovePhoto
                ) { Text("Remove Photo") }
            }

            Spacer(Modifier.weight(1f))

            if (viewState.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = { onGetNutrition(viewState.bitmap) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(96.dp)
                        .padding(16.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        contentDescription = null,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search_ai),
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Get Nutrition")
                }
            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    NutriVisionTheme {
        PreviewScreen(
            onGetNutrition = {}, onClose = {}, onChangePhoto = {}, onRemovePhoto = {},
            viewState = AppViewState(loading = false) // Preview for non-loading state
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    NutriVisionTheme {
        PreviewScreen(
            onGetNutrition = {}, onClose = {}, onChangePhoto = {}, onRemovePhoto = {},
            viewState = AppViewState(loading = true) // Preview for loading state
        )
    }
}