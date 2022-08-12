package com.euzhene.instagramcollage.utils

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


fun Modifier.clickWithoutRipple(onClick: () -> Unit):Modifier {
    return clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )
}