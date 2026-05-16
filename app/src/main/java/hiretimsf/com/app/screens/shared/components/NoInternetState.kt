package hiretimsf.com.app.screens.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hiretimsf.com.app.R

private val noInternetFontFamily = FontFamily(Font(R.font.questrial))

@Composable
fun NoInternetState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorSurface))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_connection),
            contentDescription = null,
        )
        Text(
            text = "No internet connection",
            color = colorResource(R.color.colorOnPrimarySurface),
            fontFamily = noInternetFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 18.dp),
        )
        Text(
            text = message,
            color = colorResource(R.color.colorOnSurface),
            fontFamily = noInternetFontFamily,
            fontSize = 15.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
        PrimaryGradientButton(
            onClick = onRetry,
            modifier = Modifier.padding(top = 18.dp),
        ) {
            Text(
                text = "Try again",
                color = primaryActionContentColor(),
                fontFamily = noInternetFontFamily,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
