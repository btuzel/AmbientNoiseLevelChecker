package com.example.desibel

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.desibel.ui.theme.DesibelTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = MainViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(viewModel.uiState.collectAsStateLifecycleAware().value) {
                viewModel.sendValue()
            }
            DesibelTheme {
                when (val state = viewModel.uiState.collectAsStateLifecycleAware().value) {
                    is MainViewModel.UiState.Loading -> Loaders()
                    is MainViewModel.UiState.Success -> Content(state.value)
                }
            }
        }
    }
}

@Composable
fun Loaders() {

}

@Composable
fun Content(double: Double) {
    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = double.toString())
        }
    }
}


//to show alex, put loaders content and 23-33 to decibel screen and remove line 21