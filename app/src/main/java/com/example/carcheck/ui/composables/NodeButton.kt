package com.example.carcheck.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.carcheck.data.model.CarNode

@Composable
fun NodeButton(
    index:Int,
    node: CarNode,
    offset: IntOffset,
    size: Dp,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offset { offset }
            .size(size)
    ) {

        AnimatedWobbleButton(
            index = index,
            text = (index + 1).toString(),
            isSelected = isSelected,
            onClick = onClick,
            modifier = Modifier.size(size)
        )
    }
}
