package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.fresco.FrescoImage
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.R

@Composable
fun ProductImageLedger(ledgerColors: LedgerColors, imgUrl: String) {
    FrescoImage(
        imageUrl = imgUrl,
        placeHolder = painterResource(R.drawable.default_product),
        contentDescription = "",
        modifier = Modifier
            .height(54.dp)
            .width(52.dp)
            .border(
                width = 1.dp,
                color = ledgerColors.TabBorderColorDefault,
                shape = RoundedCornerShape(6.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(6.dp))
            .padding(8.dp)
    )
}