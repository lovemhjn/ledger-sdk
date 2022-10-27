package lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.ui.component.CalculationMethodScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.RevampKeyValueChip
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.BlueGreen10
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Mustard10
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Neutral60
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.SeaGreen100
import lib.dehaat.ledger.resources.textButtonB2
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.getAmountInRupees

@Preview(
    showBackground = true
)
@Composable
private fun AvailableCreditLimitDetailsScreenPreview() = LedgerTheme {
    CreditLimitDetailsScreen(
        AvailableCreditLimitViewState(
            totalAvailableCreditLimit = "4569",
            totalCreditLimit = "100000",
            outstandingAndDeliveredAmount = "237498",
            permanentCreditLimit = "2347893",
            bufferLimit = "236489",
            totalLimit = "832940"
        )
    )
}

@Composable
fun AvailableCreditLimitDetailsScreen(
    uiState: AvailableCreditLimitViewState?,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    CommonContainer(
        title = stringResource(id = R.string.available_credit_limit),
        onBackPress = onBackPress,
        scaffoldState = scaffoldState,
        ledgerColors = ledgerColors,
        backgroundColor = Background
    ) {
        uiState?.let {
            CreditLimitDetailsScreen(uiState)
        }
    }
}

@Composable
fun CreditLimitDetailsScreen(uiState: AvailableCreditLimitViewState) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(state = rememberScrollState())
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        VerticalSpacer(height = 24.dp)

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(id = R.string.available_credit_limit),
            style = textParagraphT1(Neutral80)
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = uiState.totalAvailableCreditLimit.getAmountInRupees(),
            style = textHeadingH3(Neutral80)
        )

        VerticalSpacer(height = 12.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RevampKeyValueChip(
                modifier = Modifier
                    .weight(1.5F),
                key = stringResource(id = R.string.total_credit_limit),
                value = uiState.totalCreditLimit.getAmountInRupees(),
                backgroundColor = BlueGreen10
            )

            Icon(
                modifier = Modifier.padding(horizontal = 8.dp),
                painter = painterResource(id = R.drawable.ledger_minus),
                contentDescription = stringResource(id = R.string.accessibility_icon),
                tint = Neutral80
            )

            RevampKeyValueChip(
                modifier = Modifier
                    .weight(2.5F),
                key = stringResource(id = R.string.total_outstanding_plus_products_to_be_deliver),
                value = uiState.outstandingAndDeliveredAmount.getAmountInRupees(),
                backgroundColor = Mustard10
            )
        }

        VerticalSpacer(height = 16.dp)

        if (uiState.bufferLimit.toDoubleOrNull().orZero() > 1) {
            Divider()

            VerticalSpacer(height = 32.dp)

            VerticalSpacer(height = 8.dp)

            CalculationMethodScreen(
                backgroundColor = BlueGreen10,
                dividerColor = Color.White,
                title = stringResource(id = R.string.total_credit_limit_calculation_method),
                first = Pair(
                    stringResource(id = R.string.permanent_credit_limit),
                    uiState.permanentCreditLimit.getAmountInRupees()
                ),
                second = Pair(
                    stringResource(id = R.string.buffer_limit),
                    uiState.bufferLimit.getAmountInRupees()
                ),
                total = Pair(
                    stringResource(id = R.string.total_credit_limit),
                    uiState.totalLimit.getAmountInRupees()
                )
            )

            VerticalSpacer(height = 16.dp)
        }
    }

    VerticalSpacer(height = 16.dp)

    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(
                    top = 20.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_idea_bulb),
                contentDescription = stringResource(id = R.string.accessibility_icon),
            )

            HorizontalSpacer(width = 8.dp)

            Text(
                text = stringResource(id = R.string.how_does_payment_increase_credit_limit),
                style = textSubHeadingS3(Neutral80)
            )
        }

        Divider()

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            InformationalScreen(true)
            VerticalSpacer(height = 12.dp)
            InformationalScreen(false)
        }
    }
}

@Composable
private fun InformationalScreen(
    isFullPaymentInfo: Boolean
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(color = Neutral10, shape = RoundedCornerShape(8.dp))
) {
    var isDetailsVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isDetailsVisible = !isDetailsVisible
            }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(
                id = if (isFullPaymentInfo) {
                    R.string.when_does_total_payment_amount_adds_up
                } else {
                    R.string.when_does_payment_amount_not_adds_up
                }
            ),
            style = textParagraphT1Highlight(Neutral80)
        )

        val modifier = if (isDetailsVisible) {
            Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
        } else {
            Modifier
                .size(24.dp)
                .clip(CircleShape)
        }

        Box(modifier = modifier) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(
                    id = if (isDetailsVisible) {
                        R.drawable.ic_up
                    } else {
                        R.drawable.ic_down
                    }
                ),
                contentDescription = stringResource(id = R.string.accessibility_icon),
                tint = SeaGreen100
            )
        }
    }

    if (isDetailsVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(
                    id = if (isFullPaymentInfo) {
                        R.string.when_you_dont_owe_interest
                    } else {
                        R.string.when_you_owe_interest
                    }
                ),
                style = textButtonB2(Neutral70)
            )
            if (!isFullPaymentInfo) {
                Text(
                    text = stringResource(id = R.string.payment_amount_used_to_pay_outstanding_interest),
                    style = textCaptionCP1(Neutral80)
                )
            }

            VerticalSpacer(height = 8.dp)

            Text(
                text = stringResource(id = R.string.example),
                style = textParagraphT2Highlight(Neutral80)
            )

            VerticalSpacer(height = 4.dp)

            Text(
                text = stringResource(
                    id = if (isFullPaymentInfo) {
                        R.string.full_payment_example
                    } else {
                        R.string.partial_payment_example
                    }
                ),
                style = textParagraphT2Highlight(Neutral60)
            )
        }
    }

    VerticalSpacer(height = 8.dp)
}
