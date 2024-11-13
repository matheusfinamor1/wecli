package com.matheusfinamor.wecli.uiLayer.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matheusfinamor.wecli.uiLayer.ui.theme.openSansFontFamily

@Composable
fun ItemDescriptionDetails(
    modifier: Modifier = Modifier,
    item: String,
    titleDescriptionItem: String,
    unitOfMeasurement: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = Color.LightGray,
                    shape = ShapeDefaults.Small,
                    width = 1.dp
                )
                .clip(RoundedCornerShape(14.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titleDescriptionItem,
                fontFamily = openSansFontFamily,
                fontSize = 12.sp
            )
            Text(
                text = "$item $unitOfMeasurement",
                fontFamily = openSansFontFamily,
                fontSize = 16.sp
            )
        }
    }
}