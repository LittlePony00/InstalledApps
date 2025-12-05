package com.android.installedapps.feature.app_detail.presentation

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.installedapps.core.ui.ObserveAsEvents
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: AppDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.action) { action ->
        when (action) {
            is AppDetailContract.Action.LaunchApp -> {
                launchApp(context, action.packageName)
            }
            is AppDetailContract.Action.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.appDetail?.name ?: "App Details") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(AppDetailContract.Intent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        contentWindowInsets = WindowInsets.displayCutout.union(WindowInsets.systemBars)
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()

        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(paddingValues)
                    )
                }
                state.error != null -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(paddingValues),
                        text = state.error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                state.appDetail != null -> {
                    AppDetailContent(
                        innerPaddings = paddingValues,
                        icon = state.appDetail?.icon,
                        name = state.appDetail?.name ?: "",
                        version = state.appDetail?.version ?: "",
                        packageName = state.appDetail?.packageName ?: "",
                        checksum = state.appDetail?.checksum ?: "",
                        onLaunchClick = { viewModel.handleIntent(AppDetailContract.Intent.LaunchApp) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppDetailContent(
    name: String,
    version: String,
    icon: Drawable?,
    checksum: String,
    packageName: String,
    innerPaddings: PaddingValues,
    onLaunchClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(innerPaddings)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = icon),
            contentDescription = name,
            modifier = Modifier.size(96.dp)
        )

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailRow(label = "Version", value = version)
                DetailRow(label = "Package", value = packageName)
                DetailRow(label = "MD5 Checksum", value = checksum, isMonospace = true)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onLaunchClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open App")
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isMonospace: Boolean = false
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default
        )
    }
}

private fun launchApp(context: Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
