package lib.dehaat.ledger.presentation.ledger.details.interest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.BlueGreen10
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Mustard10
import lib.dehaat.ledger.resources.Neutral100
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.spanButtonB2
import lib.dehaat.ledger.resources.spanParagraphT2Highlight
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
    showBackground = true,
    name = "InterestDetailScreen Preview"
)
@Composable
private fun InterestDetailScreenPreview() = LedgerTheme {
    InterestDetailScreen(
        interestViewData = null,
        ledgerColors = LedgerSDK.currentApp.ledgerColors
    ) {}
}

@Composable
fun InterestDetailScreen(
    interestViewData: InterestViewData?,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit
) {
    CommonContainer(
        title = stringResource(id = R.string.interest_amount_ledger),
        onBackPress = onBackPress,
        backgroundColor = Background,
        ledgerColors = ledgerColors
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.interest_amount_ledger),
                    style = textParagraphT1Highlight(Neutral90)
                )
                VerticalSpacer(height = 4.dp)

                Text(
                    text = interestViewData?.amount.getAmountInRupees(),
                    style = textHeadingH3(Neutral80)
                )
                VerticalSpacer(height = 16.dp)


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Mustard10, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.interest_arrears),
                            style = textParagraphT2Highlight(Neutral80)
                        )

                        Text(text = interestViewData?.interest.getAmountInRupees())
                    }

                    Icon(
                        modifier = Modifier.padding(8.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = stringResource(id = R.string.accessibility_icon)
                    )

                    Column(
                        modifier = Modifier
                            .background(color = BlueGreen10, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.total_interest_payment),
                            style = textParagraphT2Highlight(Neutral80)
                        )

                        Text(text = interestViewData?.totalPayment.getAmountInRupees())
                    }
                }
            }

            /*VerticalSpacer(height = 16.dp)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            VerticalSpacer(height = 20.dp)

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp),
                                text = stringResource(id = R.string.daily_interest_details),
                                style = textSubHeadingS3(Neutral80)
                            )

                            VerticalSpacer(height = 12.dp)

                            Divider()

                            VerticalSpacer(height = 16.dp)
                        }
                    }

                    items(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) { num ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 15.dp, bottom = 19.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "16-जुलाई-2022",
                                style = textParagraphT2Highlight(Neutral80)
                            )
                            Text(
                                text = "+ ₹ 480",
                                style = textButtonB2(Neutral80)
                            )
                        }

                        Divider(modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }*/

            VerticalSpacer(height = 16.dp)

            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = getInformationalString()
            )
        }
    }
}

@Composable
private fun getInformationalString() = buildAnnotatedString {
    withStyle(spanButtonB2(Neutral100)) {
        append(stringResource(id = R.string.information_))
    }
    append(" ")
    withStyle(spanParagraphT2Highlight(Neutral100)) {
        append(stringResource(id = R.string.weekly_interest_information))
    }
}

data class InterestViewData(
    val amount: String,
    val interest: String,
    val totalPayment: String
)
