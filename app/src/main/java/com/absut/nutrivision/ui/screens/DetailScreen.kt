package com.absut.nutrivision.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.BreakfastDining
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.absut.nutrivision.R
import com.absut.nutrivision.ui.theme.NutriVisionTheme
import com.absut.nutrivision.model.InfoItem
import com.absut.nutrivision.model.NutritionRecord
import com.absut.nutrivision.model.NutritionResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    nutritionResult: NutritionResult? = null,
    record: NutritionRecord? = null,
    onNavigateBack: () -> Unit,
    onDelete: (NutritionRecord) -> Unit
) {
    val showDeleteDialog = rememberSaveable { mutableStateOf(false) }

    val defaultInfo: List<InfoItem> = listOf(
        InfoItem("Sodium", "--"),
        InfoItem("Total Sugars", "-"),
        InfoItem("Dietary Fiber", "-"),
        InfoItem("Potassium", "-"),
        InfoItem("Vitamin C", "-"),
        InfoItem("Omega 3", "-")
    )


    val imagePath = record?.imagePath ?: nutritionResult?.imagePath
    val displayName = record?.name ?: nutritionResult?.name ?: "Unknown Food Item"
    val displayCalories = record?.calories ?: nutritionResult?.calories ?: 0
    val displayType = record?.type ?: nutritionResult?.type ?: "Unknown Type"
    val displayServingSize = record?.servingSize ?: nutritionResult?.servingSize ?: "per 100g"
    val displayProtein = record?.protein ?: nutritionResult?.protein ?: 0
    val displayCarbs = record?.carbs ?: nutritionResult?.carbs ?: 0
    val displayFat = record?.fat ?: nutritionResult?.fat ?: 0
    val displayFiber = record?.fiber ?: nutritionResult?.fiber ?: 0
    val displayInfo = record?.info ?: nutritionResult?.info ?: defaultInfo

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nutrition Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    if (record != null) {
                        IconButton(onClick = { showDeleteDialog.value = true }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                        }
                    }
                    /*IconButton(onClick = { *//*TODO*//* }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }*/
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
                AsyncImage(
                    model = imagePath,
                    contentDescription = nutritionResult?.name ?: "Food image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_food),
                    error = painterResource(id = R.drawable.placeholder_food),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                )


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Chip(displayType)
                Chip(displayServingSize)
            }
            Spacer(modifier = Modifier.height(24.dp))

            CaloriesCard(calories = displayCalories)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientCard(
                    name = "Protein",
                    value = "${displayProtein}g",
                    icon = Icons.Outlined.FitnessCenter,
                    color = Color(0xFFE3F3E9),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                NutrientCard(
                    name = "Carbs",
                    value = "${displayCarbs}g",
                    icon = Icons.Outlined.BakeryDining,
                    color = Color(0xFFE3F1F3),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientCard(
                    name = "Fat",
                    value = "${displayFat}g",
                    icon = Icons.Outlined.BreakfastDining,
                    color = Color(0xFFF0E3F3),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                NutrientCard(
                    name = "Fiber",
                    value = "${displayFiber}g",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_nutrition_24),
                    color = Color(0xFFF3EFE3),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            NutritionInfoList(items = displayInfo)

            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "This information is AI generated and should be used for informational purposes only. Consult a professional for specific dietary advice.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteDialog.value && record != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Delete record?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog.value = false
                    onDelete(record)
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}

@Composable
fun Chip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
fun CaloriesCard(modifier: Modifier = Modifier, calories: Int) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        //elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total Calories",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = calories.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = "kcal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF3E7E3), CircleShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = "Calories",
                    tint = Color(0xFFE75828)
                )
            }
        }
    }
}

@Composable
fun NutrientCard(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        //elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = name, tint = Color.Unspecified)
            }
            Column(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun NutritionInfoList(modifier: Modifier = Modifier, items: List<InfoItem>) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        //elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.label, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = item.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (index < items.lastIndex) {
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    NutriVisionTheme {
        DetailScreen(nutritionResult = null, onNavigateBack = {}, onDelete = {})
    }
}