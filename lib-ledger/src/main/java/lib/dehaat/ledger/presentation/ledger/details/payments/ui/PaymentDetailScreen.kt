package lib.dehaat.ledger.presentation.ledger.details.payments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValue
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryViewWithTopPadding
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun PaymentDetailScreen(
    viewModel: PaymentDetailViewModel,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit
) {
    HandleAPIErrors(viewModel.uiEvent)
    val uiState by viewModel.uiState.collectAsState()
    val paymentSummary = uiState.paymentDetailSummaryViewData
    val scrollState = rememberScrollState()

    CommonContainer(
        title = "Payment Detail",
        onBackPress = onBackPress,
        ledgerColors = ledgerColors
    ) {
        when {
            uiState.isLoading -> {
                ShowProgressDialog(ledgerColors) {
                    viewModel.updateProgressDialog(false)
                }
            }
            uiState.state is UIState.ERROR -> {
                NoDataFound{}
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            state = scrollState,
                            enabled = true,
                        )
                        .background(Color.White)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    paymentSummary?.totalAmount?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            CreditNoteKeyValue(
                                stringResource(id = R.string.payment_amount),
                                it.getAmountInRupees(),
                                keyTextStyle = text18Sp(textColor = ledgerColors.CtaDarkColor),
                                valueTextStyle = text18Sp(
                                    fontWeight = FontWeight.Bold,
                                    textColor = ledgerColors.CtaDarkColor
                                ),
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                        }
                    }

                    SpaceMedium()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            /*if (viewModel.isLmsActivated()) {
                                paymentSummary?.principalComponent?.let {
                                    CreditNoteKeyValueInSummaryView(
                                        "Principal Paid",
                                        it.getAmountInRupees(),
                                        ledgerColors = ledgerColors,
                                    )
                                }

                                paymentSummary?.penaltyComponent?.let {
                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Penalty Paid",
                                        it.getAmountInRupees(),
                                        ledgerColors = ledgerColors,
                                    )
                                }

                                paymentSummary?.advanceComponent?.let {
                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Advance Paid",
                                        it.getAmountInRupees(),
                                        ledgerColors = ledgerColors,
                                    )
                                }
                            }*/

                            if (viewModel.isLmsActivated() == true) {
                                paymentSummary?.paidTo?.let {
                                    CreditNoteKeyValueInSummaryViewWithTopPadding(
                                        "Paid To",
                                        it,
                                        ledgerColors = ledgerColors,
                                    )
                                }
                            }

                            viewModel.paymentMode?.let {
                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Payment Method",
                                    viewModel.paymentMode ?: "",
                                    ledgerColors = ledgerColors,
                                )
                            }

                            paymentSummary?.referenceId?.let {
                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Reference ID",
                                    it,
                                    ledgerColors = ledgerColors,
                                )
                            }

                            paymentSummary?.timestamp?.let {
                                CreditNoteKeyValueInSummaryViewWithTopPadding(
                                    "Payment Date",
                                    it.toDateMonthYear(),
                                    ledgerColors = ledgerColors,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
