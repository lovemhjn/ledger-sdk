package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.fresco.FrescoImage
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.invoice.ProductViewDataV2
import lib.dehaat.ledger.presentation.model.revamp.invoice.ProductsInfoViewDataV2
import lib.dehaat.ledger.resources.BorderColor
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun ProductDetailsScreen(
    productDetails: ProductsInfoViewDataV2?
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
) {
    VerticalSpacer(height = 20.dp)

    Text(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        text = stringResource(id = R.string.product_details),
        style = textSubHeadingS3(Neutral80)
    )

    productDetails?.count?.let {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = stringResource(id = R.string.total_items_, it),
            style = textCaptionCP1(Neutral70)
        )
    }

    VerticalSpacer(height = 12.dp)

    Divider()

    Column(modifier = Modifier.padding(20.dp)) {
        VerticalSpacer(height = 12.dp)

        productDetails?.productList?.forEach {
            RevampProductView(it)
            VerticalSpacer(height = 12.dp)
        }
        Divider()

        VerticalSpacer(height = 12.dp)

        val commonStyle = textParagraphT2Highlight(Neutral80)

        RevampKeyValuePair(
            pair = Pair(
                stringResource(id = R.string.purchase_amount),
                productDetails?.itemTotal.getAmountInRupees()
            ),
            style = Pair(commonStyle, commonStyle)
        )

        VerticalSpacer(height = 12.dp)

        RevampKeyValuePair(
            pair = Pair(
                stringResource(id = R.string.discount),
                productDetails?.discount.getAmountInRupees()
            ),
            style = Pair(commonStyle, commonStyle)
        )

        VerticalSpacer(height = 12.dp)

        RevampKeyValuePair(
            pair = Pair(stringResource(id = R.string.gst), productDetails?.gst.getAmountInRupees()),
            style = Pair(commonStyle, commonStyle)
        )

        VerticalSpacer(height = 8.dp)

        Divider()

        val totalAmountStyle = textParagraphT1Highlight(Neutral80)
        RevampKeyValuePair(
            pair = Pair(
                stringResource(id = R.string.total_amount),
                productDetails?.subTotal.getAmountInRupees()
            ),
            style = Pair(totalAmountStyle, totalAmountStyle)
        )
    }
}

@Composable
private fun RevampProductView(
    product: ProductViewDataV2
) = Row(
    modifier = Modifier
        .fillMaxWidth()
) {
    FrescoImage(
        imageUrl = product.fname,
        failure = {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(R.drawable.default_product),
                contentDescription = "",
            )
        },
        modifier = Modifier
            .height(62.dp)
            .width(55.dp)
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color.White)
            .padding(8.dp)
    )

    HorizontalSpacer(width = 12.dp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = stringResource(id = R.string.product_quantity, product.quantity ?: 0),
                style = textParagraphT2Highlight(Neutral80)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.fname ?: "",
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = product.priceTotal.getAmountInRupees(),
                style = textParagraphT2Highlight(Neutral80)
            )
        }
    }
}
