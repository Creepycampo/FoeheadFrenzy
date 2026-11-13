package com.johnmuindi.foreheadfrenzykenya.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun CurvedGameTitle(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        val cx = size.width / 2f
        val nativeCanvas = drawContext.canvas.nativeCanvas

        // Reusable paint builder
        fun textPaint(
            textSizePx: Float,
            hexColor: String,
            isStroke: Boolean = false,
            strokeWidthPx: Float = 0f
        ) = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = textSizePx
            color = hexColor.toColorInt()
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT,
                android.graphics.Typeface.BOLD
            )
            textAlign = android.graphics.Paint.Align.CENTER
            style = if (isStroke) android.graphics.Paint.Style.STROKE
            else android.graphics.Paint.Style.FILL
            this.strokeWidth = strokeWidthPx
            strokeJoin = android.graphics.Paint.Join.ROUND
            strokeCap  = android.graphics.Paint.Cap.ROUND
        }

        // Draws text centered on an arc, with black outline then color fill
        fun drawArched(
            text: String,
            radius: Float,
            centerY: Float,
            startAngle: Float,
            sweepAngle: Float,
            fillHex: String,
            textSizePx: Float,
            strokePx: Float = 15f
        ) {
            val oval = android.graphics.RectF(
                cx - radius, centerY - radius,
                cx + radius, centerY + radius
            )
            val path = android.graphics.Path().also { it.addArc(oval, startAngle, sweepAngle) }

            val hOffset = android.graphics.PathMeasure(path, false).length / 2f


            nativeCanvas.drawTextOnPath(
                text, path, hOffset, 0f,
                textPaint(textSizePx, "#000000", isStroke = true, strokeWidthPx = strokePx)
            )

            nativeCanvas.drawTextOnPath(
                text, path, hOffset, 0f,
                textPaint(textSizePx, fillHex)
            )
        }


        drawArched(
            text       = "FOREHEAD",
            radius     = size.width * 0.50f,  // ← smaller = tighter curve
            centerY    = size.width * 0.61f,  // ← smaller = higher on screen
            startAngle = 210f,
            sweepAngle = 120f,
            fillHex    = "#FFD700",
            textSizePx = size.width * 0.11f,
            strokePx   = 16f
        )

        drawArched(
            text       = "FRENZY",
            radius     = size.width * 0.44f,  // ← smaller = tighter curve
            centerY    = size.width * 0.86f,  // ← larger = lower on screen
            startAngle = 215f,
            sweepAngle = 110f,
            fillHex    = "#FFFFFF",
            textSizePx = size.width * 0.155f,
            strokePx   = 18f
        )
    }
}