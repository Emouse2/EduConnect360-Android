package com.example.CuratED

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.*

@Composable
fun ColorPicker(onConfirm: (String) -> Unit) {
    val controller = rememberColorPickerController()
    var colorChosen by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
        HsvColorPicker(
            modifier = Modifier
                .width(200.dp)
                .aspectRatio(1f)
                .padding(10.dp),
            controller = controller,
            onColorChanged = {
                colorChosen = it.hexCode
            }
        )
        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(20.dp)),
            controller = controller,
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onConfirm(colorChosen) },
                modifier = Modifier.padding(10.dp)
            ) {
                Text("Confirm")
            }
            AlphaTile(
                modifier = Modifier
                    .width(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(6.dp)),
                controller = controller
            )
        }
    }
}