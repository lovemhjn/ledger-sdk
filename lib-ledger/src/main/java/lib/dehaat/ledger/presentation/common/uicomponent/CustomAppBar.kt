package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.TextBlack
import lib.dehaat.ledger.resources.subTitleTextStyle

@Composable
fun CustomAppBar(
    title: String,
    ledgerColors: LedgerColors,
    subtitle: String = "",
    iconRight: Painter? = null,
    onBackPress: () -> Unit,
    onRightIconClick: (() -> Unit)? = null,
    appBarElevation: Dp = 8.dp,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = appBarElevation,
        color = ledgerColors.ActionBarColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (subtitle.isEmpty()) 56.dp else 75.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

	        IconButton(onClick = onBackPress, modifier = Modifier.padding(start = 8.dp)) {
		        Icon(
                    painter = painterResource(id = if (LedgerSDK.isDBA) R.drawable.ic_back_dba else R.drawable.ic_back_black),
			        contentDescription = "back button"
		        )
	        }

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    color = TextBlack,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 21.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = subTitleTextStyle()
                    )
                }
            }

            iconRight?.let { painter ->
                IconButton(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(23.dp),
                    onClick = { onRightIconClick?.invoke() }) {
                    Image(
                        painter = painter,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}